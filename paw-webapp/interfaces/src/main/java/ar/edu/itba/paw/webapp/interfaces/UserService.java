package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * User service class. Exposes all functionality available to users.
 */
public interface UserService {

    /**
     * Creates a new user with a specified email and (optionally) a specified username.
     *
     * @param email    The user's email. Must be unique.
     * @param password The user's password.
     * @param username The user's username. Must be unique.
     * @return The created user.
     * @throws UserExistsException If a user with {@code email} or {@code username} already exists.
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
     * Checks whether a user with a given ID exists.
     *
     * @param id The ID of the user to check.
     * @return Whether such a user exists.
     */
    boolean existsWithId(long id);

    /**
     * Checks whether a user with a given username exists.
     *
     * @param username The username to check. Case sensitive.
     * @return Whether such a user exists.
     */
    boolean existsWithUsername(String username);

    /**
     * Checks whether a user with a given email exists.
     *
     * @param email The username to check. Case <strong>in-</strong>sensitive.
     * @return Whether such a user exists.
     */
    boolean existsWithEmail(String email);

    /**
     * Sets or updates a score for a specified game by a specified user.
     *
     * @param user   The user who is scoring the game.
     * @param gameId The scored game's ID.
     * @param score  The score, where 1 <= {@code score} <= 10
     */
    void scoreGame(User user, long gameId, int score);

    /**
     * Sets or updates a score for a specified game by a specified user.
     *
     * @param user  The user who is scoring the game.
     * @param game  The scored game.
     * @param score The score, where 1 <= {@code score} <= 10
     */
    void scoreGame(User user, Game game, int score);

    /**
     * Gets the games in the specified user's game list that are marked with the specified play status.
     *
     * @param user The user whose game list to look in.
     * @param status The status for games to match.
     * @return The matching games.
     */
    Set<Game> getGamesByStatus(User user, PlayStatus status);

    /**
     * Gets a map of all games scored by this user with their corresponding score.
     *
     * @param user The user whose scored games to check.
     * @return The resulting map.
     */
    Map<Game, Integer> getScoredGames(User user);

    /**
     * Sets or updates a play status for a specified game for a specified user.
     *
     * @param user   The user who is setting or updating status.
     * @param gameId The ID of the game whose status is being registered.
     * @param status The status.
     */
    void setPlayStatus(User user, long gameId, PlayStatus status);

    /**
     * Sets or updates a play status for a specified game for a specified user.
     *
     * @param user   The user who is setting or updating status.
     * @param game   The game whose status is being registered.
     * @param status The status.
     */
    void setPlayStatus(User user, Game game, PlayStatus status);

    /**
     * Removes score from user u to game id.
     *
     * @param u  The user who is getting a score removed
     * @param id The id of the game whose score is getting removed
     */
    void removeScore(User u, long id);

    /**
     * Removes status from user u to game id.
     *
     * @param u  The user who is getting a gameStatus removed
     * @param id The id of the game whose gameStatus is getting removed
     */
    void removeStatus(User u, long id);

    /**
     * Recommends games for user based on the scores of the games he has scored
     *
     * @param user The user who is getting the recommendations
     */
    Collection<Game> recommendGames(User user);
}
