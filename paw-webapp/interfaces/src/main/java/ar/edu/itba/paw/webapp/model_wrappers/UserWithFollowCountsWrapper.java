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
     * Constructor.
     *
     * @param user The {@link User} to wrap.
     * @implNote This method uses {@link UserWithFollowCountsWrapper#UserWithFollowCountsWrapper(User, long, long)},
     * using {@link User#getFollowingCount()} and {@link User#getFollowersCount()}.
     */
    public UserWithFollowCountsWrapper(User user) {
        this(user, user.getFollowingCount(), user.getFollowersCount());
    }


    /**
     * Constructor.
     *
     * @param user           The {@link User} to wrap.
     * @param followingCount The amount of {@link User} being followed by the given {@code user} to wrap.
     * @param followersCount The amount of {@link User} following the given {@code user} to wrap.
     */
    public UserWithFollowCountsWrapper(User user, long followingCount, long followersCount) {
        this.user = user;
        this.followingCount = followingCount;
        this.followersCount = followersCount;
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
}
