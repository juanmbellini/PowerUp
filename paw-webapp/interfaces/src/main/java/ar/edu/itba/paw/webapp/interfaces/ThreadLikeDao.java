package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.ThreadLike;

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
    boolean exists(long threadId, long userId);

    /**
     * Creates a like for a given thread by a a given user, if it doesn't already exist.
     *
     * @param threadId The thread ID.
     * @param userId The ID of the user liking the thread.
     * @return The new like count fo the given thread.
     */
    int create(long threadId, long userId);

    /**
     * Removes a like from a given thread by a a given user, if it find.
     *
     * @param threadId The thread ID.
     * @param userId The ID of the user unliking the thread.
     * @return The new like count fo the given thread.
     */
    int delete(long threadId, long userId);
}