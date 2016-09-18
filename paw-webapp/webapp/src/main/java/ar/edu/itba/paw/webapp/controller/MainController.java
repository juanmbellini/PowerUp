package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ModelAndView search(@RequestParam("name") String name,
                               @RequestParam(value = "filters", required = false) String filtersJson) {

        final ModelAndView mav = new ModelAndView("gameSearch");
        if (filtersJson == null || filtersJson.equals("")) {
            filtersJson = "{}";
        }

        Map<FilterCategory, List<String>> filters = null;
        try {
            filters = new ObjectMapper().readValue(
                    filtersJson, new TypeReference<HashMap<FilterCategory, ArrayList<String>>>() {
                    });
            mav.addObject("gameList", gameService.searchGame(name, filters));
        } catch (IOException e) {
            e.printStackTrace();  // Wrong JSON!!
            //TODO: Send something into the ModelAndView indicating the error
        }
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