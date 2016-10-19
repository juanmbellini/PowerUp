package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * This controller is in charge of handling errors.
 */
@Controller
public class ErrorController extends BaseController {


    @Autowired
    public ErrorController(UserService userService) {
        super(userService);
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
