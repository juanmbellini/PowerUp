package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * This class represents a follow from a {@link User} to another {@link User}.
 */
public interface UserFollowDao {

    /**
     * Returns a paginated collection of {@link User} being followed by the given {@link User}.
     *
     * @param user          The {@link User} being whose list of {@link User} being followed must be returned.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortDirection The sort direction.
     * @return The resulting page.
     */
    Page<UserFollow> getFollowing(User user, int pageNumber, int pageSize, SortDirection sortDirection);

    /**
     * Returns a paginated collection of {@link User} following the given {@link User}.
     *
     * @param user          The {@link User} being whose list of {@link User} following it must be returned.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortDirection The sort direction.
     * @return The resulting page.
     */
    Page<UserFollow> getFollowers(User user, int pageNumber, int pageSize, SortDirection sortDirection);

    /**
     * Finds a UserFollow made by a given User to a given User
     *
     * @param following The User that followed the User.
     * @param followed  The followed User
     * @return The matching UserFollow, or {@code null} if not present.
     */
    UserFollow find(User following, User followed);

    /**
     * Checks whether a given user has followed a given user.
     *
     * @param following The User that followed the User.
     * @param followed  The followed User
     * @return Whether the user has followed the specified thread.
     */
    boolean exists(User following, User followed);

    /**
     * Creates a new UserFollow (i.e likes a {@link User} by a given {@link User}).
     *
     * @param followed The User being followed.
     * @param follower The {@link User} that is following the other {@link User} (i.e the one performing the operation).
     * @return The new UserFollow.
     */
    UserFollow create(User followed, User follower);

    /**
     * Deletes the given UserFollow (i.e unfollows a User).
     *
     * @param UserFollow The {@link UserFollow} representing the relation of following.
     */
    void delete(UserFollow UserFollow);
}
