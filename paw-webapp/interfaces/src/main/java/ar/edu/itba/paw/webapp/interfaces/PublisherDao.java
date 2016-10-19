package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;

import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Data Access Object for Game Publishers
 */
public interface PublisherDao {

    /**
     * Returns a set of publishers for the given game.
     *
     * @param game The game whose publishers will be returned.
     * @return The set of publishers for the given game.
     */
    Set<String> getGamePublishers(Game game);

    /**
     * Returns a set of publishers for the game with the given id.
     *
     * @param gameId The game's id whose publishers. will be returned.
     * @return The set of publishers for the game with the given if.
     */
    Set<String> getGamePublishers(long gameId);
}
