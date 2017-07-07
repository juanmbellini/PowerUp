package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;

/**
 * This class wraps {@link Likeable} together with the amount of likes it has.
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
     * Constructor.
     *
     * @param entity    The wrapped {@link Likeable}.
     * @param likeCount The amount of likes.
     */
    public LikeableWrapper(T entity, long likeCount) {
        this.entity = entity;
        this.likeCount = likeCount;
    }

    /**
     * Constructor.
     *
     * @param entity The wrapped {@link Likeable}.
     * @implNote This method uses {@link Likeable#getLikeCount()} method which might trigger an SQL query.
     * If you have the like count use {@link LikeableWrapper#LikeableWrapper(Likeable, long)}.
     */
    public LikeableWrapper(T entity) {
        this(entity, entity.getLikeCount());
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
}
