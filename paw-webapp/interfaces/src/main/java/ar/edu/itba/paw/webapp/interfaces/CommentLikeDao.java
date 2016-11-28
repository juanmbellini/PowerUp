package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.CommentLike;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.CommentLike Comment likes}.
 */
public interface CommentLikeDao {

    /**
     * Checks whether a given user has liked a given comment.
     *
     * @param commentId The comment ID.
     * @param userId The user ID.
     * @return Whether the user has liked the specified comment.
     */
    CommentLike find(long commentId, long userId);

    /**
     * Creates a like for a given comment by a a given user.
     *  @param commentId The comment ID.
     * @param userId The ID of the user liking the comment.
     */
    int create(long commentId, long userId);

    /**
     * Removes a like from a given comment by a a given user.
     *  @param commentId The comment ID.
     * @param userId The ID of the user unliking the comment.
     */
    int delete(long commentId, long userId);
}