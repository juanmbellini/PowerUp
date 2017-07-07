package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.CommentLike;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Collection;
import java.util.Map;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.CommentLike}.
 */
public interface CommentLikeDao {

    /**
     * Finds a {@link CommentLike} made by a given {@link User} to a given {@link Comment}
     *
     * @param comment The liked {@link Comment}
     * @param user    The {@link User} that liked the {@link Comment}.
     * @return The matching {@link CommentLike}, or {@code null} if not present.
     */
    CommentLike find(Comment comment, User user);

    /**
     * Checks whether a given {@link User} has liked a given {@link Comment}.
     *
     * @param comment The liked {@link Comment}
     * @param user    The {@link User} that liked the {@link Comment}.
     * @return Whether the {@link User} has liked the specified {@link Comment}.
     */
    boolean exists(Comment comment, User user);

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

    /**
     * Counts the amount of likes for each {@link Comment} in the given collection.
     *
     * @param comments The {@link Comment} whose likes must be counted.
     * @return A {@link Map} holding the amount of likes for each {@link Comment}.
     */
    Map<Comment, Long> countLikes(Collection<Comment> comments);
}