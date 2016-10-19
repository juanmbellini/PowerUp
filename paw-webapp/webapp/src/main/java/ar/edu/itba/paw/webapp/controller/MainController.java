package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.form.RateAndStatusForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@Controller
public class MainController {

    private static final int DEFAULT_PAGE_SIZE = 25;
    public static final int DEFAULT_PAGE_NUMBER = 1;

    private final GameService gameService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static TypeReference<HashMap<FilterCategory, ArrayList<String>>> typeReference
            = new TypeReference<HashMap<FilterCategory, ArrayList<String>>>() {
    };

    final private UrlCreator urlCreator;

    @Autowired
    public MainController(GameService gameService, UserService userService, PasswordEncoder passwordEncoder) {
        //Spring is in charge of providing the gameService parameter.
        this.userService = userService;
        this.gameService = gameService;
        this.passwordEncoder = passwordEncoder;
        urlCreator = new UrlCreator();
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "filters", required = false) String filtersStr,
                               @RequestParam(value = "orderCategory", required = false) String orderParameter,
                               @RequestParam(value = "orderBoolean", required = false) String orderBooleanStr,
                               @RequestParam(value = "pageSize", required = false) String pageSizeStr,
                               @RequestParam(value = "pageNumber", required = false) String pageNumberStr) {

        final ModelAndView mav = new ModelAndView();

        // This is done here in order to get parameters without further modification
        String changePageUrl = urlCreator.getSearchUrl(name, filtersStr, orderParameter, orderBooleanStr,
                pageSizeStr, null);
        String changeOrderUrl = urlCreator.getSearchUrl(name, filtersStr, null, null, pageSizeStr, null);
        String changePageSizeUrl = urlCreator.getSearchUrl(name, filtersStr, orderParameter,
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

            pageSize = (pageSizeStr == null || pageSizeStr.equals("")) ? DEFAULT_PAGE_SIZE : new Integer(pageSizeStr);
            pageNumber = (pageNumberStr == null || pageNumberStr.equals("")) ?
                    DEFAULT_PAGE_NUMBER : new Integer(pageNumberStr);

            Page<Game> page = gameService.searchGames(name, filters, OrderCategory.valueOf(orderCategory),
                    orderBoolean, pageSize, pageNumber);

            mav.addObject("page", page);
            mav.addObject("hasFilters", !filtersStr.equals("{}")); // TODO: Check the applied filters size
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
                             @RequestParam(name = "id") long id) {
        final ModelAndView mav = new ModelAndView("game");
        Game game;
        Set<Game> relatedGames;
        try {
            game = gameService.findById(id);
            if (game == null) {
                return error404();
            }
            User u = getCurrentUser();
            if (u != null) {
                if (u.hasScoredGame(id)) rateAndStatusForm.setScore(u.getGameScore(id));
                if (u.hasPlayStatus(id)) rateAndStatusForm.setPlayStatus(u.getPlayStatus(id));
            }


            Set<FilterCategory> filters = new HashSet<>();
            filters.add(FilterCategory.platform);
            filters.add(FilterCategory.genre);
            relatedGames = gameService.findRelatedGames(game, filters);
        } catch (Exception e) {
            return error500();
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
        mav.addObject("relatedGames", relatedGames);
        return mav;
    }

    /* *****************************************************************************************************************
    *                                               USERS/SESSIONS
    * *****************************************************************************************************************/

    //TODO move current-user functions to UserService?

    /**
     * Gets the current user. <b>NOTE: </b>To check whether a user is currently logged in, use the less costly (and more
     * obvious) {@link #isLoggedIn()} method.
     *
     * @return The currently authenticated user, or {@code null} if none.
     */
    @ModelAttribute("currentUser")
    public User getCurrentUser() {
        String username = getCurrentUsername();
        return (username == null || username.contains("anonymous")) ? null : userService.findByUsername(username);
    }

    /**
     * Gets the currently authenticated user's username.
     *
     * @return The currently authenticated user's username.
     */
    @ModelAttribute("currentUsername")
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext() == null ? null : SecurityContextHolder.getContext().getAuthentication() == null ? null : SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Checks whether there is a currently authenticated user.
     *
     * @return Whether a user is currently authenticated with Spring.
     */
    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        String username = getCurrentUsername();
        return !(username == null || username.contains("anonymous"));
    }

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "username", required = false) String username) {
        if (username == null) {
            if (!isLoggedIn()) {
                return error400();
            }
            return new ModelAndView("redirect:/list?username=" + getCurrentUsername());
        }
        final ModelAndView mav = new ModelAndView("list");
        User u = userService.findByUsername(username);      //If we got this far, we know username != null
        if (u == null) return error400();

        Map<PlayStatus, Set<Game>> gamesInListsMap = new HashMap<>();
        for (PlayStatus playStatus : PlayStatus.values()) {
            gamesInListsMap.put(playStatus, new HashSet<>());           //TODO use other set and give it order? ScoreOrder? (If treeSet is used, danger of eliminating games)
        }
        Map<Long, PlayStatus> playStatuses = u.getPlayStatuses();
        //TODO do this in user?
        Map<Long,Game> longGameMap = gameService.findBasicDataGamesFromArrayId( playStatuses.keySet());
        for(long gameId: playStatuses.keySet()){
            Game game = longGameMap.get(gameId);
            if(game==null) throw new IllegalStateException("Status list should have a game that do not exist");
            gamesInListsMap.get(playStatuses.get(gameId)).add(game);
        }
        mav.addObject("user", u);
        mav.addObject("playStatuses", gamesInListsMap);

        return mav;
    }

    @RequestMapping(value = "/rateAndUpdateStatus", method = {RequestMethod.POST})
    public ModelAndView rateAndUpdateStatus(@Valid @ModelAttribute("rateAndStatusForm") final RateAndStatusForm rateAndStatusForm,
                                            final BindingResult errors,
                                            @RequestParam(name = "id") long id) {
        if (errors.hasErrors()) {
            return game(rateAndStatusForm, id);
        }
        final User u = getCurrentUser();
        if (u == null) {     //This should never happen; Spring only gives access to this page to authenticated users
            return new ModelAndView("redirect:/login");
        }

        Integer score = rateAndStatusForm.getScore();
        if (score != null) userService.scoreGame(u, id, score);
        else userService.removeScore(u,id);

        PlayStatus playStatus = rateAndStatusForm.getPlayStatus();
        if (playStatus != null) userService.setPlayStatus(u, id, playStatus);
        else userService.removeStatus(u,id);

        return new ModelAndView("redirect:/game?id=" + id);
    }

    @RequestMapping("/register")
    public ModelAndView register(@ModelAttribute("registerForm") final UserForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm form, final BindingResult errors) {
        if (errors.hasErrors()) {
            return register(form);
        }
        final String email = form.getEmail(),
                    hashedPassword = passwordEncoder.encode(form.getPassword()),
                    username = form.getUsername();
        User user;
        try {
            user = userService.create(email, hashedPassword, username);
        } catch (UserExistsException e) {
            String msgLow = e.getMessage().toLowerCase();
            //The calls to rejectValue will add errors to the UserForm so they get displayed properly and in the proper fields
            if(msgLow.contains("email")) {
                errors.rejectValue("email", "error.email", "An account exists with this email");
            } else if(msgLow.contains("username")) {
                errors.rejectValue("username", "error.username", "Username already taken");
            } else {
                errors.addError(new ObjectError("error", "Error creating account, please try again"));
                System.err.println("Unrecognized message in UserExistsException: \"" + e.getMessage() + "\". Printing stack trace:");
                e.printStackTrace();
            }
            return register(form);
        }
        System.out.println("Registered user " + user.getUsername() + " with email " + user.getEmail() + ", logging them in and redirecting to home");
        //Log the new user in
        Authentication auth = new UsernamePasswordAuthenticationToken(username, hashedPassword);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/login", method = {RequestMethod.GET})
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




    /* *****************************************************************************************************************
    *                                                   ERRORS
    * *****************************************************************************************************************/
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


    // TODO: Move this when adding support for multiple controllers

    /**
     * Helper class that creates different types of complex URLs to be sent to the view
     */
    private static class UrlCreator {

        /**
         * Creates an URL for the '/search' page.
         *
         * @param name The value for the 'name' query string param.
         * @param filters The value for the 'filters' query string param.
         * @param orderCategory The value for the 'orderCategory' query string param.
         * @param orderBoolean The value for the 'orderBoolean' query string param.
         * @param pageSize The value for the 'pageSize' query string param.
         * @param pageNumber The value for the 'pageNumber' query string param.
         * @return
         */
        public String getSearchUrl(String name, String filters, String orderCategory, String orderBoolean, String pageSize, String pageNumber) {
            Map<String, String> params = new LinkedHashMap<>();
            params.put("name", name);
            params.put("filters", filters);
            params.put("orderCategory", orderCategory);
            params.put("orderBoolean", orderBoolean);
            params.put("pageSize", pageSize);
            params.put("pageNumber", pageNumber);
            return createUrl("/search", params);
        }


        /**
         * Method to create an URL given a {@code baseUrl} and a map of query string parameters - values.
         *
         * @param baseUrl The base URL.
         * @param params A map containing as keys the name of the query string parameters,
         *               and values the corresponding values for those parameters.
         * @return The complex URL.
         */
        private String createUrl(String baseUrl, Map<String, String> params) {
            return baseUrl + createUrl(params, params.keySet().iterator(), true);
        }

        /**
         * Recursive method to create a query string, given a map of parameters - values.
         *
         * @param params The map of parameters - values.
         * @param keysIterator The {@code params#keySet} iterator.
         * @param questionMark A flag that says if the question mark (or the ampersand) must be placed.
         * @return
         */
        private String createUrl(Map<String, String> params, Iterator<String> keysIterator, boolean questionMark) {
            if (!keysIterator.hasNext()) {
                return "";
            }
            String param = keysIterator.next();
            String value = params.get(param);
            String symbol = questionMark ? "?" : "&";
            String result = "";
            if (value != null && !value.equals("")) {
                try {
                    result = symbol + param + "=" + URLEncoder.encode(value, "UTF-8");
                    questionMark = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return result + createUrl(params, keysIterator, questionMark);
        }

    }
}