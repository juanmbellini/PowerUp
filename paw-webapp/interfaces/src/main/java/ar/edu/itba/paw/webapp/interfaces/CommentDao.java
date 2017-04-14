package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.Comment Comments}.
 */
public interface CommentDao {


    /**
     * Creates a comment in a {@link Thread}.
     *
     * @param thread         The commented {@link Thread}.
     * @param commenter      The {@link User} commenting.
     * @param commentMessage The comment content.
     * @return
     */
    Comment comment(Thread thread, User commenter, String commentMessage);


    /**
     * Replies a comment (i.e comments a comment). Note that a reply is also a {@link Comment}.
     *
     * @param comment        The {@link Comment} to be replied.
     * @param commenter      The {@link User} replying
     * @param commentMessage The comment content.
     * @return
     */
    Comment reply(Comment comment, User commenter, String commentMessage);


    /**
     * Changes the given {@link Comment}.
     *
     * @param comment    The comment to be changed.
     * @param newComment The new content of the message.
     */
    void update(Comment comment, String newComment);


    /**
     * Removes the given {@link Comment} from the database.
     *
     * @param comment The comment to be removed.
     */
    void delete(Comment comment);

    /**
     * Finds a comment or a reply by ID.
     *
     * @param id The ID.
     * @return The matching comment or reply, or {@code null} if not found.
     */
    Comment findById(long id);

    /**
     * Finds a top-level comment for a given thread by a given user.
     *
     * @param threadId The thread ID.
     * @param userId   The user ID.
     * @return The found comment, or {@code null} if not found.
     */
    Comment findComment(long threadId, long userId);

    /**
     * Finds a reply to a given comment by a given user. Does <b>NOT</b> search recursively down further replies.
     *
     * @param commentId The parent comment ID.
     * @param userId    The user ID.
     * @return The found reply, or {@code null} if not found.
     */
    Comment findReply(long commentId, long userId);

    /**
     * Finds a reply to a given root comment by a given user, searching down the reply tree until the first match is
     * found.
     *
     * @param rootCommentId The root comment ID.
     * @param userId        The user ID.
     * @return The found reply, or {@code null} if not found.
     */
    default Comment deepFindReply(long rootCommentId, long userId) {
        Deque<Comment> queue = new LinkedList<>();
        Comment root = findById(rootCommentId);
        if (root == null) return null;
        queue.addFirst(root);
        while (!queue.isEmpty()) {
            for (Comment reply : root.getReplies()) {
                if (reply.getCommenter().getId() == userId) {
                    return reply;
                }
                //TODO check if this is necessary
//                for(Comment replyReply : reply.getReplies()) {
//                    queue.offer(replyReply);
//                }
                queue.addAll(reply.getReplies());
            }
        }
        return null;
    }

}