package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of the review service.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;

    private final UserDao userDao;

    private final GameDao gameDao;


    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, UserDao userDao, GameDao gameDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
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
    public Review create(long reviewerId, long gameId,
                         String reviewBody, int storyScore, int graphicsScore, int audioScore,
                         int controlsScore, int funScore) throws NoSuchEntityException {
        return reviewDao.create(userDao.findById(reviewerId), gameDao.findById(gameId),
                reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore);
    }

    @Override
    public void update(long reviewId, long updaterUserId,
                       String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                       Integer controlsScore, Integer funScore) {
        reviewDao.update(checkReviewValuesAndAuthoring(reviewId, updaterUserId),
                reviewBody, storyScore, graphicsScore, audioScore, controlsScore, funScore);
    }

    @Override
    public void delete(long reviewId, long deleterUserId) {
        reviewDao.delete(checkReviewValuesAndAuthoring(reviewId, deleterUserId));
    }


    @Override
    @Deprecated
    public Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException {
        return reviewDao.find(userId, gameId);
    }





    /*
     * Helpers
     */

    /**
     * Checks if the values are correct (including authoring).
     * Upon success,  it returns the {@link Review} with the given {@code reviewId}. Otherwise, an exception is thrown.
     *
     * @param reviewId The review id.
     * @param userId   The user id.
     * @return The review with the given {@code reviewId}.
     */
    private Review checkReviewValuesAndAuthoring(long reviewId, long userId) {
        Review review = checkReviewValues(reviewId, userId);
        if (userId != review.getUser().getId()) {
            throw new UnauthorizedException("Review #" + reviewId + " does not belong to user #" + userId);
        }
        return review;
    }


    /**
     * Checks if the values are correct (without authoring).
     * Upon success, it returns the {@link Review} with the given {@code reviewId}. Otherwise, an exception is thrown.
     *
     * @param reviewId The review id.
     * @param userId   The user id.
     * @return The review with the given {@code reviewId}
     */
    private Review checkReviewValues(long reviewId, long userId) {
        Review review = reviewDao.findById(reviewId);
        List<String> missings = new LinkedList<>();
        if (review == null) {
            missings.add("reviewId");
        }
        if (userDao.findById(userId) == null) {
            missings.add("userId");
        }
        if (!missings.isEmpty()) {
            throw new NoSuchEntityException(missings);
        }
        return review;
    }
}
