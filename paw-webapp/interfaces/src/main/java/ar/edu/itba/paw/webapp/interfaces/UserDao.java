package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Data Access Object for users.
 */
public interface UserDao {

    /**
     * @see UserService#create(String, String, String)
     */
    User create(String email, String password, String username) throws UserExistsException;

    /**
     * @see UserService#all()
     */
    List<User> all();

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

    /**
     *  @see UserService#recommendGames(long,Set)
     */
    Collection<Game> recommendGames(long userId, Set<Shelf> shelves);

    /**
     * @see UserService#setProfilePicture(long, byte[], String)
     */
    void setProfilePicture(long userId, byte[] picture, String mimeType) throws NoSuchEntityException;

    /**
     * @see UserService#removeProfilePicture(long)
     */
    void removeProfilePicture(long userId);

    /**
     * @see UserService#changePassword(long, String)
     */
    void changePassword(long userId, String newHashedPassword);

    /**
     * @see UserService#deleteById(long)
     */
    void deleteById(long userId);

//    /**
//     * @see UserService#getGameList(long,Set,Set)
//     */
//    Map<Game,PlayStatus> getGameList(long userId, Set<String> playStatusesFilter, Set<String> shelvesFilter);
}
