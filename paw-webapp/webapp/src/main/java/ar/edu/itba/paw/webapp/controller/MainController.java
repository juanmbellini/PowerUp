package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.RateAndStatusForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.utilities.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import netscape.javascript.JSException;
import org.atteo.evo.inflector.English;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import sun.plugin.dom.exception.InvalidStateException;
import sun.plugin.javascript.navig.Array;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class MainController {

    private static final int DEFAULT_PAGE_SIZE = 25;
    public static final int DEFAULT_PAGE_NUMBER = 1;

    private final GameService gameService;
    private final UserService userService;

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static TypeReference<HashMap<FilterCategory, ArrayList<String>>> typeReference
            = new TypeReference<HashMap<FilterCategory, ArrayList<String>>>() {
    };

    @Autowired
    public MainController(GameService gameService, UserService userService) {
        //Spring is in charge of providing the gameService parameter.
        this.userService = userService;
        this.gameService = gameService;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", "PAW");
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "orderCategory", required = false) String orderParameter,
                               @RequestParam(value = "orderBoolean", required = false) String orderBooleanStr,
                               @RequestParam(value = "filters", required = false) String filtersStr,
                               @RequestParam(value = "pageSize", required = false) String pageSizeStr,
                               @RequestParam(value = "pageNumber", required = false) String pageNumberStr) {

        final ModelAndView mav = new ModelAndView();

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
        } else if (orderParameter.equals("release date")) {
            orderCategory = "release";
        } else if (orderParameter.equals("avg-rating")) {
            orderCategory = "avg_score";
        } else {
            mav.setViewName("redirect:error400");
            return mav;
        }

        if (orderBooleanStr == null || orderBooleanStr.equals("ascending")) {
            orderBoolean = true;
        } else if (orderBooleanStr.equals("descending")) {
            orderBoolean = false;
        } else {
            mav.setViewName("redirect:error400");
            return mav;
        }

        Map<FilterCategory, List<String>> filters;
        try {
            filters = objectMapper.readValue(filtersStr, typeReference);

            // TODO: In case an exception is thrown in this two next lines, should be redirect to 400 error page, or should be set default values?
            pageSize = (pageSizeStr == null || pageSizeStr.equals("")) ? DEFAULT_PAGE_SIZE : new Integer(pageSizeStr);
            pageNumber = (pageNumberStr == null || pageNumberStr.equals("")) ?
                    DEFAULT_PAGE_NUMBER : new Integer(pageNumberStr);

            Page<Game> page = gameService.searchGames(name, filters, OrderCategory.valueOf(orderCategory),
                    orderBoolean, pageSize, pageNumber);
            // TODO: Change JSP in order to send just the page
            mav.addObject("results", page.getData());
            mav.addObject("pageNumber", page.getPageNumber());
            mav.addObject("pageSize", page.getPageSize());
            mav.addObject("totalPages", page.getTotalPages());

            mav.addObject("hasFilters", !filtersStr.equals("{}"));
            mav.addObject("appliedFilters", filters);
            mav.addObject("searchedName", HtmlUtils.htmlEscape(name));
            mav.addObject("orderBoolean", orderBooleanStr);
            mav.addObject("orderCategory", orderParameter);
            mav.addObject("filters", filtersStr);
            mav.setViewName("search");

        } catch (IOException | NumberFormatException | IllegalPageException e) {
            e.printStackTrace();  // Wrong filtersJson, pageSizeStr or pageNumberStr, or pageNumber strings
            mav.setViewName("redirect:error400");
        }
        return mav;
    }

    @RequestMapping("/advanced-search")
    public ModelAndView advancedSearch() {
        final ModelAndView mav = new ModelAndView("advanced-search");
        //Add all possible filter types
        for (FilterCategory filterCategory : FilterCategory.values()) {
            try {
                mav.addObject(English.plural(filterCategory.name()).toUpperCase(),
                        gameService.getFiltersByType(filterCategory));
            } catch (Exception e) {
                return error500();
            }
        }
        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game(@ModelAttribute("rateAndStatusForm") final RateAndStatusForm rateAndStatusForm,
                             @RequestParam(name = "id") int id) {
        final ModelAndView mav = new ModelAndView("game");
        Game game;
        Set<Game> relatedGames;
        try {
            game = gameService.findById(id);
            if (game == null) {
                return error404();
            }
            User currentUser = userService.findById(1);
            //TODO change user to current user
            if (currentUser.hasScoredGame(id)) rateAndStatusForm.setScore(currentUser.getGameScore(id));
            if (currentUser.hasPlayStatus(id)) rateAndStatusForm.setPlayStatus(currentUser.getPlayStatus(id));

            Set<FilterCategory> filters = new HashSet<>();
            filters.add(FilterCategory.platform);
            filters.add(FilterCategory.genre);
            relatedGames = gameService.findRelatedGames(game, filters);
        } catch (Exception e) {
            return error500();
        }
        ArrayList scoreValues = new ArrayList();
        for (int i = 1; i <= 10; i++) scoreValues.add(i);
        mav.addObject("scoreValues", scoreValues);
        mav.addObject("statuses", PlayStatus.values());
        mav.addObject("game", game);
        mav.addObject("relatedGames", relatedGames);
        return mav;
    }

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "userName", required = false) String userName) {
        if(userName==null){
            User currentUser = userService.findById(1);
            if(currentUser==null) return error400();
            //TODO use true current user
            userName=currentUser.getUsername();
            return new ModelAndView("redirect:/list?userName="+userName);
        }
        final ModelAndView mav = new ModelAndView("list");
        //TODO if no username is provided: if logged in, redirect with logged-in username; else, 404 or something


        User u = userService.findByUsername(userName);
        if(u==null) return error400();

        Map<PlayStatus, Set<Game>> playedGames = new HashMap<>(); //TODO change name of playedGames
        for(PlayStatus playStatus : PlayStatus.values()){
            playedGames.put(playStatus, new HashSet<Game>()); //TODO user other set and give it order?
        }
        Map<Long, PlayStatus> playStatuses =  u.getPlayStatuses();
        //Todo, do this in user?
        for(long gameId: playStatuses.keySet()){
            Game game = gameService.findById(gameId);
            if(game==null) throw new InvalidStateException("Status list should have a game that do not exist");
            playedGames.get(playStatuses.get(gameId)).add(game);
        }
        mav.addObject("user",u);
        mav.addObject("playStatuses", playedGames);

        return mav;
    }

    @RequestMapping(value = "/rateAndUpdateStatus", method = {RequestMethod.POST})
    public ModelAndView rateAndUpdateStatus(@Valid @ModelAttribute("rateAndStatusForm") final RateAndStatusForm rateAndStatusForm,
                                            final BindingResult errors,
                                            @RequestParam(name = "id") int id) {
        if (errors.hasErrors()) {
            return game(rateAndStatusForm, id);
        }
        //TODO change user to current user
        final User u = userService.findById(1);

        Integer score = rateAndStatusForm.getScore();
        if (score != null) userService.scoreGame(u, id, score);
        else; //TODO delete score from userMap

        PlayStatus playStatus = rateAndStatusForm.getPlayStatus();
        if (playStatus != null) userService.setPlayStatus(u, id, playStatus);
        else;//TODO delete status from userMap

        return new ModelAndView("redirect:/game?id=" + id);
    }

    @RequestMapping("/register")//TODO
    public ModelAndView register(@ModelAttribute("registerForm") final UserForm form) {
            return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        final User u = userService.create(form.getEmail(), form.getUsername(), form.getPassword());
        //TODO redirect to user page
        return new ModelAndView("redirect:/?userId="+ u.getId());
    }

    @RequestMapping("/login")
    public ModelAndView login(@ModelAttribute("loginForm") final LoginForm form) {
        return new ModelAndView("login");
    }

    @RequestMapping("/recommend")
    public ModelAndView recommend() {
        final ModelAndView mav = new ModelAndView("recommend");
        User u = userService.findById(1);//TODO user currentUser
        if(u==null) return error400(); //TODO avisar que no esta logueado
        Collection<Game> recommendedGames = userService.recommendGames(u);
        mav.addObject("recommendedGames", recommendedGames);
        return mav;
    }




    @RequestMapping("/error500")
    public ModelAndView error500() {
        final ModelAndView mav = new ModelAndView("error500");
        return mav;
    }

    @RequestMapping("/error404")
    public ModelAndView error404() {
        final ModelAndView mav = new ModelAndView("error404");
        return mav;
    }

    @RequestMapping("/error400")
    public ModelAndView error400() {
        final ModelAndView mav = new ModelAndView("error400");
        return mav;
    }
}