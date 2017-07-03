package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.Thread;

/**
 * This class wraps a {@link Thread} together with the amount of likes it has.
 */
public class ThreadWithLikesCount {

    /**
     * The wrapped {@link Thread}
     */
    private final Thread thread;

    /**
     * The amount of likes.
     */
    private final long likesCount;


    /**
     * Constructor.
     *
     * @param thread     The wrapped {@link Thread}.
     * @param likesCount The amount of likes.
     */
    public ThreadWithLikesCount(Thread thread, long likesCount) {
        this.thread = thread;
        this.likesCount = likesCount;
    }

    /**
     * @return The wrapped {@link Thread}
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * The amount of likes.
     */
    public long getLikesCount() {
        return likesCount;
    }
}
