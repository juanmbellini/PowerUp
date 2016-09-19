package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GameDao {

    /**
     * Returns a <code>Collection</code> of <code>Game</code> that have in their names <code>name</code>
     * as a sub-string.
     * <p>
     *      Filters can be applied in the form of a <code>Map</code>, where each key is a FilterCategory, and the
     *      corresponding value is a <code>List</code> of <code>Strings</code>, indicating each value to be applied
     *      as a filter. The list can be empty, but not null.
     * </p>
     * @param name The <code>Game</code> name that must be matched.
     * @param filters A <code>Map</code> with filters to be applied.
     * @return The <code>Collection</code> of games
     *
     * @throws IllegalArgumentException if a list in the <code>filters</code> <code>Map</code> is null.
     */
    Collection<Game> searchGame(String name, Map<FilterCategory,List<String>> filters)
            throws IllegalArgumentException;

    /**
     * Returns a Game whose id is the specified <code>id</code>, or <code>null</code> if not present.
     * @param id The Game's id
     * @return The Game whose id is the specified <code>id</code>, or <code>null</code> if not present.
     */
    Game findById(long id);

    //TODO: Move this to a FilterDao or to service layer, based on a search result

    /**
     * Returns a <code>Collection</code> with all ther filters that can be applied
     * with a given <code>filterCategory</code>
     * @param filterCategory The <code>FilterCategory</code>
     * @return A <code>Collection</code> with all the filters that can be applied
     * with a given <code>filterCategory</code>
     */
    Collection<String> getFiltersByType(FilterCategory filterCategory);

}
