package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Service layer for {@link ar.edu.itba.paw.webapp.model.Thread Threads}. Exposes functionality available to threads.
 */
public interface ThreadService {


    /**
     * Returns a {@link Page} with the threads, applying filters, pagination and sorting.
     *
     * @param titleFilter    Filter for the title.
     * @param userIdFilter   Filter for user id.
     * @param usernameFilter Filter for user name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type (id, game id, or creation date).
     * @param sortDirection  The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<Thread> getThreads(String titleFilter, Long userIdFilter, String usernameFilter, int pageNumber, int pageSize,
                            ThreadDao.SortingType sortingType, SortDirection sortDirection);

    /**
     * Creates a new {@link Thread} with the specified data.
     *
     * @param title         The thread's title.
     * @param creatorUserId The user's id.
     * @param threadBody    The thread's body (i.e. initial comment).
     * @return The created thread.
     * @throws NoSuchEntityException If the creator doesn't exist.
     */
    Thread create(String title, long creatorUserId, String threadBody) throws NoSuchEntityException;

    /**
     * Updates the {@link Thread} with the given {@code threadId}.
     *
     * @param threadId   The thread's id.
     * @param title      The new title.
     * @param threadBody The body of the thread.
     * @param userId     The id of the user performing the operation.
     */
    void update(long threadId, String title, String threadBody, long userId);

    /**
     * Deletes a thread along with all its comments. Only the thread's creator may delete threads.
     *
     * @param threadId The ID of the thread to delete.
     * @param userId   The ID of the user attempting to delete the thread.
     * @throws NoSuchEntityException If the thread doesn't exist.
     * @throws UnauthorizedException If the user isn't allowed to delete the thread.
     */
    void delete(long threadId, long userId) throws NoSuchEntityException, UnauthorizedException;


    /**
     * Finds a thread by ID.
     *
     * @param threadId The ID to match.
     * @return The matching thread or {@code null} if not found.
     */
    Thread findById(long threadId);

    /**
     * Marks a like for a given thread by a given user, if not already liked.
     *
     * @param threadId The ID of the thread to like.
     * @param userId   The ID of the user liking the thread.
     */
    void likeThread(long threadId, long userId);

    /**
     * Removes a like from a given thread by a given user, if liked.
     *
     * @param threadId The ID of the thread to unlike.
     * @param userId   The ID of the user unliking the thread.
     */
    void unlikeThread(long threadId, long userId);


    /*
     * Comments
     */

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
     * @param commentId The ID of the comment to be liked.
     * @param userId    The ID of the user liking the comment.
     */
    void likeComment(long commentId, long userId);

    /**
     * Removes a like from a given comment or reply by a given user, if liked.
     *
     * @param commentId The ID of the comment to be unliked.
     * @param userId    The ID of the user unliking the comment.
     */
    void unlikeComment(long commentId, long userId);

    /**
     * Edits a comment. Only the original commenter may update their comment.
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
     * @param userId    The ID of the user attempting to delete the comment.
     * @throws NoSuchEntityException If the comment doesn't exist.
     */
    void deleteComment(long commentId, long userId) throws NoSuchEntityException;


}