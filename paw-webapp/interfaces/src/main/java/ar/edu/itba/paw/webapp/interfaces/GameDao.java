package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Data Access Object for games. Exposes all data-related functionality provided by games.
 */
public interface GameDao {

    /**
     * @see GameService#searchGames(String, Map, List, OrderCategory, boolean)
     * @throws IllegalArgumentException if a list in the {@code filters} map is null.
     */
    Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters, OrderCategory orderCategory, boolean order) throws IllegalArgumentException;

    /**
     * @see GameService#findRelatedGames(Game, Set)
     */
    Set<Game> findRelatedGames(Game baseGame, Set<FilterCategory> filters);

    /**
     * @see GameService#findById(long)
     */
    Game findById(long id);

    //TODO: Move this to a FilterDao or to service layer, based on a search result
    //TODO move what? ^ why? Y u no explain? - Juen

    /**
     * @see GameService#getFiltersByType(FilterCategory)
     */
    Collection<String> getFiltersByType(FilterCategory filterCategory);

}
