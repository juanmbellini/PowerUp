package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.model_interfaces.Commentable;

/**
 * This class wraps a {@link Commentable} together with the amount of comments it has.
 */
public class CommentableWrapper<T extends Commentable> {

    /**
     * The wrapped {@link Commentable}
     */
    private final T entity;

    /**
     * The amount of comments.
     */
    private final long commentCount;


    /**
     * Constructor.
     *
     * @param entity       The wrapped {@link Commentable}.
     * @param commentCount The amount of comments.
     */
    public CommentableWrapper(T entity, long commentCount) {
        this.entity = entity;
        this.commentCount = commentCount;
    }

    /**
     * Constructor.
     *
     * @param entity The wrapped {@link Commentable}.
     * @implNote This method uses {@link Commentable#getCommentCount()} ()} method which might trigger an SQL query.
     * If you have the like count use {@link CommentableWrapper#CommentableWrapper(Commentable, long)}.
     */
    public CommentableWrapper(T entity) {
        this(entity, entity.getCommentCount());
    }

    /**
     * @return The wrapped {@link Commentable}.
     */
    public T getEntity() {
        return entity;
    }

    /**
     * The amount of comments.
     */
    public long getCommentCount() {
        return commentCount;
    }
}
