package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.ReviewDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by juanlipuma on Nov/7/16.
 */
@Repository
public class ReviewHibernateDao implements ReviewDao {

    //TODO fix repeated code, extract some finding functionality to a generalized method if possible

    @PersistenceContext
    private EntityManager em;

    private final GameDao gameDao;

    private final UserDao userDao;

    @Autowired
    private ReviewHibernateDao(GameDao gameDao, UserDao userDao) {
        //For Hibernate
        this.gameDao = gameDao;
        this.userDao = userDao;
    }

    @Override
    public Review create(long reviewerId, long gameId, String review, int storyScore, int graphicsScore, int audioScore, int controlsScore, int funScore) throws NoSuchEntityException {
        User user = userDao.findById(reviewerId);
        if(user == null) {
            throw new NoSuchUserException(reviewerId);
        }
        Game game = gameDao.findById(gameId);
        if(game == null) {
            throw new NoSuchGameException(gameId);
        }
        final Review reviewObj = new Review(user, game, review, storyScore, graphicsScore, audioScore, controlsScore, funScore);
        em.persist(reviewObj);
        return reviewObj;
    }

    @Override
    public Set<Review> findByGameId(long id) {
        if(!gameDao.existsWithId(id)) {
            throw new NoSuchGameException(id);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.game.id = :id", Review.class);
        baseQuery.setParameter("id", id);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Review> findRecentByGameId(long id, int limit) throws NoSuchGameException {
        if(!gameDao.existsWithId(id)) {
            throw new NoSuchGameException(id);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.game.id = :id ORDER BY R.date DESC", Review.class);
        baseQuery.setParameter("id", id);
        baseQuery.setMaxResults(limit);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Review> findByUserId(long id) {
        if(!userDao.existsWithId(id)) {
            throw new NoSuchUserException(id);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.user.id = :id", Review.class);
        baseQuery.setParameter("id", id);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Review> findRecentByUserId(long id, int limit) throws NoSuchUserException {
        if(!userDao.existsWithId(id)) {
            throw new NoSuchUserException(id);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.user.id = :id ORDER BY R.date DESC", Review.class);
        baseQuery.setParameter("id", id);
        baseQuery.setMaxResults(limit);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Review> findByUsername(String name) {
        if(!userDao.existsWithUsername(name)) {
            throw new NoSuchUserException(name);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.user.username = :name", Review.class);
        baseQuery.setParameter("name", name);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Review> findRecentByUsername(String username, int limit) throws NoSuchUserException {
        if(!userDao.existsWithUsername(username)) {
            throw new NoSuchUserException(username);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.user.username = :username ORDER BY R.date DESC", Review.class);
        baseQuery.setParameter("username", username);
        baseQuery.setMaxResults(limit);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Review findById(long reviewId) {
        return em.find(Review.class, reviewId);
    }

    @Override
    public Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException {
        if(!userDao.existsWithId(userId)) {
            throw new NoSuchUserException(userId);
        }
        if(!gameDao.existsWithId(gameId)) {
            throw new NoSuchGameException(gameId);
        }
        TypedQuery<Review> baseQuery = em.createQuery("FROM Review AS R where R.game.id = :game_id AND R.user.id = :user_id", Review.class);
        baseQuery.setParameter("game_id", gameId);
        baseQuery.setParameter("user_id", userId);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }
}
