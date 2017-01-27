package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.ThreadLike Thread likes}.
 */
public interface ThreadLikeDao {

    /**
     * Finds a like for a given thread by a given user.
     *
     * @param threadId The thread ID.
     * @param userId The ID of the user who liked the thread.
     * @return The matching like, or {@code null} if not found.
     */
    ThreadLike find(long threadId, long userId);

    /**
     * Checks whether a given user has liked a given thread.
     *
     * @param threadId The thread ID.
     * @param userId The user ID.
     * @return Whether the user has liked the specified thread.
     */
    boolean exists(long threadId, long userId); // TODO: difference with find(threadId, userId) != null ??

    ThreadLike create(Thread thread, User user);

    void delete(ThreadLike threadLike);

}