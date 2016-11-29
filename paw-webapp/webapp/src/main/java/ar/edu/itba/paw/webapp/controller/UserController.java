package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.ShelfService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 * <p>
 * This controller is in charge of handling users' operations request.
 */
@Controller
public class UserController extends BaseController {


    /**
     * A game service used for listing games.
     */
    private GameService gameService;
    /**
     * A shelf service used for getting shelf information.
     */
    private final ShelfService shelfService;
    /**
     * A password encoder used for hashing passwords when creating users.
     */
    private PasswordEncoder passwordEncoder; // TODO: move this to user service [JMB]


    /**
     * A TypeReference that references to a {@code Map<{@link FilterCategory }, List<String>>}, which represents
     * filters that can be applied to game searches.
     */
    private final TypeReference<ArrayList<String>> typeReference;


    @Autowired
    public UserController(UserService us, GameService gameService, PasswordEncoder passwordEncoder, ShelfService shelfService) {
        super(us);
        this.gameService = gameService;
        this.passwordEncoder = passwordEncoder;
        typeReference = new TypeReference<ArrayList<String>>() {};
        this.shelfService = shelfService;
    }

    @RequestMapping("/profile")
    public ModelAndView profile(@RequestParam(value = "username", required = false) String username) {
        if(username == null) {
            if(isLoggedIn()) {
                return new ModelAndView("redirect:/profile?username=" + getCurrentUsername());
            } else {
                return new ModelAndView("redirect:/");
            }
        }
        User user = userService.findByUsername(username);
        if(user == null) {
            return new ModelAndView("redirect:error400");
        }
        //Safe to render Profile page
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", user);
        Map<PlayStatus, Set<Game>> gameList = userService.getGameList(user.getId());
        mav.addObject("playedGames", gameList.get(PlayStatus.PLAYED));
        mav.addObject("playingGames", gameList.get(PlayStatus.PLAYING));
        mav.addObject("planToPlayGames", gameList.get(PlayStatus.PLAN_TO_PLAY));

        //Add up to 10 games in descending rank order
        Map<Game, Integer> topGames = new LinkedHashMap<>();
        Map<Integer, Set<Long>> reverseScoredGames = userService.getScoredGamesRev(user.getId());
        for (int score = 10; score > 0 && topGames.size() < 10; score--) {
            if(reverseScoredGames.containsKey(score)) {
                for(long gameId : reverseScoredGames.get(score)) {
                    topGames.put(gameService.findById(gameId), score);
                    if(topGames.size() >= 10) {
                        break;
                    }
                }
            }
        }
        mav.addObject("topGames", topGames);

        return mav;
    }

