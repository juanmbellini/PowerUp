package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.CommentLike;
import ar.edu.itba.paw.webapp.model.User;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.CommentLike}.
 */
public interface CommentLikeDao {

    /**
     * Finds the {@link CommentLike} for the {@link Comment} with the given {@code commentId},
     * done by the {@link User} with the given {@code userId}.
     *
     * @param commentId The comment ID.
     * @param userId    The user ID.
     * @return The {@link CommentLike}, or {@code null} if the {@link User} didn't like the {@link Comment}.
     */
    CommentLike find(long commentId, long userId);

    /**
     * Creates a {@link CommentLike} (i.e. makes the given {@link User} like the given {@link Comment}).
     *
     * @param comment The comment to be liked.
     * @param user    The user liking the comment.
     * @return The created {@link CommentLike}.
     */
    CommentLike create(Comment comment, User user);

    /**
     * Deletes the given {@link CommentLike} (i.e. makes the {@link User} unlike the {@link Comment}).
     *
     * @param commentLike The {@link CommentLike} to be removed.
     */
    void delete(CommentLike commentLike);

}