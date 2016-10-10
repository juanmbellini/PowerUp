package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.User;

/**
 * User service class. Exposes all functionality available to users.
 */
public interface UserService {

    /**
     * Creates a new user with a specified email and (optionally) a specified username.
     *
     * @param email The user's email. Must be unique.
     * @param password The user's password.
     * @param username (Optional) The user's username. Must be unique.
     * @return The created user.
     * @throws UserExistsException If a user with {@code email} already exists.
     */
    User create(String email, String password, String username) throws UserExistsException;

    /**
     * Finds a user by username.
     *
     * @param username The username. Case-sensitive.
     * @return The found user or {@code null} if not found.
     */
    User findByUsername(String username);

    /**
     * Finds a user by email.
     *
     * @param email The email. Case-<strong>in</strong>sensitive.
     * @return The found user or {@code null} if not found.
     */
    User findByEmail(String email);

    /**
     * Finds a user by id.
     *
     * @param id The user ID.
     * @return The found user or {@code null} if not found.
     */
    User findById(long id);

    /**
     * Records a score for a specified game by a specified user.
     *
     * @param user The user who is scoring the game.
     * @param gameId The ID of the scored game.
     * @return {@code true} on success, {@code false} on error (e.g. user already scored this game).
     */
    boolean scoreGame(User user, long gameId);

    /**
     * Records a score for a specified game by a specified user.
     *
     * @param user The user who is scoring the game.
     * @param game The scored game.
     * @return {@code true} on success, {@code false} on error (e.g. user already scored this game).
     */
    default boolean scoreGame(User user, Game game) {
        return scoreGame(user, game.getId());
    }
}
