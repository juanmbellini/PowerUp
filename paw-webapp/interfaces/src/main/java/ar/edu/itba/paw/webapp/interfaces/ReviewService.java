package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableWrapper;
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
     * @param usernameFilter Filter for username.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type (id, game id, or creation date).
     * @param sortDirection  The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<LikeableWrapper<Review>> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String usernameFilter,
                                             int pageNumber, int pageSize,
                                             ReviewDao.SortingType sortingType, SortDirection sortDirection, User currentUser);

    /**
     * Finds a review by ID.
     *
     * @param reviewId    The ID to match.
     * @param currentUser
     * @return The matching review or {@code null} if not found.
     */
    LikeableWrapper<Review> findById(long reviewId, User currentUser);

    /**
     * Creates a new review with the specified data.
     *
     * @param gameId     The id of the game getting reviewed.
     * @param reviewBody The textual review.
     * @param reviewer   The {@link User} writing the {@link Review}.
     * @return The created review.
     * @throws NoSuchEntityException If no such user or game exists.
     * @throws ValidationException   If the {@link User} with the given {@code reviewerId}
     *                               has already reviewed the {@link Game} with the given {@code gameId}.
     */
    Review create(Long gameId, String reviewBody, User reviewer)
            throws NoSuchEntityException, ValidationException;

    /**
     * Changes the body to a given {@link Review}.
     *
     * @param reviewId The ID of the {@link Review} whose body will be updated.
     * @param newBody  The new body.
     * @param updater  The {@link User} performing the operation.
     */
    void changeReviewBody(long reviewId, String newBody, User updater);

    /**
     * Deletes {@link Review} with the given {@code reviewId}.
     *
     * @param reviewId The id of the review to be deleted.
     * @param deleter  The {@link User} deleting the {@link Review}.
     */
    void delete(long reviewId, User deleter);

    /**
     * Marks a like for a given review by a given user, if not already liked.
     *
     * @param reviewId The ID of the review to like.
     * @param liker    The {@link User} performing the operation.
     */
    void likeReview(long reviewId, User liker);

    /**
     * Removes a like from a given review by a given user, if liked.
     *
     * @param reviewId The ID of the review to unlike.
     * @param unliker  The {@link User} performing the operation.
     */
    void unlikeReview(long reviewId, User unliker);


    /**
     * Returns a {@link Page} of {@link User} liking the {@link Review} with the given {@code reviewId}.
     *
     * @param reviewId      The ID of the {@link Review}.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (id, or creation date).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting {@link Page}.
     */
    Page<User> getUsersLikingTheReview(long reviewId, int pageNumber, int pageSize,
                                       ReviewLikeDao.SortingType sortingType, SortDirection sortDirection);


}