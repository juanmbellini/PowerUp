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

    // TODO: javadoc
    void update(Thread thread, String title, String initialComment);

    // TODO: javadoc
    void updateHotValue(Thread thread);

    // TODO: javadoc
    void deleteThread(Thread thread) throws NoSuchEntityException;

    /**
     * @see ThreadService#findRecent(int)
     */
    Set<Thread> findRecent(int limit);

    /**
     * @see ThreadService#findBestPointed(int)
     */
    Set<Thread> findBestPointed(int limit);

    /**
     * @see ThreadService#findHottest(int)
     */
    Set<Thread> findHottest(int limit);

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


}