    @RequestMapping(value = "/list")
    public ModelAndView list(@RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "playStatusesCheckbox", required = false) String[] playStatusesCheckboxStr,
                             @RequestParam(value = "shelvesCheckbox", required = false) String[] shelvesCheckboxStr) {
        if (username == null) {
            if (!isLoggedIn()) {
                return new ModelAndView("redirect:error400");
            }
            return list(getCurrentUsername(),playStatusesCheckboxStr,shelvesCheckboxStr);
        }
        final ModelAndView mav = new ModelAndView("shelves");
        User user = userService.findByUsername(username);
        if (user == null) return new ModelAndView("error400");

        Set<String> shelvesFilter = new HashSet<>();
        if(shelvesCheckboxStr!=null){
            for(String s: shelvesCheckboxStr){
                shelvesFilter.add(s);
            }
        }

        Set<String> playStatusesFilter = new HashSet<>();
        if(playStatusesCheckboxStr!=null){
            for(String s: playStatusesCheckboxStr){
                playStatusesFilter.add(s);
            }
        }


        Map<Game, Set<Shelf>> shelvesForGames = new HashMap();
        Map<Game, PlayStatus> playStatuses = new HashMap<>();


        for (Map.Entry<PlayStatus, Set<Game>> entry : userService.getGameList(user.getId()).entrySet()) {
            // TODO use other set and give it order? ScoreOrder? (If treeSet is used, danger of eliminating games)
            PlayStatus status = entry.getKey();
            Set<Game> games = entry.getValue();
            for(Game game : games) {
                if(!shelvesForGames.containsKey(game)){
                    shelvesForGames.put(game,new HashSet<>());
                }
                if(!playStatuses.containsKey(game)) {
                    playStatuses.put(game, status);
                }
            }
        }
        Set<Shelf> shelves = shelfService.findByUserId(user.getId());
        for(Shelf shelf : shelves) {
            for(Game game : shelf.getGames()) {
                if(shelvesForGames.containsKey(game)){
                    shelvesForGames.get(game).add(shelf);
                }
            }
        }
        //scores
        Map<Game, Integer> scores = userService.getScoredGames(user.getId());


        Set<Game> games = new HashSet<>();

        for(Game game: playStatuses.keySet()){
            boolean validGame = false;
            if(playStatusesFilter.isEmpty()) validGame =true;
            for(String playStatusFilter: playStatusesFilter){
                if(playStatuses.get(game).name().equals(playStatusFilter)){
                    validGame = true;
                }
            }
            for(String shelfFilter: shelvesFilter){
                for(Shelf shelf: shelvesForGames.get(game)){
                    if(!shelf.getName().equals(shelfFilter)){
                        validGame = false;
                    }
                }
            }
            if(validGame) games.add(game);
        }



        mav.addObject("playstatus",PlayStatus.values());
        mav.addObject("playStatusesFilter",playStatusesFilter);
        mav.addObject("shelvesFilter",shelvesFilter);
        mav.addObject("games",games);
        mav.addObject("user", user);
        mav.addObject("scores",scores);
        mav.addObject("shelves", shelves);
        mav.addObject("shelvesForGamesMap",shelvesForGames);
        mav.addObject("playStatuses", playStatuses);
        return mav;
    }

    @RequestMapping(value = "/remove-from-list", method = RequestMethod.POST)
    public ModelAndView removeFromList(@RequestParam(value = "gameId") long gameId,
                                       @RequestParam(value = "userId") long userId,
                                       @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        if (!isLoggedIn() || userId!= getCurrentUser().getId()) {
            return new ModelAndView("redirect:error400");
        }
        User user = getCurrentUser();
        if (user == null) return new ModelAndView("error400");

        if(!gameService.existsWithId(gameId))return new ModelAndView("error400");

        userService.removeScore(userId,gameId);
        userService.removeStatus(userId,gameId);
        for(Shelf shelf: shelfService.findByUserId(userId)){
            shelfService.removeGame(shelf.getId(),gameId);
        }


//        shelfService.findByGameId();
//        shelfService.removeGame();

        return list(null,null,null);
    }




    @RequestMapping("/register")
    public ModelAndView registerGet(@ModelAttribute("registerForm") final UserForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = {RequestMethod.POST})
    public ModelAndView registerPost(@Valid @ModelAttribute("registerForm") final UserForm form,
                                     final BindingResult errors,
                                     final RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return registerGet(form);
        }
        final String email = form.getEmail(),
                // TODO: Move encryption to UserService [JMB]
                hashedPassword = passwordEncoder.encode(form.getPassword()),
                username = form.getUsername();

        User user;
        try {
            user = userService.create(email, hashedPassword, username);
        } catch (UserExistsException e) {
            LOG.warn("Registration form validated but UserExists exception still thrown during registration of {} / {}: {}", username, email, e);
            return registerGet(form);
        }
        LOG.info("Registered user {} with email {}, logging them in and redirecting to home", user.getUsername(), user.getEmail());

        //Log the new user in
        Authentication auth = new UsernamePasswordAuthenticationToken(username, hashedPassword);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ModelAndView("redirect:/");
    }
}
