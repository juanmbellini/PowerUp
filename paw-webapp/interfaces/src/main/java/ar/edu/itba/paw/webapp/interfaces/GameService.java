package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Game2;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.utilities.Page;

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
     * @return A {@link Page} of games matching the specified criteria.
     * @throws IllegalArgumentException if a list in the {@code filters} map is null.
     */
    Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory,
                           boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException;

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
     * Checks whether a game with a given ID exists.
     *
     * @param id The ID of the game to check.
     * @return Whether such a game exists.
     */
    boolean existsWithId(long id);

    /**
     * Checks whether a game with a given title exists.
     *
     * @param title The title of the game to check. Must be an exact (albeit <strong>in-</strong>sensitive) match
     *             for this to return true. I.e. "Hello, World!" will match "hello, wOrld!" but not "hello world" (due
     *             to the lack of comma and exclamation mark)
     * @return Whether such a game exists.
     */
    boolean existsWithTitle(String title);

    /**
     * Returns a collection with all the filters that can be applied to a specified {@link FilterCategory}.
     *
     * @param filterCategory The filter category.
     * @return A collection with all the filters that can be applied with the given filter category.
     */
    Collection<String> getFiltersByType(FilterCategory filterCategory);

    /**
     * @param ids the collection of the ids
     * @return a Map from gameId to a Game with the basic data of the game.
     */
    Map<Long,Game> findBasicDataGamesFromArrayId(Collection<Long> ids);

    public Game2 findById2(long id);
}
