package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;

import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Data Access Object for Game Pictures
 */
public interface PictureDao {

    /**
     * Returns a set of pictures for the given game.
     *
     * @param game The game whose pictures will be returned.
     * @return The set of pictures for the given game.
     */
    Set<String> getGamePictures(Game game);

    /**
     * Returns a set of pictures for the game with the given id.
     *
     * @param gameId The game's id whose pictures. will be returned.
     * @return The set of pictures for the game with the given if.
     */
    Set<String> getGamePictures(long gameId);
}
