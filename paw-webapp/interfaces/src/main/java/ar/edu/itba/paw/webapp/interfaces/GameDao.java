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
    Collection<Game> searchGame(String name, Map<FilterCategory,List<String>> filters) throws IllegalArgumentException;

    Game findById(long id);

    Collection<String> getFiltersByType(FilterCategory filterType);

}
