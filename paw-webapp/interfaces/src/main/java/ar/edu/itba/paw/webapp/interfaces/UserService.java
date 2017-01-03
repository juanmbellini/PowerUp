package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Collection;
import java.util.List;
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
     * Gets all users.
     *
     * @return All registered users.
     */
    List<User> all();

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
     * @param userId The ID of user who is scoring the game.
     * @param gameId The scored game's ID.
     * @param score  The score, where 1 <= {@code score} <= 10
     */
    void scoreGame(long userId, long gameId, int score);

    /**
     * Gets the games in the specified user's game list that are marked with the specified play status.
     *
     * @param userId The ID of the user whose game list to look in.
     * @param status The status for games to match.
     * @return The matching games.
     */
    Set<Game> getGamesByStatus(long userId, PlayStatus status);

    /**
     * Gets a map of all games scored by this user with their corresponding score.
     *
     * @param userId The ID of the user whose scored games to check.
     * @return The resulting map.
     */
    Map<Game, Integer> getScoredGames(long userId);

    /**
     * Gets the specified user's scored games in reverse indexing - instead of mapping <gameId, score>, the mapping is
     * of <score, gameIds>.
     *
     * @return An unmodifiable map containing the same information as {@link #getScoredGames(long)} but with reverse
     * indexing.
     */
    Map<Integer, Set<Long>> getScoredGamesRev(long userId);

    /**
     * Checks whether the specified user has scored the game with the specified ID.
     *
     * @param userId The ID of the user who should have rated the game.
     * @param gameId The ID of the game to check.
     * @return Whether the specified user has scored the game with ID {@code gameId}.
     */
    boolean hasScoredGame(long userId, long gameId);

    /**
     * Gets the score that this user gave to a specified game.
     *
     * @param userId The ID of the user whose scores to check.
     * @param gameId The ID of the game for which to get score.
     * @return The score that this user gave to the game with ID {@code gameId}.
     * @throws IllegalArgumentException If this user hasn't scored the specified game.
     * @see #hasScoredGame(long, long)
     */
    int getGameScore(long userId, long gameId);

    /**
     * Checks whether this user has recorded a play status for the game with the specified ID.
     *
     * @param userId The ID of the user to check.
     * @param gameId The ID of the game to check.
     * @return Whether this user has registered a play status for the game with ID {@code gameId}.
     */
    boolean hasPlayStatus(long userId, long gameId);

    /**
     * Gets the play status that this user set for a specified game.
     *
     * @param userId The ID of the user to check.
     * @param gameId The ID of the game for which to get a play status.
     * @return The registered play status.
     * @throws IllegalArgumentException If this user hasn't set a play status for the game with ID {@code gameId}.
     * @see #hasPlayStatus(long, long)
     */
    PlayStatus getPlayStatus(long userId, long gameId);

    /**
     * Sets or updates a play status for a specified game for a specified user.
     *
     * @param userId The ID of the user who is setting or updating status.
     * @param gameId The ID of the game whose status is being registered.
     * @param status The status.
     */
    void setPlayStatus(long userId, long gameId, PlayStatus status);

    /**
     * Removes a score recorded by a specified user for a specified game.
     *
     * @param userId The ID of the user who is getting a score removed.
     * @param gameId The ID of the game whose score is getting removed.
     */
    void removeScore(long userId, long gameId);

    /**
     * Removes status from user u to game id.
     *
     * @param userId The ID of the user who is getting a gameStatus removed
     * @param gameId The ID of the game whose gameStatus is getting removed
     */
    void removeStatus(long userId, long gameId);

    /**
     * Recommends games for user based on the scores of the games he has scored
     *
     * @param userId The ID of the user who is getting the recommendations
     */
    Collection<Game> recommendGames(long userId);

    /**
     * Recommends games for user based on the scores of the games he has scored for the shelf selected
     * @param userId
     * @param shelves
     * @return
     */
    Collection<Game> recommendGames(long userId, Set<Shelf> shelves);

    /**
     * Gets all games in this user's main game list (games they have marked as playing, played, etc.).
     *
     * @param userId The ID of the user whose list to fetch.
     * @return The user's game list, mapping each {@link PlayStatus} to a set of games.
     */
    Map<PlayStatus, Set<Game>> getGameList(long userId);

    /**
     *
     *
     * @param userId The ID of the user whose list to fetch.
     * @return The user's game list, mapping each {@link PlayStatus} to a set of games.
     */

//    /**
//     * Gets all games in this user's main game list (games they have marked as playing, played, etc.), using playStatusFilter and shelvesFilters.
//     * @param id
//     * @param playStatusesFilter
//     * @param shelvesFilter
//     * @return
//     */
//    Map<Game, PlayStatus>  getGameList(long id, Set<String> playStatusesFilter, Set<String> shelvesFilter);

    /**
     * Sets the profile picture for a user. Only authenticated users may set their own pictures.
     *
     * @param userId  The ID of the user whose profile picture to set.
     * @param picture The picture's binary data.
     * @throws NoSuchEntityException If no such user exists.
     */
    void setProfilePicture(long userId, byte[] picture) throws NoSuchEntityException;

    /**
     * Removes the profile picture for a user. Only authenticated users may remove their own pictures.
     *
     * @param userId  The ID of the user whose profile picture to remove.
     * @throws NoSuchEntityException If no such user exists.
     */
    void removeProfilePicture(long userId) throws NoSuchEntityException;

    /**
     * Changes the user's hashed password for the provided one.
     * @param newHashedPassword new password. Must be hashed.
     * @param userId  The ID of the user whose password to change.
     * @throws NoSuchEntityException If no such user exists.
     */
    void changePassword(long userId, String newHashedPassword) throws NoSuchEntityException;

    /**
     * Returns a new randomly generated password.
     * @return The generated password.
     */
    String generateNewPassword();

    /**
     * Removes a game from a user list, removing the scores, playStatues and from the shelves it was on.
     * @param userId
     * @param gameId
     */
    void removeFromList(long userId, long gameId);

}
