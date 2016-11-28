package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data Access Object for games. Exposes all data-related functionality provided by games.
 */
public interface GameDao {

    /**
     * @see GameService#searchGames(String, Map, OrderCategory, boolean, int, int)
     */
    Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory,
                           boolean ascending, int pageSize, int pageNumber) throws IllegalArgumentException;

    /**
     * Same as {@link GameService#searchGames(String, Map, OrderCategory, boolean, int, int)}
     * but without pagination
     */
    Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory,
                                 boolean ascending) throws IllegalArgumentException;

    /**
     * @see GameService#findRelatedGames(long, Set)
     */
    Collection<Game> findRelatedGames(long gameId, Set<FilterCategory> filters);

    /**
     * @see GameService#findById(long)
     */
    Game findById(long id);

    /**
     * @see GameService#findByIds(Collection<Long>)
     */
    Map<Long, Game> findByIds(Collection<Long> ids);

    /**
     * @see GameService#existsWithId(long)
     */
    boolean existsWithId(long id);

    /**
     * @see GameService#existsWithTitle(String)
     */
    boolean existsWithTitle(String title);

    //TODO: Move this to a FilterDao or to service layer, based on a search result
    //TODO move what? ^ why? Y u no explain? - Juen

    /**
     * @see GameService#getFiltersByType(FilterCategory)
     */
    Collection<String> getFiltersByType(FilterCategory filterCategory);

    /**
     * Update the avgScore of the game with id gameId.
     *
     * @param gameId
     */
    void updateAvgScore(long gameId);

    /**
     * @see GameService#getGenres(long)
     */
    Collection<Genre> getGenres(long gameId);

    /**
     * @see GameService#getPlatforms(long)
     */
    Map<Platform, GamePlatformReleaseDate> getPlatforms(long gameId);

    /**
     * @see GameService#getPublishers(long)
     */
    Collection<Company> getPublishers(long gameId);

    /**
     * @see GameService#getDevelopers(long)
     */
    Collection<Company> getDevelopers(long gameId);

    /**
     * @see GameService#getKeywords(long)
     */
    Collection<Keyword> getKeywords(long gameId);

    /**
     * @see GameService#getReviews(long)
     */
    Collection<Review> getReviews(long gameId);

    /**
     * @see GameService#getScores(long)
     */
    Map<Long, Integer> getScores(long gameId);

    /**
     * Return the recommended games, giving different importance to different filters.
     *
     * @param excludedGameIds A set of games that must no be in the result set.
     * @param filtersScoresMap A map with the score given for each filter. The higher the score, the more likely that a
     *                         different game that matches the same filter will be included in the result.
     * @return The recommended games.
     */
    Collection<Game> getRecommendedGames(Set<Long> excludedGameIds, Map<FilterCategory, Map<String, Double>> filtersScoresMap);

    /**
     * @see GameService#getVideos(long)
     */
    Map<String,String> getVideos(long gameId);
}