package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;

public interface GameService {

    /**
     * Finds all games with matching names that meet the criteria specified in {@code filters}.
     *
     * @param name Partial or complete name of the game. An empty name will match all games.
     * @param filters Criteria for the games to match.
     * @return Games matching the specified criteria. TODO is there a maximum list size? If so, specify. If not, specify.
     */
    List<Game> searchGames(String name, Collection<Filter> filters);

    /**
     * Finds the game with the specified ID.
     *
     * @param id The game ID.
     * @return The matching game, or {@code null} if not found.
     */
    Game findById(int id);
}
