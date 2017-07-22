package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;

/**
 * This class wraps a {@link Likeable} together with the amount of likes it has.
 */
public class LikeableWrapper<T extends Likeable> {

    /**
     * The wrapped {@link Likeable}
     */
    private final T entity;

    /**
     * The amount of likes.
     */
    private final long likeCount;

    /**
     * Indicates whether the current {@link User} likes the {@code entity}.
     */
    private final Boolean likedByCurrentUser;


    /**
     * Constructor.
     *
     * @param entity             The wrapped {@link Likeable}.
     * @param likeCount          The amount of likes.
     * @param likedByCurrentUser Indicates whether the current {@link User} likes the {@code entity}.
     */
    public LikeableWrapper(T entity, long likeCount, Boolean likedByCurrentUser) {
        this.entity = entity;
        this.likeCount = likeCount;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    /**
     * Constructor.
     *
     * @param entity             The wrapped {@link Likeable}.
     * @param likedByCurrentUser Indicates whether the current {@link User} likes the {@code entity}.
     * @implNote This method uses {@link Likeable#getLikeCount()} method which might trigger an SQL query.
     * If you have the like count use {@link LikeableWrapper#LikeableWrapper(Likeable, long, Boolean)}.
     */
    public LikeableWrapper(T entity, Boolean likedByCurrentUser) {
        this(entity, entity.getLikeCount(), likedByCurrentUser);
    }

    /**
     * @return The wrapped {@link Likeable}.
     */
    public T getEntity() {
        return entity;
    }

    /**
     * The amount of likes.
     */
    public long getLikeCount() {
        return likeCount;
    }

    /**
     * @return Whether the current {@link User} likes the {@code entity}.
     */
    public Boolean getLikedByCurrentUser() {
        return likedByCurrentUser;
    }
}
