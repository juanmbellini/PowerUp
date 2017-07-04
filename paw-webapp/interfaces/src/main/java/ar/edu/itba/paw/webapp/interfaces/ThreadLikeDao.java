package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;

import java.util.Collection;
import java.util.Map;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.ThreadLike Thread likes}.
 */
public interface ThreadLikeDao {

    /**
     * Finds a {@link ThreadLike} made by a given {@link User} to a given {@link Thread}
     *
     * @param thread The liked {@link Thread}
     * @param user   The {@link User} that liked the {@link Thread}.
     * @return The matching {@link ThreadLike}, or {@code null} if not present.
     */
    ThreadLike find(Thread thread, User user);

    /**
     * Checks whether a given user has liked a given thread.
     *
     * @param thread The liked {@link Thread}
     * @param user   The {@link User} that liked the {@link Thread}.
     * @return Whether the user has liked the specified thread.
     */
    boolean exists(Thread thread, User user);

    /**
     * Creates a new {@link ThreadLike} (i.e likes a {@link Thread} by a given {@link User}).
     *
     * @param thread The {@link Thread} being liked
     * @param user   The {@link User} liking the {@link Thread}.
     * @return The new {@link ThreadLike}.
     */
    ThreadLike create(Thread thread, User user);

    /**
     * Deletes the given {@link ThreadLike} (i.e unlikes a {@link Thread}).
     *
     * @param threadLike The {@link ThreadLike} representing the {@link Thread} being liked.
     */
    void delete(ThreadLike threadLike);

    /**
     * Counts the amount of likes for each {@link Thread} in the given collection.
     *
     * @param threads The {@link Thread} whose likes must be counted.
     * @return A {@link Map} holding the amount of likes for each {@link Thread}.
     */
    Map<Thread, Long> countLikes(Collection<Thread> threads);
}