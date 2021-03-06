package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
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
     * @param name     Partial or complete name of the game. An empty name will match all games.
     * @param filters  Criteria for the games to match. May be empty, but not {@code null}.
     * @param pageSize is the size of the page. If it is 0, it will be considered to be the full page.
     * @return A {@link Page} of games matching the specified criteria.
     * @throws IllegalArgumentException if a list in the {@code filters} map is null.
     */
    Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory,
                           boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException;

    /**
     * Finds games related to a base game that match criteria specified in {@code filters}.
     *
     * @param gameId  The ID form the game to compare against.
     * @param filters Criteria under which to find related games.
     * @return The matching games. May be empty but not {@code null}.
     */
    Collection<Game> findRelatedGames(long gameId, Set<FilterCategory> filters);

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
     * Returns a {@link Page} with filters that can be applied to a specified {@link FilterCategory}.
     *
     * @param pageNumber     The number of the {@link Page}.
     * @param pageSize       The size of the {@link Page}.
     * @param filterCategory The filter category.
     * @return A collection of strings in which each element is a value that can be applied
     * as a filter of the given category
     */
    Page<String> getFiltersByType(FilterCategory filterCategory, int pageNumber, int pageSize);

    /**
     * @return A random game id from the game database.
     */
    long getRandomGameId();

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
    Collection<Company> getPublishers(long gameId);

    /**
     * Gets all developers associated with a game.
     *
     * @param gameId The ID of the game to search.
     * @return The matching developers.
     */
    Collection<Company> getDevelopers(long gameId);

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

    /**
     * Given a game ID, gets a map relating user IDs to the scores that said users
     * gave to the specified game. This returns the "inverse" map that would result
     * from calling {@link UserService#getScoredGames(long)}.
     *
     * @param gameId The ID of the game whose scores to get.
     * @return The resulting map.
     */
    Map<Long, Integer> getScores(long gameId);

    /**
     * Gets all videos for a given game.
     *
     * @param gameId The ID of the game.
     * @return A map whose keys are YouTube video IDs and whose values are video names.
     * @throws NoSuchEntityException If the game doesn't exist.
     */
    Map<String, String> getVideos(long gameId) throws NoSuchEntityException;

    /**
     * Gets all picture URLs for a given game.
     *
     * @param gameId The ID of the game.
     * @return A set of picture URLs for the game.
     * @throws NoSuchEntityException If the game doesn't exist.
     */
    Set<String> getPictureUrls(long gameId) throws NoSuchEntityException;
}
