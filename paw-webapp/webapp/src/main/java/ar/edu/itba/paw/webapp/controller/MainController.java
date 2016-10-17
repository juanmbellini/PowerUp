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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import sun.plugin.dom.exception.InvalidStateException;
import sun.plugin.javascript.navig.Array;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
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

    @Autowired
    public MainController(GameService gameService, UserService userService, PasswordEncoder passwordEncoder) {
        //Spring is in charge of providing the gameService parameter.
        this.userService = userService;
        this.gameService = gameService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
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
        } else if (orderParameter.equals("avg-score")) {
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
            mav.setViewName("search");
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
            if(u != null) {
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
        for(PlayStatus status : PlayStatus.values()) {
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
     * @return Whether a user is currently authenticated with Spring.
     */
    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn() {
        String username = getCurrentUsername();
        return !(username == null || username.contains("anonymous"));
    }

    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "username", required = false) String username) {
        if(username == null) {
            if(!isLoggedIn()) {
                return error400();
            }
            return new ModelAndView("redirect:/list?username=" + getCurrentUsername());
        }
        final ModelAndView mav = new ModelAndView("list");
        User u = userService.findByUsername(username);      //If we got this far, we know username != null
        if(u == null) return error400();

        Map<PlayStatus, Set<Game>> playedGames = new HashMap<>();   //TODO change name of playedGames
        for(PlayStatus playStatus : PlayStatus.values()){
            playedGames.put(playStatus, new HashSet<>());           //TODO use other set and give it order?
        }
        Map<Long, PlayStatus> playStatuses =  u.getPlayStatuses();
        //TODO do this in user?
        for(long gameId: playStatuses.keySet()){
            Game game = gameService.findById(gameId);
            if(game==null) throw new InvalidStateException("Status list should have a game that do not exist");
            playedGames.get(playStatuses.get(gameId)).add(game);
        }
        mav.addObject("user", u);
        mav.addObject("playStatuses", playedGames);

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
        if(u == null) {
            return new ModelAndView("redirect:/");  //TODO return HTTP 401; configure web auth to only give access to authenticated users
        }

        Integer score = rateAndStatusForm.getScore();
        if (score != null) userService.scoreGame(u, id, score);
        else; //TODO delete score from userMap

        PlayStatus playStatus = rateAndStatusForm.getPlayStatus();
        if (playStatus != null) userService.setPlayStatus(u, id, playStatus);
        else;//TODO delete status from userMap

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
        //TODO handle duplicate emails/usernames here
        final String email = form.getEmail(),
                    hashedPassword = passwordEncoder.encode(form.getPassword()),
                    username = form.getUsername();
        final User user = userService.create(email, hashedPassword, username);
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
}