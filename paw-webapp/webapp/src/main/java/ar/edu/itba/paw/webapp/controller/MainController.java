package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashSet;
import java.util.List;

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
    public ModelAndView searchGameByName(@RequestParam("name") String name,
                                         @RequestParam(value="genre", required = false) String filterGenre,
                                         @RequestParam(value = "publisher", required = false) String filterPublisher) {
        final ModelAndView mav = new ModelAndView("search");
        HashSet<Filter> filters = new HashSet<Filter>();
        if (filterGenre != null) filters.add(new Filter(Filter.FilterCategory.GENRES, filterGenre));
        if (filterPublisher != null) filters.add(new Filter(Filter.FilterCategory.PUBLISHERS, filterPublisher));
        List<Game> results = gameService.searchGames(name, filters);
        mav.addObject("searchedName", name);
        mav.addObject("hasFilters", filterGenre != null || filterPublisher != null);
        mav.addObject("results", results);
        return mav;
    }

    @RequestMapping("/advanced-search")
    public ModelAndView advancedSearch() {
        final ModelAndView mav = new ModelAndView("advanced-search");
        //TODO add possible filters here
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