package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.Set;

/**
 * Created by juanlipuma on Nov/7/16.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewDao reviewDao;

    @Override
    public Set<Review> findByGameId(long id) throws NoSuchGameException{
        return reviewDao.findByGameId(id);
    }

    @Override
    public Set<Review> findByGameName(String name) throws NoSuchGameException {
        return reviewDao.findByGameName(name);
    }

    @Override
    public Set<Review> findByUserId(long id) throws NoSuchUserException {
        return reviewDao.findByUserId(id);
    }

    @Override
    public Set<Review> findByUserName(String username) throws NoSuchUserException {
        return reviewDao.findByUserName(username);
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
