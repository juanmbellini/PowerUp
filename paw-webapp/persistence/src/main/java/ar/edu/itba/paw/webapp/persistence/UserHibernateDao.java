package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Authority;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by julrodriguez on 28/10/16.
 */
@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    private final GameDao gameDao;

    @Autowired
    public UserHibernateDao(GameDao gameDao) {
        this.gameDao = gameDao;
    }

    @Override
    public User create(String email, String hashedPassword, String username) throws UserExistsException {
        final User user = new User(email,username, hashedPassword, Authority.USER);
        em.persist(user);
        return user;
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public boolean existsWithId(long id) {
        return findById(id) != null;
    }

    @Override
    public User findByUsername(String username) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean existsWithUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public User findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean existsWithEmail(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public void scoreGame(long userId, long gameId, int score) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        if(!gameDao.existsWithId(gameId)) {
            throw new NoSuchEntityException(Game.class, gameId);
        }
        user.scoreGame(gameId, score);
        em.persist(user);
    }

    @Override
    public void setPlayStatus(long userId, long gameId, PlayStatus status) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        if(!gameDao.existsWithId(gameId)) {
            throw new NoSuchEntityException(Game.class, gameId);
        }
        user.setPlayStatus(gameId, status);
        em.persist(user);
    }

    @Override
    public void removeScore(long userId, long gameId) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        user.getScoredGames().remove(gameId);
        em.persist(user);
    }

    @Override
    public void removeStatus(long userId, long gameId) {
        User user = findById(userId);
        if(user == null) {
            throw new NoSuchUserException(userId);
        }
        user.getPlayStatuses().remove(gameId);
        em.persist(user);
    }

    @Override
    public Collection<Game> recommendGames(long userId) {
        //TODO implement
        return Collections.emptySet();
    }
}
