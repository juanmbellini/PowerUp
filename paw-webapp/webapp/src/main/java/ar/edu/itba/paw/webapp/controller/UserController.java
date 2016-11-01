package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional  //TODO exterminate exterminate exterminate exterminate exterminate exterminate exterminate exterminate exterminate exterminate exterminate exterminate
public class UserController extends BaseController {


    /**
     * A game service used for listing games.
     */
    private GameService gameService;
    /**
     * A password encoder used for hashing passwords when creating users.
     */
    private PasswordEncoder passwordEncoder; // TODO: move this to user service [JMB]


    @Autowired
    public UserController(UserService us, GameService gameService, PasswordEncoder passwordEncoder) {
        super(us);
        this.gameService = gameService;
        this.passwordEncoder = passwordEncoder;
    }


    @RequestMapping("/list")
    public ModelAndView list(@RequestParam(value = "username", required = false) String username) {

        // TODO: Check if we are really allowing anyone to check other's lists. [JMB]
        if (username == null) {
            if (!isLoggedIn()) {
                return new ModelAndView("redirect:error400");
            }
            return new ModelAndView("redirect:/list?username=" + getCurrentUsername());
        }
        final ModelAndView mav = new ModelAndView("list");
        User u = userService.findByUsername(username);
        if (u == null) return new ModelAndView("error400");

        //User found, populate their list
        Map<PlayStatus, Map<Game, Integer>> gameList = new HashMap<>();
        Map<Game, Integer> scores = userService.getScoredGames(u);
        for (PlayStatus playStatus : PlayStatus.values()) {
            // TODO use other set and give it order? ScoreOrder? (If treeSet is used, danger of eliminating games)
            Map<Game, Integer> gameCategory = new LinkedHashMap<>();
            for(Game game : userService.getGamesByStatus(u, playStatus)) {
                gameCategory.put(game, scores.containsKey(game.getId()) ? scores.get(game.getId()) : -1);
            }
            gameList.put(playStatus, gameCategory);
        }
        mav.addObject("user", u);
        mav.addObject("gameList", gameList);

        return mav;
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
            redirectAttributes.addFlashAttribute("registerForm", form);
            return new ModelAndView("redirect:/register");
        }
        final String email = form.getEmail(),
                // TODO: Move encryption to UserService [JMB]
                hashedPassword = passwordEncoder.encode(form.getPassword()),
                username = form.getUsername();

        User user;
        try {
            user = userService.create(email, hashedPassword, username);
        } catch (UserExistsException e) {
            String msgLow = e.getMessage().toLowerCase();
            // The calls to rejectValue will add errors to the UserForm so they get displayed properly and in the proper fields
            if (msgLow.contains("email")) {
                errors.rejectValue("email", "error.email", "An account exists with this email");
            } else if (msgLow.contains("username")) {
                errors.rejectValue("username", "error.username", "Username already taken");
            } else {
                errors.addError(new ObjectError("error", "Error creating account, please try again"));
                System.err.println("Unrecognized message in UserExistsException: \"" + e.getMessage() + "\".");
                System.err.println("Printing stack trace:");
                e.printStackTrace();
            }
            redirectAttributes.addFlashAttribute("registerForm", form);
            return new ModelAndView("redirect:/register");
        }
        // TODO: Remove this println [JMB]
        System.out.println("Registered user " + user.getUsername() + " with email " + user.getEmail() + ", logging them in and redirecting to home");

        //Log the new user in
        Authentication auth = new UsernamePasswordAuthenticationToken(username, hashedPassword);
        SecurityContextHolder.getContext().setAuthentication(auth);
        return new ModelAndView("redirect:/");
    }


}
