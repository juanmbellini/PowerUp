package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
public class HelloWorldController {

    private final GameService gameService;

    @Autowired
    public HelloWorldController(GameService gameService) {
        //Spring is in charge of providing the gameService parameter.
        this.gameService = gameService;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld() {
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("greeting", "PAW");
        return mav;
    }
    @RequestMapping("/game/search")
    public ModelAndView searchGameByName() {
        final ModelAndView mav = new ModelAndView("gameSearch");
        Collection<Game> searchedGame = gameService.findByName("Mario"); //TODO variable parameter
        mav.addObject("gameList", searchedGame);
        return mav;
    }
}
