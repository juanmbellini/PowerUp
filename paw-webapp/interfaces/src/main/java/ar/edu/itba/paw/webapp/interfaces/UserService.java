package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model_wrappers.GameWithUserShelvesWrapper;
import ar.edu.itba.paw.webapp.model_wrappers.UserWithFollowCountsWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.List;

/**
 * User service class. Exposes all functionality available to users.
 */
public interface UserService {


    /**
     * Returns a {@link Page} with the users, applying filters, pagination and sorting.
     *
     * @param usernameFilter  Filter for username.
     * @param emailFilter     Filter for email.
     * @param authorityFilter Filter for authority.
     * @param pageNumber      The page number.
     * @param pageSize        The page size.
     * @param sortingType     The sorting type (id, game id, or creation date).
     * @param sortDirection   The sort direction (i.e ASC or DESC).
     * @param currentUser     The {@link User} performing the operation.
     * @return The resulting page.
     */
    Page<UserWithFollowCountsWrapper> getUsers(String usernameFilter, String emailFilter, Authority authorityFilter,
                                               int pageNumber, int pageSize,
                                               UserDao.SortingType sortingType, SortDirection sortDirection,
                                               User currentUser);


    /**
     * Finds a {@link User} by id.
     *
     * @param id          The user ID.
     * @param currentUser The {@link User} performing the operation.
     * @return The found user or {@code null} if not found.
     */
    UserWithFollowCountsWrapper findById(long id, User currentUser);

    /**
     * Finds a {@link User} by id.
     *
     * @param id The user ID.
     * @return The found user or {@code null} if not found.
     */
    UserWithFollowCountsWrapper findById(long id);

    /**
     * Finds a {@link User} by username.
     *
     * @param username    The username. Case-sensitive.
     * @param currentUser The {@link User} performing the operation.
     * @return The found user or {@code null} if not found.
     */
    UserWithFollowCountsWrapper findByUsername(String username, User currentUser);

    /**
     * Finds a {@link User} by username.
     *
     * @param username The username. Case-sensitive.
     * @return The found user or {@code null} if not found.
     */
    UserWithFollowCountsWrapper findByUsername(String username);

    /**
     * Finds a {@link User} by email.
     *
     * @param email       The email. Case-<strong>in</strong>sensitive.
     * @param currentUser The {@link User} performing the operation.
     * @return The found user or {@code null} if not found.
     */
    UserWithFollowCountsWrapper findByEmail(String email, User currentUser);

    /**
     * Finds a {@link User} by email.
     *
     * @param email The email. Case-<strong>in</strong>sensitive.
     * @return The found user or {@code null} if not found.
     */
    UserWithFollowCountsWrapper findByEmail(String email);


    /**
     * Creates a new {@link User} with the specified data.
     *
     * @param username The user's username. Must be unique.
     * @param email    The user's email. Must be unique.
     * @param password The user's password.
     * @return The created user.
     * @throws UserExistsException If a user with {@code email} or {@code username} already exists.
     */
    User create(String username, String email, String password);

    /**
     * Changes the user's password.
     *
     * @param userId      The user id.
     * @param newPassword The new password.
     * @param updaterId   The id of the user performing the operation.
     */
    void changePassword(long userId, String newPassword, long updaterId);

    /**
     * Changes the user's profile picture.
     *
     * @param userId    The user id.
     * @param picture   The new picture.
     * @param mimeType  The new picture's mime type.
     * @param updaterId The id of the user performing the operation.
     */
    void changeProfilePicture(long userId, byte[] picture, String mimeType, long updaterId);

    /**
     * Removes the profile picture for a user.
     *
     * @param userId    The user id.
     * @param updaterId The id of the user performing the operation.
     */
    void removeProfilePicture(long userId, long updaterId);

    /**
     * Returns the play statuses made by the {@link User} with the given {@code userId},
     * applying filtering, sorting and pagination.
     *
     * @param userId         The user id.
     * @param gameIdFilter   The filter for game id.
     * @param gameNameFilter The filter for game name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type.
     * @param sortDirection  The sort direction.
     * @return The resulting page.
     */
    Page<UserGameStatus> getPlayStatuses(long userId, Long gameIdFilter, String gameNameFilter,
                                         int pageNumber, int pageSize, UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                         SortDirection sortDirection);


    /**
     * Sets or updates a play status for a specified game for a specified user.
     *
     * @param userId    The ID of the user who is setting or updating status.
     * @param gameId    The ID of the game whose status is being registered.
     * @param status    The status.
     * @param updaterId The id of the user performing the operation.
     */
    void setPlayStatus(long userId, Long gameId, PlayStatus status, long updaterId);

    /**
     * Removes status from user u to game id.
     *
     * @param userId    The ID of the user who is getting a gameStatus removed
     * @param gameId    The ID of the game whose gameStatus is getting removed
     * @param updaterId The id of the user performing the operation.
     */
    void removePlayStatus(long userId, Long gameId, long updaterId);


