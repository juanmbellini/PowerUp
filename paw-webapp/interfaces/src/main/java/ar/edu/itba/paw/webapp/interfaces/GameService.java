package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface GameService {

    //TODO change Object to Game, rebase with master branch

    /**
     * Finds all games with matching names that meet the criteria specified in {@code filters}.
     *
     * @param name Partial or complete name of the game. An empty name will match all games.
     * @param filters Criteria for the games to match.
     * @return Games matching the specified criteria. TODO is there a maximum list size? If so, specify. If not, specify.
     */
    Collection<Game> searchGame(String name, Map<FilterCategory,List<String>> filters);

    /**
     * Finds the game with the specified ID.
     *
     * @param id The game ID.
     * @return The matching game, or {@code null} if not found.
     */
    Game findById(long id);

    /**
     * Finds all available filter values given the filterType.
     *
     * @param filterType the type of the filter (ENUM).
     * @return filter values availables, or empty collection if there is no value available.
     */
    Collection<String> getFiltersByType(FilterCategory filterType);
}
