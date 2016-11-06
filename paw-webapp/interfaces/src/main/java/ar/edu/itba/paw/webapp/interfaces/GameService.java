package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Game service class. Exposes all functionality available to games.
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
     * Finds games related to a base game that match criteria specified in {@code filters}.
     *
     * @param baseGameId The ID of the game to compare against.
     * @param filters    Criteria under which to find related games.
     * @return The matching games. May be empty but not {@code null}.
     */
    Set<Game> findRelatedGames(long baseGameId, Set<FilterCategory> filters);

    /**
     * Finds the {@link Game} with the specified ID.
     *
     * @param id The game ID.
     * @return The matching game, or {@code null} if not found.
     */
    Game findById(long id);

    /**
     * Finds a collection of games with the given IDs.
     *
     * @param ids The IDs to match.
     * @return A map relating each ID with its matching game, if found.
     */
    Map<Long, Game> findByIds(Collection<Long> ids);

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
     *              for this to return true. I.e. "Hello, World!" will match "hello, wOrld!" but not "hello world" (due
     *              to the lack of comma and exclamation mark)
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
     * Gets all genres associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching genres.
     */
    Collection<Genre> getGenres(long gameId);

    /**
     * Gets all platforms, and their respectiv release dates, associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching platforms with their corresponding release dates.
     */
    Map<Platform, GamePlatformReleaseDate> getPlatforms(long gameId);

    /**
     * Gets all publishers associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching publishers.
     */
    Collection<Publisher> getPublishers(long gameId);

    /**
     * Gets all developers associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching developers.
     */
    Collection<Developer> getDevelopers(long gameId);

    /**
     * Gets all keywords associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching keywords.
     */
    Collection<Keyword> getKeywords(long gameId);

    /**
     * Gets all reviews associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching reviews.
     */
    Collection<Review> getReviews(long gameId);
}
