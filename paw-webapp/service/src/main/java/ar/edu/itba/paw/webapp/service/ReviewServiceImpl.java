package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewService;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of the review service.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService, ValidationExceptionThrower {

    private final ReviewDao reviewDao;

    private final GameDao gameDao;


    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, GameDao gameDao) {
        this.reviewDao = reviewDao;
        this.gameDao = gameDao;
    }


    @Override
    public Page<Review> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String userNameFilter,
                                   int pageNumber, int pageSize,
                                   ReviewDao.SortingType sortingType, SortDirection sortDirection) {
        return reviewDao.getReviews(gameIdFilter, gameNameFilter, userIdFilter, userNameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
    }

    @Override
    public Review findById(long reviewId) {
        return reviewDao.findById(reviewId);
    }

    @Override
    public Review create(Long gameId, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                         Integer controlsScore, Integer funScore, User reviewer)
            throws NoSuchEntityException, ValidationException {

        // If no game id is sent, just call create, which will throw a ValidationException with all errors.
        if (gameId != null) {
            // TODO: create method for checking existence
            if (!reviewDao.getReviews(gameId, null, reviewer.getId(), null, 1, 1, ReviewDao.SortingType.GAME_ID,
                    SortDirection.ASC).getData().isEmpty()) {
                throwValidationException(Stream.of(GAME_ALREADY_REVIEWED_BY_USER).collect(Collectors.toList()));
            }
        }
        return reviewDao.create(reviewer, gameId == null ? null : gameDao.findById(gameId),
                reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore);
    }

    @Override
    public void update(long reviewId, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                       Integer controlsScore, Integer funScore, User updater) {
        reviewDao.update(checkReviewValuesAndAuthoring(reviewId, updater),
                reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore);
    }

    @Override
    public void delete(long reviewId, User deleter) {
        reviewDao.delete(checkReviewValuesAndAuthoring(reviewId, deleter));
    }




    /*
     * Helpers
     */

    /**
     * Checks if the values are correct (including authoring).
     * Upon success,  it returns the {@link Review} with the given {@code reviewId}. Otherwise, an exception is thrown.
     *
     * @param reviewId The review id.
     * @param user     The user.
     * @return The review with the given {@code reviewId}.
     * @throws NoSuchEntityException If no {@link Review} exists with the given {@code reviewId}.
     * @throws UnauthorizedException If the given {@link User} is not the creator
     *                               of the {@link Review} with the given {@code reviewId}.
     */
    private Review checkReviewValuesAndAuthoring(long reviewId, User user) throws NoSuchEntityException,
            UnauthorizedException {
        Review review = reviewDao.findById(reviewId);
        if (review == null) {
            throw new NoSuchEntityException(Collections.singletonList("reviewId"));
        }
        final long userId = user.getId();
        if (userId != review.getUser().getId()) {
            throw new UnauthorizedException("Review #" + reviewId + " does not belong to user #" + userId);
        }
        return review;
    }


    private static final ValueError GAME_ALREADY_REVIEWED_BY_USER =
            new ValueError(ValueError.ErrorCause.ALREADY_EXISTS,
                    "(reviewerId, gameId)", "That user already reviewed that game");
}
