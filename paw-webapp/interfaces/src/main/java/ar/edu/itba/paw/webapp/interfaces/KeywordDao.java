package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;

import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Data Access Object for Game Keywords
 */
public interface KeywordDao {

    /**
     * Returns a set of keywords for the given game.
     *
     * @param game The game whose keywords will be returned.
     * @return The set of keywords for the given game.
     */
    Set<String> getGameKeywords(Game game);

    /**
     * Returns a set of keywords for the game with the given id.
     *
     * @param gameId The game's id whose keywords. will be returned.
     * @return The set of keywords for the game with the given if.
     */
    Set<String> getGameKeywords(long gameId);
}
