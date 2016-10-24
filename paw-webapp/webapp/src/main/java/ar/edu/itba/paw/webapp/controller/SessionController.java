package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.LoginForm;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * This controller is in charge of handling log in requests.
 */
@Controller
public class SessionController extends BaseController{

    @Autowired
    public SessionController(UserService us) {
        super(us);
    }


    @RequestMapping(value = "/login", method = {RequestMethod.GET})
    public ModelAndView login(@ModelAttribute("loginForm") final LoginForm form, @RequestParam(value = "error", required = false) String error) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("error", error != null);
        return mav;
    }


    /*
     * Log out request is handled by Spring Security
     */
}
