package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.MailService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 * <p>
 * This controller is in charge of handling requests to the home page.
 */
@Controller
public class HomeController extends BaseController {

    @Autowired
    public HomeController(UserService us) {
        super(us);
    }


    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
        Collection<Game> recommendedGames = new LinkedHashSet<>();
        if (isLoggedIn()) {
            recommendedGames = userService.recommendGames(getCurrentUser().getId());
        }
        mav.addObject("recommendedGames", recommendedGames);
        return mav;
    }

}
