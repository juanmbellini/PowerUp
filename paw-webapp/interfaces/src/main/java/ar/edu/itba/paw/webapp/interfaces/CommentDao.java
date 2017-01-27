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


    // TODO: javadoc
    Comment comment(Thread thread, User commenter, String commentMessage);

    // TODO: javadoc
    Comment reply(Comment comment, User commenter, String commentMessage);

    // TODO: javadoc
    void update(Comment comment, String newComment);

    // TODO: javadoc
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