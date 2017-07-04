package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.Thread;

/**
 * This class wraps a {@link Thread} together with the amount of likes it has.
 */
public class ThreadWithLikeCount {

    /**
     * The wrapped {@link Thread}
     */
    private final Thread thread;

    /**
     * The amount of likes.
     */
    private final long likeCount;


    /**
     * Constructor.
     *
     * @param thread     The wrapped {@link Thread}.
     * @param likeCount The amount of likes.
     */
    public ThreadWithLikeCount(Thread thread, long likeCount) {
        this.thread = thread;
        this.likeCount = likeCount;
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
    public long getLikeCount() {
        return likeCount;
    }
}
