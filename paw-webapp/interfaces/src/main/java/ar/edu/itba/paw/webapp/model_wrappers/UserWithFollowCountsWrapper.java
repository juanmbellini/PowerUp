package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserFollow;

/**
 * This class wraps a {@link User} together with the amount of {@link UserFollow} created by and referencing to it
 * (i.e amount of following and followers).
 */
public class UserWithFollowCountsWrapper {

    /**
     * The {@link User} being wrapped.
     */
    private final User user;

    /**
     * The amount of {@link User} being followed by the wrapped {@link User}.
     */
    private final long followingCount;

    /**
     * The amount of {@link User} following by the wrapped {@link User}.
     */
    private final long followersCount;

    /**
     * Indicates whether the current {@link User} is following the wrapped {@link User}.
     */
    private final Boolean followedByCurrentUser;

    /**
     * Indicates whether the current {@link User} is followed the wrapped {@link User}.
     */
    private final Boolean followingCurrentUser;


    /**
     * Constructor.
     *
     * @param user                  The {@link User} to wrap.
     * @param followedByCurrentUser Indicates whether the current {@link User} is following the wrapped {@link User}.
     * @param followingCurrentUser  Indicates whether the current {@link User} is followed the wrapped {@link User}.
     * @implNote This method uses
     * {@link UserWithFollowCountsWrapper#UserWithFollowCountsWrapper(User, Boolean, Boolean)}
     * using {@link User#getFollowingCount()} and {@link User#getFollowersCount()}.
     */
    public UserWithFollowCountsWrapper(User user, Boolean followedByCurrentUser, Boolean followingCurrentUser) {
        this(user, user.getFollowingCount(), user.getFollowersCount(), followedByCurrentUser, followingCurrentUser);
    }


    /**
     * Constructor.
     *
     * @param user                  The {@link User} to wrap.
     * @param followingCount        The amount of {@link User} being followed by the given {@code user} to wrap.
     * @param followersCount        The amount of {@link User} following the given {@code user} to wrap.
     * @param followedByCurrentUser Indicates whether the current {@link User} is following the wrapped {@link User}.
     * @param followingCurrentUser  Indicates whether the current {@link User} is followed the wrapped {@link User}.
     */
    public UserWithFollowCountsWrapper(User user, long followingCount, long followersCount,
                                       Boolean followedByCurrentUser, Boolean followingCurrentUser) {
        this.user = user;
        this.followingCount = followingCount;
        this.followersCount = followersCount;
        this.followedByCurrentUser = followedByCurrentUser;
        this.followingCurrentUser = followingCurrentUser;
    }

    /**
     * @return The {@link User} being wrapped.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return The amount of {@link User} being followed by the wrapped {@link User}.
     */
    public long getFollowingCount() {
        return followingCount;
    }

    /**
     * @return The amount of {@link User} following by the wrapped {@link User}.
     */
    public long getFollowersCount() {
        return followersCount;
    }

    /**
     * @return Indicates whether the current {@link User} is following the wrapped {@link User}.
     */
    public Boolean getFollowedByCurrentUser() {
        return followedByCurrentUser;
    }

    /**
     * @return Indicates whether the current {@link User} is followed the wrapped {@link User}.
     */
    public Boolean getFollowingCurrentUser() {
        return followingCurrentUser;
    }
}
