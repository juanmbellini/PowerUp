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

    @RequestMapping("/search")
    public ModelAndView search() {
        //TODO actually perform a search here and add the results to the MAV
        final ModelAndView mav = new ModelAndView("search");
        mav.addObject("greeting", "PAW");
        return mav;
    }



    @RequestMapping("/gameSearch")
    public ModelAndView searchGameByName(@RequestParam("name") String name,
                                         @RequestParam(value="genre", required = false) String filterGenre,
                                         @RequestParam(value = "publisher", required = false) String filterPublisher

                                        ){
        final ModelAndView mav = new ModelAndView("gameSearch");
        HashSet<Filter> filters = new HashSet<Filter>();
        if(filterGenre!=null) filters.add(new Filter(Filter.FilterCategory.GENRES,filterGenre));
        if(filterPublisher!=null) filters.add(new Filter(Filter.FilterCategory.PUBLISHERS,filterPublisher));
       Collection<Game> searchedGame = gameService.searchGame(name, filters);
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