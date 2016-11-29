package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.ShelfService;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Controller for shelf operations.
 */
@Controller
public class ShelfController extends BaseController {

    /**
     * A Object Mapper to generate Objects from JSONs.
     */
    private final ObjectMapper objectMapper;

    /**
     * A TypeReference that references to a {@code Map<{@link FilterCategory }, List<String>>}, which represents
     * filters that can be applied to game searches.
     */
    private final TypeReference<ArrayList<String>> typeReference;


    /**
     * A game service used for listing games.
     */
    private GameService gameService;

    private final ShelfService shelfService;

    @Autowired
    public ShelfController(UserService us, GameService gameService, ShelfService shelfService) {
        super(us);
        this.gameService = gameService;
        this.shelfService = shelfService;
        objectMapper = new ObjectMapper();
        typeReference = new TypeReference<ArrayList<String>>() {};
    }

    @RequestMapping(value = "/shelves")
    public ModelAndView list(@RequestParam(value = "username", required = false) String username,
//                             @RequestParam(value = "shelves", required = false) String shelvesStr,
//                             @RequestParam(value = "playStatuses", required = false) String playStatusesStr
                             @RequestParam(value = "playStatusesCheckbox", required = false) String[] playStatusesCheckboxStr,
                             @RequestParam(value = "shelvesCheckbox", required = false) String[] shelvesCheckboxStr) {
        if (username == null) {
            if (!isLoggedIn()) {
                return new ModelAndView("redirect:error400");
            }
            return list(getCurrentUsername(),playStatusesCheckboxStr,shelvesCheckboxStr);
        }
        final ModelAndView mav = new ModelAndView("shelves");
        User user = userService.findByUsername(username);
        if (user == null) return new ModelAndView("error400");

        Set<String> shelvesFilter = new HashSet<>();
        if(shelvesCheckboxStr!=null){
            for(String s: shelvesCheckboxStr){
                shelvesFilter.add(s);
            }
        }

        Set<String> playStatusesFilter = new HashSet<>();
        if(playStatusesCheckboxStr!=null){
            for(String s: playStatusesCheckboxStr){
                playStatusesFilter.add(s);
            }
        }


        Map<Game, Set<Shelf>> shelvesForGames = new HashMap();
        Map<Game, PlayStatus> playStatuses = new HashMap<>();


        for (Map.Entry<PlayStatus, Set<Game>> entry : userService.getGameList(user.getId()).entrySet()) {
            // TODO use other set and give it order? ScoreOrder? (If treeSet is used, danger of eliminating games)
            PlayStatus status = entry.getKey();
            Set<Game> games = entry.getValue();
            for(Game game : games) {
                if(!shelvesForGames.containsKey(game)){
                    shelvesForGames.put(game,new HashSet<>());
                }
                if(!playStatuses.containsKey(game)) {
                    playStatuses.put(game, status);
                }
            }
        }
        Set<Shelf> shelves = shelfService.findByUserId(user.getId());
        for(Shelf shelf : shelves) {
            for(Game game : shelf.getGames()) {
                if(shelvesForGames.containsKey(game)){
                    shelvesForGames.get(game).add(shelf);
                }
            }
        }
        //scores
        Map<Game, Integer> scores = userService.getScoredGames(user.getId());


        Set<Game> games = new HashSet<>();

        for(Game game: playStatuses.keySet()){
            boolean validGame = false;
            if(playStatusesFilter.isEmpty()) validGame =true;
            for(String playStatusFilter: playStatusesFilter){
                if(playStatuses.get(game).name().equals(playStatusFilter)){
                    validGame = true;
                }
            }
            for(String shelfFilter: shelvesFilter){
                for(Shelf shelf: shelvesForGames.get(game)){
                    if(!shelf.getName().equals(shelfFilter)){
                        validGame = false;
                    }
                }
            }
            if(validGame) games.add(game);
        }



        mav.addObject("playstatus",PlayStatus.values());
        mav.addObject("playStatusesFilter",playStatusesFilter);
        mav.addObject("shelvesFilter",shelvesFilter);
        mav.addObject("games",games);
        mav.addObject("user", user);
        mav.addObject("scores",scores);
        mav.addObject("shelves", shelves);
        mav.addObject("shelvesForGamesMap",shelvesForGames);
        mav.addObject("playStatuses", playStatuses);
        return mav;
    }

    @RequestMapping(value = "/create-shelf", method = RequestMethod.POST)
    public ModelAndView createShelf(@RequestParam(value = "name") String name, @RequestParam(value = "gameIds", required = false) long[] gameIds) {
        try {
            if(!isLoggedIn()) {
                LOG.warn("User {} attempted to create a, unauthorized", getCurrentUsername());
                return new ModelAndView("error404");
            }
            if(!shelfService.findByName(name).isEmpty()) {
                LOG.info("User {} attempted to create Shelf with duplicate name (\"{}\")", getCurrentUsername(), name);
                //TODO show the error to the user
                return new ModelAndView("redirect:/shelves?username=" + getCurrentUsername());
            }
            shelfService.create(name, getCurrentUser().getId(), gameIds == null ? new long[0] : gameIds);
            LOG.info("Created Shelf \"{}\" for user {}", name, getCurrentUsername());
        } catch (Exception e) {
            LOG.error("Error creating shelf \"{}\" for user {}:", name, getCurrentUsername(), e);
            return new ModelAndView("error500");
        }

        return new ModelAndView("redirect:/shelves?username=" + getCurrentUsername());
    }