    /**
     * Returns the scores made by the {@link User} with the given {@code userId},
     * applying filtering, sorting and pagination.
     *
     * @param userId         The user id.
     * @param gameIdFilter   The filter for game id.
     * @param gameNameFilter The filter for game name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type.
     * @param sortDirection  The sort direction.
     * @return The resulting page.
     */
    Page<UserGameScore> getGameScores(long userId, Long gameIdFilter, String gameNameFilter,
                                      int pageNumber, int pageSize, UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                      SortDirection sortDirection);


    /**
     * Sets or updates a score for the {@link Game} with the given {@code gameId},
     * for the {@link User} with the given {@code userId}
     *
     * @param userId    The user id.
     * @param gameId    The scored game's id.
     * @param score     The score.
     * @param updaterId The id of the user performing the operation.
     */
    void setGameScore(long userId, long gameId, Integer score, long updaterId);

    /**
     * Removes the score for the {@link Game} with the given {@code gameId},
     * for the {@link User} with the given {@code userId}
     *
     * @param userId    The user id.
     * @param gameId    The scored game's id.
     * @param updaterId The id of the user performing the operation.
     */
    void removeGameScore(long userId, long gameId, long updaterId);


    /**
     * Adds an {@link Authority} to the user with the given {@code userId}.
     *
     * @param userId    The user's id.
     * @param authority The authority.
     * @param updaterId The id of the user performing the operation.
     */
    void addAuthority(long userId, Authority authority, long updaterId);

    /**
     * Removes an {@link Authority} to the user with the given {@code userId}.
     *
     * @param userId    The user's id.
     * @param authority The authority.
     * @param updaterId The id of the user performing the operation.
     */
    void removeAuthority(long userId, Authority authority, long updaterId);

    /**
     * Removes the {@link User} with the given {@code userId}.
     *
     * @param userId    The user id.
     * @param deleterId THe id of the user performing the operation.
     */
    void delete(long userId, long deleterId);

    /**
     * Recommends games for user based on the scores of the games he has scored
     *
     * @param userId The ID of the user who is getting the recommendations
     */
    Collection<Game> recommendGames(long userId);

    /**
     * Recommends games for user based on the scores of the games he has scored for the shelf selected
     *
     * @param userId           The id of the {@link User} to which the recommendation must be done.
     * @param shelfNameFilters A {@link List} of names of {@link Shelf} to be included in the recommendation process.
     * @return A {@link Collection} of recommended {@link Game}s.
     */
    Collection<Game> recommendGames(long userId, List<String> shelfNameFilters);

    /**
     * Returns a {@link User}'s list (i.e games in a {@link Shelf} or with {@link PlayStatus}).
     * Filtering and sorting can be applied.
     *
     * @param userId        The {@link User} owning the list's id.
     * @param shelfNames    A {@link List} of names of {@link Shelf} to apply filtering.
     * @param statuses      A {@link List} of {@link PlayStatus} to apply filtering.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type.
     * @param sortDirection The sort direction.
     * @return The resulting page.
     */
    Page<GameWithUserShelvesWrapper> getGameList(long userId, List<String> shelfNames, List<PlayStatus> statuses,
                                                 int pageNumber, int pageSize,
                                                 UserDao.ListGameSortingType sortingType, SortDirection sortDirection);


    /**
     * Returns a new randomly generated password.
     *
     * @return The generated password.
     */
    String generateNewPassword();

    /**
     * Returns a paginated collection of {@link User} being followed by the {@link User} with the given {@code userId}.
     *
     * @param userId        The id of {@link User} being whose list of {@link User} being followed must be returned.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortDirection The sort direction.
     * @return The resulting page.
     */
    Page<User> getFollowing(long userId, int pageNumber, int pageSize, SortDirection sortDirection);

    /**
     * Makes the given {@code follower} {@link User} follow the {@link User} with the given {@code followedId}.
     *
     * @param followedId The id of the {@link User} being followed.
     * @param follower   The {@link User} performing the operation
     *                   (i.e the one following the {@link User} with the given {@code followedId}).
     */
    void followUser(long followedId, User follower);

    /**
     * Makes the given {@code unFollower} {@link User} unfollow the {@link User} with the given {@code unFollowedId}.
     *
     * @param unFollowedId The id of the {@link User} being unfollowed.
     * @param unFollower   The {@link User} performing the operation
     *                     (i.e the one unfollowing the {@link User} with the given {@code unFollowedId}).
     */
    void unFollowUser(long unFollowedId, User unFollower);

    /**
     * Returns a paginated collection of {@link User} following the {@link User} with the given {@code userId}.
     *
     * @param userId        The id of {@link User} being whose list of {@link User} following it must be returned.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortDirection The sort direction.
     * @return The resulting page.
     */
    Page<User> getFollowers(long userId, int pageNumber, int pageSize, SortDirection sortDirection);

//    /**
//     * Change the user's password with a new randomly generated one.
//     * @param id The ID of the user whose password to reset.
//     * @return The generated password.
//     */
//    void resetPassword(long id);
}
