package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;

/**
 * Data Access Object for users.
 */
public interface UserDao {

    /**
     * @see UserService#create(String, String, String)
     */
    User create(String email, String password, String username) throws UserExistsException;

    /**
     * @see UserService#findByUsername(String)
     */
    User findByUsername(String username);

    /**
     * @see UserService#findByEmail(String)
     */
    User findByEmail(String email);

    /**
     * @see UserService#findById(long)
     */
    User findById(long id);

    /**
     * @see UserService#scoreGame(User, long)
     */
    boolean scoreGame(User user, long gameId);

    /**
     * @see UserService#scoreGame(User, Game)
     */
    default boolean scoreGame(User user, Game game) {
        return scoreGame(user, game.getId());
    }
}
