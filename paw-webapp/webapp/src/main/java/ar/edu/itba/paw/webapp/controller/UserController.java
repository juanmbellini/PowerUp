package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.MailService;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

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
     * A mail service used for reseting the user's password.
     */
    private MailService mailService;
    /**
     * A password encoder used for hashing passwords when creating users.
     */
    private PasswordEncoder passwordEncoder; // TODO: move this to user service [JMB]


    @Autowired
    public UserController(UserService us, GameService gameService, PasswordEncoder passwordEncoder, MailService mailService) {
        super(us);
        this.mailService=mailService;
        this.gameService = gameService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/profile")
    public ModelAndView profile(@RequestParam(value = "username", required = false) String username,
                                @ModelAttribute("changePasswordForm") final ChangePasswordForm form,
                                final BindingResult errors) {
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
        mav.addObject("formHasErrors", errors.hasErrors());
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
        Map<PlayStatus, Map<Game, Integer>> gameListWithScores = new HashMap<>();
        Map<Game, Integer> scores = userService.getScoredGames(u.getId());
        for (Map.Entry<PlayStatus, Set<Game>> entry : userService.getGameList(u.getId()).entrySet()) {
            // TODO use other set and give it order? ScoreOrder? (If treeSet is used, danger of eliminating games)
            PlayStatus status = entry.getKey();
            Set<Game> games = entry.getValue();
            if(!gameListWithScores.containsKey(status)) {
                gameListWithScores.put(status, new LinkedHashMap<>());
            }
            Map<Game, Integer> gameCategory = gameListWithScores.get(status);
            for(Game game : games) {
                gameCategory.put(game, scores.containsKey(game) ? scores.get(game) : -1);
            }
            gameListWithScores.put(status, gameCategory);
        }
        mav.addObject("user", u);
        mav.addObject("gameList", gameListWithScores);
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

    @RequestMapping(value = "/change-password", method = {RequestMethod.POST})
    public ModelAndView changePassword(@Valid @ModelAttribute("changePasswordForm") final ChangePasswordForm form,
                                       final BindingResult errors,
                                        @RequestParam (value = "username") final String username) {

        if (errors.hasErrors()) {
            return profile(username, form, errors);
        }

        User user = getCurrentUser();
        String hashedOldPassword = passwordEncoder.encode(form.getOldPassword());
        String hashedNewPassword = passwordEncoder.encode(form.getNewPassword());
        if(hashedOldPassword.equals(user.getHashedPassword())){
            userService.changePassword(user.getId(),hashedNewPassword);
        }else{
            LOG.warn("old password did not match");
        }

        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(value = "/reset-password", method = {RequestMethod.POST})
    public ModelAndView resetPassword(@RequestParam(name = "email") final String email) {

        //hace un findByEmail, si lo encontraas resetea, listo

        User user;
        try {
            user = userService.findByEmail(email);
        } catch (NoSuchEntityException e) {
            LOG.warn("No user found associated to that email");
            return new ModelAndView("redirect:/");
        }
        String password = userService.generateNewPassword();
        String hashedPassword = passwordEncoder.encode(password);
        userService.changePassword(user.getId(), hashedPassword);
        mailService.sendEmailChangePassword(user,password);
        LOG.info("Password has been reseted. Your new password has been sent to your email.");

        return new ModelAndView("redirect:/");
    }
}
