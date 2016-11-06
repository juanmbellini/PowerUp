package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;

import javax.persistence.TransactionRequiredException;
import java.util.Collection;

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
     * @see UserService#existsWithId(long)
     */
    boolean existsWithId(long id);

    /**
     * @see UserService#existsWithUsername(String)
     */
    boolean existsWithUsername(String username);

    /**
     * @see UserService#existsWithEmail(String)
     */
    boolean existsWithEmail(String email);

    /**
     * @see UserService#scoreGame(long, long, int)
     */
    void scoreGame(long userId, long gameId, int score);

    /**
     * @see UserService#setPlayStatus(long, long, PlayStatus)
     */
    void setPlayStatus(long userId, long gameId, PlayStatus status);

    /**
     * @see UserService#removeScore(long, long)
     */
    void removeScore(long userId, long id);

    /**
     * @see UserService#removeStatus(long, long)
     */
    void removeStatus(long userId, long id);

    /**
     * @see UserService#recommendGames(long)
     */
    Collection<Game> recommendGames(long userId);
}
