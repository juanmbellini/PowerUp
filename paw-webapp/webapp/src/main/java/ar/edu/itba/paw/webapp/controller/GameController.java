package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;
import ar.edu.itba.paw.webapp.form.RateAndStatusForm;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * This controller is in charge of handling games' operations requests.
 */
@Controller
//@Transactional  //TODO die die die die die die die die
public class GameController extends BaseController {


    /**
     * Default page size for games search results.
     */
    private static final int DEFAULT_PAGE_SIZE = 25;
    /**
     * Default page number for game search results.
     */
    public static final int DEFAULT_PAGE_NUMBER = 1;


    /**
     * A game service to make games operations.
     */
    private final GameService gameService;
    /**
     * A Object Mapper to generate Objects from JSONs.
     */
    private final ObjectMapper objectMapper;
    /**
     * A TypeReference that references to a {@code Map<{@link FilterCategory}, List<String>>}, which represents
     * filters that can be applied to game searches.
     * For more info. see {@link #search(String, String, String, String, String, String)}.
     */
    private final TypeReference<Map<FilterCategory, ArrayList<String>>> typeReference;

    private final PlatformService platformService;

    private final DeveloperService developerService;

    private final PublisherService publisherService;

    private final GenreService genreService;


    @Autowired
    public GameController(GameService gameService, UserService us, PlatformService platformService, DeveloperService developerService, PublisherService publisherService, GenreService genreService) {
        super(us);
        this.gameService = gameService;
        this.platformService = platformService;
        this.developerService = developerService;
        this.publisherService = publisherService;
        this.genreService = genreService;
        objectMapper = new ObjectMapper();
        typeReference = new TypeReference<Map<FilterCategory, ArrayList<String>>>() {};
    }


    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "filters", required = false) String filtersStr,
                               @RequestParam(value = "orderCategory", required = false) String orderParameter,
                               @RequestParam(value = "orderBoolean", required = false) String orderBooleanStr,
                               @RequestParam(value = "pageSize", required = false) String pageSizeStr,
                               @RequestParam(value = "pageNumber", required = false) String pageNumberStr) {

        final ModelAndView mav = new ModelAndView();

        // This is done here in order to get query string parameters without further modification
        String changePageUrl = getUrlCreator().getSearchUrl(name, filtersStr, orderParameter, orderBooleanStr,
                pageSizeStr, null);
        String changeOrderUrl = getUrlCreator().getSearchUrl(name, filtersStr, null, null, pageSizeStr, null);
        String changePageSizeUrl = getUrlCreator().getSearchUrl(name, filtersStr, orderParameter,
                orderBooleanStr, null, null);

        name = name == null ? "" : name;
        filtersStr = (filtersStr == null || filtersStr.equals("")) ? "{}" : filtersStr;
        String orderCategory;
        boolean orderBoolean;
        int pageSize;
        int pageNumber;

        // TODO: make a new function for this
        // TODO: change string to use those in enum and avoid this
        if (orderParameter == null || orderParameter.equals("name")) {
            orderCategory = "name";
        } else if (orderParameter.equals("release")) {
            orderCategory = "release";
        } else if (orderParameter.equals("rating")) {
            orderCategory = "avg_score";
        } else {
            mav.setViewName("redirect:/error400");
            return mav;
        }

        if (orderBooleanStr == null || orderBooleanStr.equals("ascending")) {
            orderBoolean = true;
        } else if (orderBooleanStr.equals("descending")) {
            orderBoolean = false;
        } else {
            mav.setViewName("redirect:/error400");
            return mav;
        }

        Map<FilterCategory, List<String>> filters = null;
        try {
            filters = objectMapper.readValue(filtersStr, typeReference);

            pageSize = (pageSizeStr == null || pageSizeStr.equals("")) ? DEFAULT_PAGE_SIZE : new Integer(pageSizeStr);
            pageNumber = (pageNumberStr == null || pageNumberStr.equals("")) ?
                    DEFAULT_PAGE_NUMBER : new Integer(pageNumberStr);

            Page<Game> page = gameService.searchGames(name, filters, OrderCategory.valueOf(orderCategory),
                    orderBoolean, pageSize, pageNumber);
//            for(Game g : page.getData()) {
//                g.getPlatforms().size();
//            }

            mav.addObject("page", page);
            mav.addObject("hasFilters", !filtersStr.equals("{}")); // TODO: Check the applied filters size [JMB]
            mav.addObject("appliedFilters", filters);
            mav.addObject("searchedName", HtmlUtils.htmlEscape(name));
            mav.addObject("orderBoolean", orderBooleanStr);
            mav.addObject("orderCategory", orderParameter);
            mav.addObject("filters", filtersStr);

            mav.addObject("changePageUrl", changePageUrl);
            mav.addObject("changeOrderUrl", changeOrderUrl);
            mav.addObject("changePageSizeUrl", changePageSizeUrl);

            mav.setViewName("search");

        } catch (IOException | NumberFormatException | IllegalPageException e) {
            e.printStackTrace();  // Wrong filtersJson, pageSizeStr or pageNumberStr, or pageNumber strings
            mav.setViewName("redirect:/error400");
        }

        //Add all possible filters (categories and values) for the filters section,
        //indicating whether the filter is applied or not
        for (FilterCategory filterCategory : FilterCategory.values()) {
            try {
                Set<Object[]> values = new LinkedHashSet<>();
                for(String value : gameService.getFiltersByType(filterCategory)) {
                    values.add(new Object[] {value, filters != null && filters.containsKey(filterCategory) && filters.get(filterCategory).contains(value)});
                }
                mav.addObject(English.plural(filterCategory.name()).toUpperCase(), values);
            } catch (Exception e) {
                return new ModelAndView("redirect:/error500");
            }
        }
        return mav;
    }

    @RequestMapping("/advanced-search")
    public ModelAndView advancedSearch() {
        final ModelAndView mav = new ModelAndView("advanced-search");
        //Add all possible filter types, as strings, in ascending alphabetical order
        try {
            //Platforms
            Set<String> platforms = new TreeSet<>();
            for(Platform p : platformService.all()) {
                platforms.add(p.getName());
            }
            mav.addObject("platforms", platforms);

            //Developers
            Set<String> developers = new TreeSet<>();
            for(Developer p : developerService.all()) {
                developers.add(p.getName());
            }
            mav.addObject("developers", developers);

            //Publishers
            Set<String> publishers = new TreeSet<>();
            for(Publisher p : publisherService.all()) {
                publishers.add(p.getName());
            }
            mav.addObject("publishers", publishers);

            //Genres
            Set<String> genres = new TreeSet<>();
            for(Genre p : genreService.all()) {
                genres.add(p.getName());
            }
            mav.addObject("genres", genres);
        } catch (Exception e) {
            LOG.error("Error populating filter types", e);
            return new ModelAndView("redirect:/error500");
        }
        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game(@RequestParam(name = "id") long gameId,
                             @ModelAttribute("rateAndStatusForm") RateAndStatusForm rateAndStatusForm,
                             @ModelAttribute("currentUser") User user) {
        final ModelAndView mav = new ModelAndView("game");
        Game game;
        long userId = user.getId();
        Set<Game> relatedGames = new HashSet<>();
        try {
            game = gameService.findById(gameId);
            if (game == null) {
                return new ModelAndView("redirect:/error404");
            }
            if (user != null) {
                if (userService.hasScoredGame(userId, gameId)) {
                    rateAndStatusForm.setScore(userService.getGameScore(userId, gameId));
                }
                if (userService.hasPlayStatus(userId, gameId)) {
                    rateAndStatusForm.setPlayStatus(userService.getPlayStatus(userId, gameId));
                }
            }

            Set<FilterCategory> filters = new HashSet<>();
            filters.add(FilterCategory.platform);
            filters.add(FilterCategory.genre);
            //relatedGames = gameService.findRelatedGames(game, filters);
        } catch (Exception e) {
            return new ModelAndView("redirect:/error500");
        }
        List<Integer> scoreValues = new ArrayList<>();
        for (int i = 1; i <= 10; i++) scoreValues.add(i);
        mav.addObject("scoreValues", scoreValues);
        /*
            Pass a map of statuses to the <select> dropdown. The map's keys will be the form's values and the map's
            values will be what will get displayed to the user.
         */
        Map<PlayStatus, String> statuses = new LinkedHashMap<>();
        for (PlayStatus status : PlayStatus.values()) {
            statuses.put(status, status.getPretty());
        }
        mav.addObject("statuses", statuses);
        mav.addObject("game", game);
        mav.addObject("genres", gameService.getGenres(gameId));
        mav.addObject("platforms", gameService.getPlatforms(gameId));
        mav.addObject("developers", gameService.getDevelopers(gameId));
        mav.addObject("publishers", gameService.getPublishers(gameId));
        mav.addObject("relatedGames", relatedGames);
        return mav;
    }


    @RequestMapping(value = "/rateAndUpdateStatus", method = {RequestMethod.POST})
    public ModelAndView rateAndUpdateStatus(@Valid @ModelAttribute("rateAndStatusForm")
                                            final RateAndStatusForm rateAndStatusForm,
                                            final BindingResult errors,
                                            @RequestParam(name = "id") long id,
                                            final RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView("redirect:/game?id=" + id);


        if (errors.hasErrors()) {
            redirectAttributes.addFlashAttribute("rateAndStatusForm", rateAndStatusForm);
            return mav;
        }
        final User u = getCurrentUser();
        if (u == null) {     //This should never happen; Spring only gives access to this page to authenticated users
            return new ModelAndView("redirect:/login");
        }
        long userId = u.getId();

        Integer score = rateAndStatusForm.getScore();
        if (score != null) {
            userService.scoreGame(userId, id, score);
        } else {
            userService.removeScore(userId, id);
        }

        PlayStatus playStatus = rateAndStatusForm.getPlayStatus();
        if (playStatus != null) {
            userService.setPlayStatus(userId, id, playStatus);
        } else {
            userService.removeStatus(userId, id);
        }
        return mav;
    }
}