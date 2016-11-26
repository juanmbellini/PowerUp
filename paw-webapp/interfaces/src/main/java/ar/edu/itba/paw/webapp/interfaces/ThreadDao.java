package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Thread;

import java.util.Set;

/**
 * Data Access Object for {@link Thread Threads}.
 */
public interface ThreadDao {

    /**
     * @see ThreadService#create(String, long, String)
     */
    Thread create(String title, long creatorUserId, String creatorComment) throws NoSuchEntityException;

    /**
     * @see ThreadService#create(String, long)
     */
    default Thread create(String title, long creatorUserId) throws NoSuchEntityException {
        return create(title, creatorUserId, "");
    }

    /**
     * @see ThreadService#findRecent(int)
     */
    Set<Thread> findRecent(int limit);

    /**
     * @see ThreadService#findByUserId(long id)
     */
    Set<Thread> findByUserId(long id);

    /**
     * @see ThreadService#findByTitle(String title)
     */
    Set<Thread> findByTitle(String title);

    /**
     * @see ThreadService#findById(long threadId)
     */
    Thread findById(long threadId);

    /**
     * @see ThreadService#changeTitle(long threadId, String newTitle)
     */
    void changeTitle(long threadId, String newTitle) throws NoSuchEntityException, IllegalArgumentException;

    /**
     * @see ThreadService#likeThread(long threadId, long userId)
     */
    int likeThread(long threadId, long userId);

    /**
     * @see ThreadService#unlikeThread(long threadId, long userId)
     */
    int unlikeThread(long threadId, long userId);

    /**
     * @see ThreadService#comment(long threadId, long commenterId, String comment)
     */
    void comment(long threadId, long commenterId, String comment);

    /**
     * @see ThreadService#replyToComment(long commentId, long commenterId, String reply)
     */
    void replyToComment(long commentId, long replierId, String reply);

    /**
     * @see ThreadService#likeComment(long commentId, long userId)
     */
    int likeComment(long commentId, long userId);

    /**
     * @see ThreadService#unlikeComment(long commentId, long userId)
     */
    int unlikeComment(long commentId, long userId);

    /**
     * @see ThreadService#deleteComment(long commentId)
     */
    void deleteComment(long commentId) throws NoSuchEntityException;

    /**
     * @see ThreadService#deleteThread(long threadId)
     */
    void deleteThread(long threadId) throws NoSuchEntityException;

}