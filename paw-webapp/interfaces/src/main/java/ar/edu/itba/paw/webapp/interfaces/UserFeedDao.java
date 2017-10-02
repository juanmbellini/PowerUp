package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.UserGameStatus;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * This interface defines methods to get a given {@link User}'s feed.
 */
public interface UserFeedDao {


    /**
     * Returns a paginated collection of {@link Thread}, according to the given {@link User}.
     * The collection has chronological desc. order
     *
     * @param user       The {@link User} owning the list of {@link Thread} for its feed.
     * @param pageNumber The page number.
     * @param pageSize   The page size.
     * @return The resulting {@link Page}.
     */
    Page<Thread> getThreads(User user, int pageNumber, int pageSize);

    /**
     * Returns a paginated collection of {@link Review}, according to the given {@link User}.
     * The collection has chronological desc. order
     *
     * @param user       The {@link User} owning the list of {@link Thread} for its feed.
     * @param pageNumber The page number.
     * @param pageSize   The page size.
     * @return The resulting {@link Page}.
     */
    Page<Review> getReviews(User user, int pageNumber, int pageSize);


    /**
     * Returns a paginated collection of {@link UserGameStatus}, according to the given {@link User}.
     * The collection has chronological desc. order
     *
     * @param user       The {@link User} owning the list of {@link Thread} for its feed.
     * @param pageNumber The page number.
     * @param pageSize   The page size.
     * @return The resulting {@link Page}.
     */
    Page<UserGameStatus> getPlayStatuses(User user, int pageNumber, int pageSize);
}
