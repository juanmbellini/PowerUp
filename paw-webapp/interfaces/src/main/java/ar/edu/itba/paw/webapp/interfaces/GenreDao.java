package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import org.joda.time.LocalDate;

import java.util.Map;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Data Access Object for Game Platforms
 */
public interface GenreDao {

    /**
     * Returns a set of genres for the given game.
     *
     * @param game The game whose genres will be returned.
     * @return The set of genres for the given game.
     */
    Set<String> getGameGenres(Game game);

    /**
     * Returns a set of genres for the game with the given id.
     *
     * @param gameId The game's id whose genres. will be returned.
     * @return The set of genres for the game with the given if.
     */
    Set<String> getGameGenres(long gameId);
}
