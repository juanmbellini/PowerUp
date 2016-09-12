package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    private final GameService gameService;

    @Autowired
    public MainController(GameService gameService) {
        //Spring is in charge of providing the gameService parameter.
        this.gameService = gameService;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", "PAW");
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search() {
        //TODO actually perform a search here and add the results to the MAV
        final ModelAndView mav = new ModelAndView("search");
        mav.addObject("greeting", "PAW");
        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game() {
        final ModelAndView mav = new ModelAndView("game");
        mav.addObject("greeting", "PAW");
        return mav;
    }
}