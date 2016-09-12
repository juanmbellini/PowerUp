package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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

    @RequestMapping("/results")
    public ModelAndView results() {
        final ModelAndView mav = new ModelAndView("results");
        mav.addObject("greeting", "PAW");
        return mav;
    }

    @RequestMapping("/search")
    public ModelAndView search() {
        final ModelAndView mav = new ModelAndView("search");
        mav.addObject("greeting", "PAW");
        return mav;
    }



    @RequestMapping("/gameSearch")
    public ModelAndView searchGameByName(@RequestParam("name") String name) {
        final ModelAndView mav = new ModelAndView("gameSearch");
       Collection<Game> searchedGame = gameService.searchGame(name, new HashSet<Filter>() {
       });
       mav.addObject("gameList", searchedGame);
        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game() {
        final ModelAndView mav = new ModelAndView("game");
        mav.addObject("greeting", "PAW");
        return mav;
    }
}