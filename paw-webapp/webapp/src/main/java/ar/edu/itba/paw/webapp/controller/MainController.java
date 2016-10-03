package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atteo.evo.inflector.English;
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


    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final static TypeReference<HashMap<FilterCategory, ArrayList<String>>> typeReference
            = new TypeReference<HashMap<FilterCategory, ArrayList<String>>>() {
    };

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
    public ModelAndView search(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "orderCategory", required = false) String orderParameter,
                               @RequestParam(value = "filters", required = false) String filtersJson) {

        final ModelAndView mav = new ModelAndView();

        if (filtersJson == null || filtersJson.equals("")) {
            filtersJson = "{}";
        }
        if(name == null){
            name = "";
        }
        Map<FilterCategory, List<String>> filters = null;
        try {
            filters = objectMapper.readValue(filtersJson, typeReference);
            //TODO make a new function for this
            if(orderParameter == null) {
                orderParameter = "name";
            }if(orderParameter.equals("release date")){
                orderParameter = "release";
            }else if(orderParameter.equals("avg-rating")){
                orderParameter = "avg_score";
            }else{
                return error404();
            }

            mav.addObject("results", gameService.searchGames(name, filters, OrderCategory.valueOf(orderParameter), true));
            mav.addObject("hasFilters", !filtersJson.equals("{}"));
            mav.addObject("appliedFilters", filters);
            mav.addObject("searchedName", name);
            mav.setViewName("search");
            mav.addObject("filters", filtersJson);
        } catch (IOException e) {
            e.printStackTrace();  // Wrong JSON!!
            mav.setViewName("redirect:error500");
        }
        return mav;
    }


    @RequestMapping("/advanced-search")
    public ModelAndView advancedSearch() {
        final ModelAndView mav = new ModelAndView("advanced-search");
        //Add all possible filter types
        for (FilterCategory filterCategory : FilterCategory.values()) {
            try {
                mav.addObject(English.plural(filterCategory.name()).toUpperCase(),
                        gameService.getFiltersByType(filterCategory));
//                mav.addObject((filterCategory.name() + "s").toUpperCase(), gameService.getFiltersByType(filterCategory));
            } catch (Exception e) {
                return error500();
            }
        }
        return mav;
    }

    @RequestMapping("/game")
    public ModelAndView game(@RequestParam(name = "id") int id) {
        final ModelAndView mav = new ModelAndView("game");
        Game game;
        try {
            game = gameService.findById(id);
        } catch (Exception e) {
            return error500();
        }
        if(game == null){
            return error404();
        }
        mav.addObject("game", game);
        return mav;
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

}