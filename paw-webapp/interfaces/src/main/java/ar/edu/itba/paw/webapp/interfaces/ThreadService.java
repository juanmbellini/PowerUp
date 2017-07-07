package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableEntityWrapper;
import ar.edu.itba.paw.webapp.model_wrappers.ThreadWithLikeCount;
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
     * @param usernameFilter Filter for username.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type (id, game id, or creation date).
     * @param sortDirection  The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<ThreadWithLikeCount> getThreads(String titleFilter, Long userIdFilter, String usernameFilter,
                                         int pageNumber, int pageSize,
                                         ThreadDao.SortingType sortingType, SortDirection sortDirection);

    /**
     * Finds a thread by ID.
     *
     * @param threadId The ID to match.
     * @return The matching thread or {@code null} if not found.
     */
    ThreadWithLikeCount findById(long threadId);

    /**
     * Creates a new {@link Thread} with the specified data.
     *
     * @param title      The thread's title.
     * @param threadBody The thread's body (i.e. initial comment).
     * @param creator    The {@link User} creating the {@link Thread}.
     * @return The created thread.
     * @throws NoSuchEntityException If the creator doesn't exist.
     */
    Thread create(String title, String threadBody, User creator) throws NoSuchEntityException;

    /**
     * Updates the {@link Thread} with the given {@code threadId}.
     *
     * @param threadId   The thread's id.
     * @param title      The new title.
     * @param threadBody The body of the thread.
     * @param updater    The {@link User} performing the operation.
     */
    void update(long threadId, String title, String threadBody, User updater);

    /**
     * Deletes a thread along with all its comments. Only the thread's creator may delete threads.
     *
     * @param threadId The ID of the thread to delete.
     * @param deleter  The {@link User} performing the operation.
     */
    void delete(long threadId, User deleter);


    /**
     * Marks a like for a given thread by a given user, if not already liked.
     *
     * @param threadId The ID of the thread to like.
     * @param liker    The {@link User} performing the operation.
     */
    void likeThread(long threadId, User liker);

    /**
     * Removes a like from a given thread by a given user, if liked.
     *
     * @param threadId The ID of the thread to unlike.
     * @param unliker  The {@link User} performing the operation.
     */
    void unlikeThread(long threadId, User unliker);


    /*
     * Comments
     */


    /**
     * Finds a {@link Page} of top level {@link Comment}s to the given {@link Thread}.
     * Sorting and Pagination can be applied.
     *
     * @param threadId      The commented {@link Thread} ID.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (id, game id, or creation date).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<LikeableEntityWrapper<Comment>> getThreadComments(long threadId, int pageNumber, int pageSize,
                                                           CommentDao.SortingType sortingType,
                                                           SortDirection sortDirection);

    /**
     * Finds a comment by ID.
     *
     * @param commentId The ID to match.
     * @return The matching comment or {@code null} if not found.
     */
    LikeableEntityWrapper<Comment> findCommentById(long commentId);


    /**
     * Adds a top-level comment to a given thread, created by a given user.
     *
     * @param threadId  The ID of the thread to which the {@link Comment} belongs.
     * @param body      The comment's body.
     * @param commenter The {@link User} commenting.
     * @return The created comment.
     */
    Comment comment(long threadId, String body, User commenter);

    /**
     * Edits a comment. Only the original commenter may update their comment.
     *
     * @param commentId The comment ID.
     * @param newBody   The new comment.
     * @param updater   The {@link User} performing the operation.
     */
    void editComment(long commentId, String newBody, User updater);

    /**
     * Deletes a comment along with all its replies. Only the original commenter may delete their comments.
     *
     * @param commentId The ID of the comment to delete.
     * @param deleter   The {@link User} performing the operation.
     * @throws NoSuchEntityException If the comment doesn't exist.
     */
    void deleteComment(long commentId, User deleter) throws NoSuchEntityException;

    /**
     * Adds a reply to a given comment.
     *
     * @param commentId The ID of the comment being replied.
     * @param body      The reply content.
     * @param replier   The {@link User} replying.
     * @return The created reply.
     */
    Comment replyToComment(long commentId, String body, User replier);

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


}