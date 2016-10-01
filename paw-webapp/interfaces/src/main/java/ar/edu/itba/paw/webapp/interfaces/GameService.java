package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Game service class. Exposes all functionality available to games. For this project, implementations of this service
 * are usually simple as the service adds little to no extra functionality other than finding games.
 */
public interface GameService {

    /**
     * Finds all games with matching names that meet the criteria specified in {@code filters}. Filters are applied as
     * (filter category, value) entries in {@code filters}. Results are ordered with field specified in {@code orderCategory}
     * and ascending if {@code ascending == true}.
     *
     * @param name    Partial or complete name of the game. An empty name will match all games.
     * @param filters Criteria for the games to match. May be empty, but not {@code null}.
     * @return Games matching the specified criteria. TODO is there a maximum list size? If so, specify. If not, specify.
     */
    Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean ascending);

    /**
     * Finds games related to {@code baseGame} that match criteria specified in {@code filters}.
     *
     * @param baseGame The game to compare against.
     * @param filters  Criteria under which to find related games.
     * @return The matching games. May be empty but not {@code null}.
     */
    Set<Game> findRelatedGames(Game baseGame, Set<FilterCategory> filters);

    /**
     * Finds the {@link Game} with the specified ID.
     *
     * @param id The game ID.
     * @return The matching game, or {@code null} if not found.
     */
    Game findById(long id);

    /**
     * Returns a collection with all the filters that can be applied to a specified {@link FilterCategory}.
     *
     * @param filterCategory The filter category.
     * @return A collection with all the filters that can be applied with the given filter category.
     */
    Collection<String> getFiltersByType(FilterCategory filterCategory);
}
