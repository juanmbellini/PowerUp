package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Service layer for game reviews. Exposes functionality available to reviews.
 */
public interface ReviewService {


    /**
     * Returns a {@link Page} with the reviews, applying filters, pagination and sorting.
     *
     * @param gameIdFilter   Filter for game id.
     * @param gameNameFilter Filter for game name.
     * @param userIdFilter   Filter for user id.
     * @param usernameFilter Filter for user name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type (id, game id, or creation date).
     * @param sortDirection  The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<Review> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String usernameFilter,
                            int pageNumber, int pageSize,
                            ReviewDao.SortingType sortingType, SortDirection sortDirection);

    /**
     * Finds a review by ID.
     *
     * @param reviewId The ID to match.
     * @return The matching review or {@code null} if not found.
     */
    Review findById(long reviewId);

    /**
     * Creates a new review with the specified data.
     *
     * @param gameId        The id of the game getting reviewed.
     * @param reviewBody    The textual review.
     * @param storyScore    The story score.
     * @param graphicsScore The graphics score.
     * @param audioScore    The audio score.
     * @param controlsScore The controls score.
     * @param funScore      The fun score.
     * @param reviewer      The {@link User} writing the {@link Review}.
     * @return The created review.
     * @throws NoSuchEntityException If no such user or game exists.
     * @throws ValidationException   If the {@link User} with the given {@code reviewerId}
     *                               has already reviewed the {@link Game} with the given {@code gameId}.
     */
    Review create(Long gameId, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                  Integer controlsScore, Integer funScore, User reviewer)
            throws NoSuchEntityException, ValidationException;


    /**
     * Updates the {@link Review} with the given {@code reviewId}.
     *
     * @param reviewId      The review id.
     * @param reviewBody    The new review body.
     * @param storyScore    The new story score.
     * @param graphicsScore The new graphics score.
     * @param audioScore    The new audio score.
     * @param controlsScore The new controls score.
     * @param funScore      The new fun score.
     * @param updater       The {@link User} updating the {@link Review}.
     */
    void update(long reviewId, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                Integer controlsScore, Integer funScore, User updater);

    /**
     * Deletes {@link Review} with the given {@code reviewId}.
     *
     * @param reviewId The id of the review to be deleted.
     * @param deleter  The {@link User} deleting the {@link Review}.
     */
    void delete(long reviewId, User deleter);


}