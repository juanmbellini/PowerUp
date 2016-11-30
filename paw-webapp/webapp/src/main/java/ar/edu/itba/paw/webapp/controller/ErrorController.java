package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 * <p>
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
        return new ModelAndView("error500");
    }

    @RequestMapping("/error404")
    public ModelAndView error404() {
        return new ModelAndView("error404");
    }

    @RequestMapping("/error405")
    public ModelAndView error405() {
        return new ModelAndView("error405");
    }

    @RequestMapping("/error400")
    public ModelAndView error400() {
        return new ModelAndView("error400");
    }

}
