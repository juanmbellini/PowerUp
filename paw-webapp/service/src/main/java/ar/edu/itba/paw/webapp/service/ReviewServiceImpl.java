package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewService;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Implementation of the review service.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public Review create(long reviewerId, long gameId, String review, int storyScore, int graphicsScore, int audioScore, int controlsScore, int funScore) throws NoSuchEntityException {
        return reviewDao.create(reviewerId, gameId, review, storyScore, graphicsScore, audioScore, controlsScore, funScore);
    }

    @Override
    public Set<Review> findByGameId(long id) throws NoSuchGameException{
        return reviewDao.findByGameId(id);
    }

    @Override
    public Page<Review> findPageByGameId(long id, int pageNumber, int pageSize) throws NoSuchGameException {
        return reviewDao.findPageByGameId(id, pageNumber, pageSize);
    }

    @Override
    public Set<Review> findRecentByGameId(long id, int limit) throws NoSuchGameException {
        return reviewDao.findRecentByGameId(id, limit);
    }

    @Override
    public Set<Review> findByUserId(long id) throws NoSuchUserException {
        return reviewDao.findByUserId(id);
    }

    @Override
    public Page<Review> findPageByUserId(long id, int pageNumber, int pageSize) throws NoSuchGameException {
        return reviewDao.findPageByUserId(id, pageNumber, pageSize);
    }

    @Override
    public Set<Review> findRecentByUserId(long id, int limit) throws NoSuchUserException {
        return reviewDao.findRecentByUserId(id, limit);
    }

    @Override
    public Set<Review> findByUsername(String username) throws NoSuchUserException {
        return reviewDao.findByUsername(username);
    }

    @Override
    public Set<Review> findRecentByUsername(String username, int limit) throws NoSuchUserException {
        return reviewDao.findRecentByUsername(username, limit);
    }

    @Override
    public Review findById(long reviewId) {
        return reviewDao.findById(reviewId);
    }

    @Override
    public Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException {
        return reviewDao.find(userId, gameId);
    }
}
