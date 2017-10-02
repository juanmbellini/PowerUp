package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.model_interfaces.Commentable;
import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;

import java.util.Optional;

/**
 * This class wraps a {@link Commentable} and {@link Likeable} entity,
 * together with the amount of comments  and likes it has.
 */
public class CommentableAndLikeableWrapper<T extends Likeable & Commentable> {

    /**
     * The wrapped entity.
     */
    private final T entity;

    /**
     * The {@link CommentableWrapper} to wrap the entity together with the comment count.
     */
    private final CommentableWrapper commentableWrapper;

    /**
     * The {@link LikeableWrapper} to wrap the entity together with the like count.
     */
    private final LikeableWrapper likeableWrapper;

    /**
     * Constructor.
     *
     * @param entity             The entity to be wrapped.
     * @param commentCount       The amount of comments to be wrapped.
     * @param likeCount          The amount of likes to be wrapped.
     * @param likedByCurrentUser Indicates whether the current {@link User} likes the {@code entity}.
     */
    private CommentableAndLikeableWrapper(T entity, Long commentCount, Long likeCount, Boolean likedByCurrentUser) {
        this.entity = entity;
        this.commentableWrapper = Optional.ofNullable(commentCount)
                .map(count -> new CommentableWrapper<>(entity, count))
                .orElse(new CommentableWrapper<>(entity));
        this.likeableWrapper = Optional.ofNullable(likeCount)
                .map(count -> new LikeableWrapper<>(entity, count, likedByCurrentUser))
                .orElse(new LikeableWrapper<>(entity, likedByCurrentUser));
    }

    /**
     * @return The wrapped entity.
     */
    public T getEntity() {
        return entity;
    }

    /**
     * @return The amount of comments.
     */
    public long getCommentCount() {
        return commentableWrapper.getCommentCount();
    }

    /**
     * @return The amount of likes.
     */
    public long getLikeCount() {
        return likeableWrapper.getLikeCount();
    }

    /**
     * @return Whether the current {@link User} likes the {@code entity}.
     */
    public Boolean getLikedByCurrentUser() {
        return likeableWrapper.getLikedByCurrentUser();
    }


    /**
     * Builder class for a {@link CommentableAndLikeableWrapper}.
     *
     * @param <E>
     */
    public static class Builder<E extends Likeable & Commentable> {

        /**
         * The entity to be wrapped
         */
        private E entity;

        /**
         * The amount of comments to be wrapped.
         */
        private Long commentCount;

        /**
         * The amount of likes to be wrapped.
         */
        private Long likeCount;

        /**
         * Indicates whether the current {@link User} likes the {@code entity}.
         */
        private Boolean likedByCurrentUser;

        /**
         * Constructor.
         */
        public Builder() {
            this.clear();
        }

        /**
         * Sets the entity to be wrapped
         *
         * @param entity The entity to be wrapped
         * @return this (for method chaining).
         */
        public Builder<E> setEntity(E entity) {
            this.entity = entity;
            return this;
        }

        /**
         * Sets the amount of comments to be wrapped.
         *
         * @param commentCount the amount of comments to be wrapped.
         *                     If {@code null}, {@link Commentable#getCommentCount()} will be used instead,
         *                     which may trigger a SQL query.
         * @return this (for method chaining).
         */
        public Builder<E> setCommentCount(long commentCount) {
            this.commentCount = commentCount;
            return this;
        }

        /**
         * Sets the amount of likes to be wrapped.
         *
         * @param likeCount the amount of likes to be wrapped.
         *                  If {@code null}, {@link Likeable#getLikeCount()} ()} will be used instead,
         *                  which may trigger a SQL query.
         * @return this (for method chaining).
         */
        public Builder<E> setLikeCount(long likeCount) {
            this.likeCount = likeCount;
            return this;
        }

        /**
         * Sets the flag that indicates whether the current {@link User} likes the {@code entity}.
         *
         * @param likedByCurrentUser Whether the current {@link User} likes the {@code entity}.
         * @return this (for method chaining).
         */
        public Builder<E> setLikedByCurrentUser(Boolean likedByCurrentUser) {
            this.likedByCurrentUser = likedByCurrentUser;
            return this;
        }

        /**
         * Builds the new {@link CommentableAndLikeableWrapper} according to the set values.
         *
         * @return The new {@link CommentableAndLikeableWrapper}.
         */
        public CommentableAndLikeableWrapper<E> build() {
            return new CommentableAndLikeableWrapper<>(entity, commentCount, likeCount, likedByCurrentUser);
        }

        /**
         * Clears the {@link Builder}.
         *
         * @return this (for method chaining).
         */
        public Builder<E> clear() {
            this.entity = null;
            this.commentCount = null;
            this.likeCount = null;
            this.likedByCurrentUser = null;
            return this;
        }
    }
}
