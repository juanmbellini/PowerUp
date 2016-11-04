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
     * @see UserService#scoreGame(User, long, int)
     */
    void scoreGame(User user, long gameId, int score);

    /**
     * @see UserService#scoreGame(User, Game, int)
     */
    void scoreGame(User user, Game game, int score);

    /**
     * @see UserService#setPlayStatus(User, long, PlayStatus)
     */
    void setPlayStatus(User user, long gameId, PlayStatus status);

    /**
     * @see UserService#setPlayStatus(User, Game, PlayStatus)
     */
    void setPlayStatus(User user, Game game, PlayStatus status);

    /**
     * @see UserService#removeScore(User, long)
     */
    void removeScore(User u, long id);

    /**
     * @see UserService#removeStatus(User, long)
     */
    void removeStatus(User u, long id);

    /**
     * @see UserService#recommendGames(User)
     */
    Collection<Game> recommendGames(User user);

    /**
     * Binds the specified user to the current transaction context so lazily-loaded collections can be fetched from the
     * database.
     *
     * @param user The user to bind.
     * @throws IllegalArgumentException If the specified user instance is not an entity or is a removed entity. (TODO remove this annotation? This should never happen)
     * @throws TransactionRequiredException If there is no transaction ongoing.
     */
    //TODO does this belong at interface level? JDBC wouldn't need to implement this at all, this is 100% Hibernate-dependent
    //Consider moving to Hibernate DAOs, storing Hibernate types in Hibernate services (e.g. UserHibernateDao in
    //UserService, instead of generid UserDao) and calling method directly
    User bindToCurrentTransaction(User user);
}