    @RequestMapping(value = "/update-shelf", method = RequestMethod.POST)
    public ModelAndView updateShelf(@RequestParam(value = "shelfId") long shelfId, @RequestParam(value = "newIds") long[] newGameIds) {
        try {
            if(!isLoggedIn() || !shelfService.belongsTo(shelfId, getCurrentUser().getId())) {
                LOG.warn("User {} attempted to update Shelf #{}, unauthorized", getCurrentUsername(), shelfId);
                return new ModelAndView("error404");
            }
            shelfService.update(shelfId, newGameIds);
            LOG.info("Updated Shelf #{} with the following game IDs: {}", shelfId, newGameIds);
        } catch (NoSuchEntityException e) {
            LOG.info("User {} attempted to update nonexistent Shelf #{}", getCurrentUsername(), shelfId);
            return new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error updating shelf #{}:", shelfId, e);
            return new ModelAndView("error500");
        }

        return new ModelAndView("redirect:/shelves?username=" + getCurrentUsername());
    }

    @RequestMapping(value = "/delete-shelf", method = RequestMethod.POST)
    public ModelAndView deleteShelf(@RequestParam(value = "shelfId") long shelfId) {
        try {
            if(!isLoggedIn() || !shelfService.belongsTo(shelfId, getCurrentUser().getId())) {
                LOG.warn("User {} attempted to delete Shelf #{}, unauthorized", getCurrentUsername(), shelfId);
                return new ModelAndView("error404");
            }
            shelfService.delete(shelfId);
            LOG.info("Deleted Shelf #{}", shelfId);
        } catch (NoSuchEntityException e) {
            LOG.info("User {} attempted to delete nonexistent Shelf #{}", getCurrentUsername(), shelfId);
            return new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error deleting shelf #{}:", shelfId, e);
            return new ModelAndView("error500");
        }

        return new ModelAndView("redirect:/shelves?username=" + getCurrentUsername());
    }

    @RequestMapping(value = "/rename-shelf", method = RequestMethod.POST)
    public ModelAndView renameShelf(@RequestParam(value = "shelfId") long shelfId, @RequestParam(value = "name") String newName) {
        try {
            if(!isLoggedIn() || !shelfService.belongsTo(shelfId, getCurrentUser().getId())) {
                LOG.warn("User {} attempted to rename Shelf #{}, unauthorized", getCurrentUsername(), shelfId);
                return new ModelAndView("error404");
            }
            String oldName = shelfService.findById(shelfId).getName();
            shelfService.rename(shelfId, newName);
            LOG.info("Renamed Shelf \"{}\" (#{}) to \"{}\"", oldName, shelfId, newName);
        } catch (NoSuchEntityException e) {
            LOG.info("User {} attempted to rename nonexistent Shelf #{}", getCurrentUsername(), shelfId);
            return new ModelAndView("error404");
        } catch (Exception e) {
            LOG.error("Error deleting shelf #{}:", shelfId, e);
            return new ModelAndView("error500");
        }

        return new ModelAndView("redirect:/shelves?username=" + getCurrentUsername());
    }

    @RequestMapping(value = "/update-shelves-by-game", method = RequestMethod.POST)
    public ModelAndView updateByGame(@RequestParam(value = "gameId") long gameId, @RequestParam Map<String, String> updates, @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        try {
            if(!isLoggedIn()) {
                LOG.warn("Unauthenticated user attempted to update Game #{} in shelves, unauthorized", gameId);
                return new ModelAndView("error404");
            }
            for(Map.Entry<String, String> entry : updates.entrySet()) {
                if(!entry.getKey().equals("gameId") && !entry.getKey().equals("returnUrl")) {
                    //Treat each operation separately - if one fails (but ONLY from a NoSuchEntityException), the others can still try to execute
                    long shelfId = Long.valueOf(entry.getKey());
                    boolean add = Boolean.valueOf(entry.getValue());
                    try {
                        if(shelfService.belongsTo(shelfId, getCurrentUser().getId())) {
                            if(add) {
                                shelfService.addGame(shelfId, gameId);
                            } else {
                                shelfService.removeGame(shelfId, gameId);
                            }
                            LOG.info("{} Game #{} {} {}'s Shelf #{}", add ? "Added" : "Removed", gameId, add ? "to" : "from", getCurrentUsername(), shelfId);
                        } else {
                            LOG.info("User {} attempted to modify Shelf #{} that doesn't belong to them, skipping", getCurrentUsername(), shelfId);
                        }
                    } catch(NoSuchEntityException e) {
                        LOG.info("User {} attempted to {} Game #{} {} nonexistent Shelf #{}", getCurrentUsername(), add ? "add" : "remove", gameId, add ? "to" : "from", shelfId);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Error updating shelves by game:", gameId, e);
            return new ModelAndView("error500");
        }

        return new ModelAndView("redirect:" + (returnUrl != null ? returnUrl : "/game?id=" + gameId));
    }
}
