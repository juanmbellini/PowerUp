package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.*;

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

//    @RequestMapping("/results")
//    public ModelAndView results() {
//        final ModelAndView mav = new ModelAndView("results");
//        mav.addObject("greeting", "PAW");
//        return mav;
//    }
//
//    @RequestMapping("/search")
//    public ModelAndView search() {
//        final ModelAndView mav = new ModelAndView("search");
//        mav.addObject("greeting", "PAW");
//        return mav;
//    }



    @RequestMapping("/search")
    public ModelAndView search(@RequestParam("name") String name,
                                         @RequestParam(value = "genre", required = false) String filterGenre,
                                         @RequestParam(value = "publisher", required = false) String filterPublisher,
                                         @RequestParam(value = "filters", required = false) String filtersJson
                                        ){

        Map<FilterCategory, List<String>> filters = null;
        try {
            filters = new ObjectMapper().readValue(
                    filtersJson, new TypeReference<HashMap<FilterCategory, ArrayList<String>>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            filters = new HashMap<>();
        }

        Collection<?> searchedGame = gameService.searchGame(name, filters);

        final ModelAndView mav = new ModelAndView("gameSearch");
        Game game = null;
//        HashSet<Filter> filters = new HashSet<Filter>();
//        if (filterGenre != null) filters.add(new Filter(Filter.FilterCategory.GENRES,filterGenre));
//        if(filterPublisher!=null) filters.add(new Filter(Filter.FilterCategory.PUBLISHERS,filterPublisher));
//        Collection<Game> searchedGame = gameService.searchGame(name, filters);
//        mav.addObject("gameList", searchedGame);
        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game() {
        final ModelAndView mav = new ModelAndView("game");
        mav.addObject("greeting", "PAW");
        return mav;
    }
}