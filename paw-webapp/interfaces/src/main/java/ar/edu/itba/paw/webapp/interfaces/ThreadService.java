package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;

import java.util.Set;

/**
 * Service layer for {@link ar.edu.itba.paw.webapp.model.Thread Threads}. Exposes functionality available to threads.
 */
public interface ThreadService {

    /**
     * Creates a thread with a given name, created by a given user, with a given initial comment.
     *
     * @param title          The thread's title.
     * @param creatorUserId  The user ID of the creator of the thread.
     * @param creatorComment The creator's initial thread comment, to start discussion.
     * @return The created thread.
     * @throws NoSuchEntityException If the creator doesn't exist.
     */
    Thread create(String title, long creatorUserId, String creatorComment) throws NoSuchEntityException;

    /**
     * Creates a thread with an empty initial comment.
     *
     * @see #create(String, long, String)
     */
    default Thread create(String title, long creatorUserId) throws NoSuchEntityException {
        return create(title, creatorUserId, "");
    }

    /**
     * Finds up to a specified amount of recently updated threads.
     *
     * @param limit The maximum number of threads to get.
     * @return Up to {@code limit} threads.
     */
    Set<Thread> findRecent(int limit);

    /**
     * Returns a set of shelves created by a specified user, identified by ID.
     *
     * @param id The ID of the user whose shelves to fetch.
     * @return The resulting set of shelves.
     */
    Set<Thread> findByUserId(long id);

    /**
     * Finds threads by title.
     *
     * @param title The thread title. Case <b>in</b>sensitive.
     * @return The matching threads.
     */
    Set<Thread> findByTitle(String title);

    /**
     * Finds a thread by ID.
     *
     * @param threadId The ID to match.
     * @return The matching thread or {@code null} if not found.
     */
    Thread findById(long threadId);

    /**
     * Changes the title of a thread.
     *
     * @param threadId The ID of the thread whose title to change.
     * @param newTitle The thread's new title.
     * @throws NoSuchEntityException    If the thread doesn't exist.
     * @throws IllegalArgumentException If the name is null or invalid (e.g. empty).
     */
    void changeTitle(long threadId, String newTitle) throws NoSuchEntityException, IllegalArgumentException;

    /**
     * Marks a like for a given thread by a given user, if not already liked.
     *
     * @param threadId The ID of the thread to like.
     * @param userId   The ID of the user liking the thread.
     * @return The new like count.
     */
    int likeThread(long threadId, long userId);

    /**
     * Removes a like from a given thread by a given user, if liked.
     *
     * @param threadId The ID of the thread to unlike.
     * @param userId   The ID of the user unliking the thread.
     * @return The new like count.
     */
    int unlikeThread(long threadId, long userId);

    /**
     * Adds a top-level comment to a given thread, created by a given user.
     *
     * @param threadId    The ID of the thread to which the comment belongs.
     * @param commenterId The ID of the commenter.
     * @param comment     The comment.
     * @return The created comment.
     */
    Comment comment(long threadId, long commenterId, String comment);

    /**
     * Adds a reply to a given comment, created by a given user.
     *
     * @param commentId The ID of the comment being replied.
     * @param replierId The ID of the replier.
     * @param reply     The reply.
     * @return The created reply.
     */
    Comment replyToComment(long commentId, long replierId, String reply);

    /**
     * Marks a like for a given comment or reply by a given user, if not already liked.
     *
     * @param id     The ID of the comment or reply to like.
     * @param userId The ID of the user liking the comment.
     * @return The new like count.
     */
    int likeComment(long id, long userId);

    /**
     * Removes a like from a given comment or reply by a given user, if liked.
     *
     * @param id     The ID of the comment or reply to unlike.
     * @param userId The ID of the user unliking the comment.
     * @return The new like count.
     */
    int unlikeComment(long id, long userId);

    /**
     * Edits a comment. Only the original commenter may edit their comment.
     *
     * @param commentId  The comment ID.
     * @param userId     The commenter ID.
     * @param newComment The new comment.
     */
    void editComment(long commentId, long userId, String newComment);

    /**
     * Deletes a comment along with all its replies. Only the original commenter may delete their comments.
     *
     * @param commentId The ID of the comment to delete.
     * @param userId The ID of the user attempting to delete the comment.
     * @throws NoSuchEntityException If the comment doesn't exist.
     */
    void deleteComment(long commentId, long userId) throws NoSuchEntityException;

    /**
     * Deletes a thread along with all its comments.
     *
     * @param threadId The ID of the thread to delete.
     * @throws NoSuchEntityException If the thread doesn't exist.
     */
    void deleteThread(long threadId) throws NoSuchEntityException;
}