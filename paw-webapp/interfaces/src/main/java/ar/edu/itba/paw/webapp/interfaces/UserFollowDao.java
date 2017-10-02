package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.Map;

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
     * @return Whether the user has followed the specified user.
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

    /**
     * Counts the amount of {@link User}s each {@link User} in the given collection is following.
     *
     * @param users The {@link Collection} of {@link User} to count their followings.
     * @return A {@link Map} holding the amount of {@link User}s being followed by each {@link User}.
     */
    Map<User, Long> countFollowing(Collection<User> users);

    /**
     * Counts the amount of {@link User}s following each {@link User} in the given collection.
     *
     * @param users The {@link Collection} of {@link User} to count their amount of followers.
     * @return A {@link Map} holding the amount of {@link User}s following each {@link User}.
     */
    Map<User, Long> countFollowers(Collection<User> users);

    /**
     * Indicates whether the given {@link User} is following the given {@code users}.
     *
     * @param users The {@link User}s that must be checked if the given {@link User} is following.
     * @param user  The {@link User} following (or not) the {@link User}s.
     * @return A {@link Map} holding a flag for each {@link User},
     * which indicates if the given {@link User} is following them, or not
     */
    Map<User, Boolean> following(Collection<User> users, User user);

    /**
     * Indicates whether the given {@link User} is being followed by the given {@code users}.
     *
     * @param users The {@link User}s that must be checked if are following the given {@link User}.
     * @param user  The {@link User} being followed (or not) by the {@link User}s.
     * @return A {@link Map} holding a flag for each {@link User},
     * which indicates if the given {@link User} is being followed by them, or not
     */
    Map<User, Boolean> followedBy(Collection<User> users, User user);
}
