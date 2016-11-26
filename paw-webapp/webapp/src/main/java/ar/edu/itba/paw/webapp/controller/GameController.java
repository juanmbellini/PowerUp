package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.form.RateAndStatusForm;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private final CompanyService companyService;

    private final GenreService genreService;

    private final ShelfService shelfService;

    private final ReviewService reviewService;


    @Autowired
    public GameController(GameService gameService, UserService us, PlatformService platformService, CompanyService companyService, GenreService genreService, ShelfService shelfService, ReviewService reviewService) {
        super(us);
        this.gameService = gameService;
        this.companyService = companyService;
        this.reviewService = reviewService;
        this.platformService = platformService;
        this.genreService = genreService;
        this.shelfService = shelfService;
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

        //Add all possible filters (categories and values) for the filters section, indicating whether the filter is applied or not
        //Genres
        Set<Object[]> genres = new LinkedHashSet<>();
        for(Genre g : genreService.all()) {
            genres.add(new Object[] {g.getName(), filters != null && filters.containsKey(FilterCategory.genre) && filters.get(FilterCategory.genre).contains(g.getName())});
        }
        mav.addObject("genres", genres);
        //Platforms
        Set<Object[]> platforms = new LinkedHashSet<>();
        for(Platform p : platformService.all()) {
            platforms.add(new Object[] {p.getName(), filters != null && filters.containsKey(FilterCategory.platform) && filters.get(FilterCategory.platform).contains(p.getName())});
        }
        mav.addObject("platforms", platforms);
        //Developers
        Set<Object[]> developers = new LinkedHashSet<>();
        for(Company d : companyService.all()) {
            developers.add(new Object[] {d.getName(), filters != null && filters.containsKey(FilterCategory.developer) && filters.get(FilterCategory.developer).contains(d.getName())});
        }
        mav.addObject("developers", developers);
        //Publishers
        Set<Object[]> publishers = new LinkedHashSet<>();
        for(Company p : companyService.all()) {
            publishers.add(new Object[] {p.getName(), filters != null && filters.containsKey(FilterCategory.publisher) && filters.get(FilterCategory.publisher).contains(p.getName())});
        }
        mav.addObject("publishers", publishers);

        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game(@RequestParam(name = "id") long gameId,
                             @ModelAttribute("rateAndStatusForm") RateAndStatusForm rateAndStatusForm) {
        final ModelAndView mav = new ModelAndView("game");
        Game game;
        User currentUser = getCurrentUser();
        Collection<Game> relatedGames = new LinkedHashSet<>();
        try {
            game = gameService.findById(gameId);
            if (game == null) {
                return new ModelAndView("redirect:/error404");
            }
            if (currentUser != null) {
                long userId = currentUser.getId();
                if (userService.hasScoredGame(userId, gameId)) {
                    rateAndStatusForm.setScore(userService.getGameScore(userId, gameId));
                }
                if (userService.hasPlayStatus(userId, gameId)) {
                    rateAndStatusForm.setPlayStatus(userService.getPlayStatus(userId, gameId));
                }
                Map<Shelf, Boolean> shelves = new LinkedHashMap<>();
                for(Shelf shelf : shelfService.findByUserId(userId)) {
                    shelves.put(shelf, shelf.getGames().contains(game));
                }
                mav.addObject("shelves", shelves);
            }

            Set<FilterCategory> filters = new HashSet<>();
            filters.add(FilterCategory.platform);
            filters.add(FilterCategory.genre);
            relatedGames = gameService.findRelatedGames(game.getId(), filters);
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
        mav.addObject("reviews", reviewService.findRecentByGameId(game.getId(), 5));    //TODO don't use magic numbers
        mav.addObject("canSubmitReview", isLoggedIn() && reviewService.find(getCurrentUser().getId(), gameId) == null);
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

    @RequestMapping(value = "/reviews")
    public ModelAndView reviews(@RequestParam(name = "gameId", required = false, defaultValue = "-1") long gameId, @RequestParam(name = "userId", required = false, defaultValue = "-1") long userId) {
        //Need at least one of the two
        if(gameId == -1 && userId == -1) {
            return new ModelAndView("error400");
        }
        ModelAndView mav = null;
        try {
            mav = new ModelAndView("reviews");
            if(gameId != -1) {
                if(userId != -1) {
                    //Find by both
                    Set<Review> singleReview = new LinkedHashSet<>(1);  //Add as set because view assumes it will be a collection
                    singleReview.add(reviewService.find(userId, gameId));
                    mav.addObject("reviews", singleReview);
                    mav.addObject("user", userService.findById(userId));
                } else {
                    //Find by game ID
                    mav.addObject("reviews", reviewService.findByGameId(gameId));
                }
                //Need this in both cases to populate title - not getting the game from the reviews set as it might be empty
                mav.addObject("game", gameService.findById(gameId));
            } else {
                //Find by user ID
                mav.addObject("reviews", reviewService.findByUserId(userId));
                mav.addObject("user", userService.findById(userId));
            }
            mav.addObject("canSubmitReview", isLoggedIn() && userId == -1 && reviewService.find(getCurrentUser().getId(), gameId) == null);
        } catch (NoSuchGameException e) {
            LOG.warn("Requested reviews for nonexistent game (ID={})", gameId);
            mav = new ModelAndView("error404");
        } catch (NoSuchUserException e) {
            LOG.warn("Requested reviews for nonexistent user (ID={})", userId);
            mav = new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error rendering Reviews page", e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/write-review", method = RequestMethod.GET)
    public ModelAndView writeReview(@RequestParam(name = "id") long gameId,
                                    @ModelAttribute("reviewForm") final ReviewForm reviewForm) {
        ModelAndView mav = null;
        try {
            Game game = gameService.findById(gameId);
            if(game == null) {
                LOG.warn("Requested to write a review for a nonexistent game (ID={})", gameId);
                return new ModelAndView("error404");
            }
            //No need to check if logged in - spring security restricts access to this page to authenticated users
            if(reviewService.find(getCurrentUser().getId(), gameId) != null) {
                LOG.info("User #{} attempted to write a review for Game #{} when they already have a review, access denied", getCurrentUser().getId(), gameId);
                return new ModelAndView("error400");
            }
            mav = new ModelAndView("write-review");
            mav.addObject("game", game);
        } catch (Exception e) {
            LOG.error("Error rendering Write Review page", e);
            mav = new ModelAndView("error500");
        }
        return mav;
    }

    @RequestMapping(value = "/write-review", method = RequestMethod.POST)
    public ModelAndView submitReview(@RequestParam(name = "id") long gameId,
                                     @Valid @ModelAttribute("reviewForm") final ReviewForm reviewForm,
                                     final BindingResult errors) {
        if (errors.hasErrors()) {
            return writeReview(gameId, reviewForm);
        }
        //Valid review, create
        try {
            reviewService.create(getCurrentUser().getId(), gameId, reviewForm.getReview(), reviewForm.getStoryScore(), reviewForm.getGraphicsScore(), reviewForm.getAudioScore(), reviewForm.getControlsScore(), reviewForm.getStoryScore());
        } catch (NoSuchEntityException e) {
            LOG.warn("Attempted to create a review with an invalid user or game ID {}", e);
            return new ModelAndView("error404");
        } catch (Exception e) {
            //TODO handle users submitting duplicate reviews more gracefully
            LOG.error("Error creating review for Game #{} for User #{}", gameId, getCurrentUser().getId(), e);
            return new ModelAndView("error500");
        }
        //Created successfully, redirect to all reviews page
        return new ModelAndView("redirect:/reviews?gameId=" + gameId);
    }
}