package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.model_interfaces.LikeableEntity;

/**
 * This class wraps {@link LikeableEntity} together with the amount of likes it has.
 */
public class LikeableEntityWrapper<T extends LikeableEntity> {

    /**
     * The wrapped {@link LikeableEntity}
     */
    private final T entity;

    /**
     * The amount of likes.
     */
    private final long likeCount;


    /**
     * Constructor.
     *
     * @param entity    The wrapped {@link LikeableEntity}.
     * @param likeCount The amount of likes.
     */
    public LikeableEntityWrapper(T entity, long likeCount) {
        this.entity = entity;
        this.likeCount = likeCount;
    }

    /**
     * Constructor.
     *
     * @param entity The wrapped {@link LikeableEntity}.
     * @implNote This method uses {@link LikeableEntity#getLikeCount()} method which might trigger an SQL query.
     * If you have the like count use {@link LikeableEntityWrapper#LikeableEntityWrapper(LikeableEntity, long)}.
     */
    public LikeableEntityWrapper(T entity) {
        this(entity, entity.getLikeCount());
    }

    /**
     * @return The wrapped {@link LikeableEntity}.
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
