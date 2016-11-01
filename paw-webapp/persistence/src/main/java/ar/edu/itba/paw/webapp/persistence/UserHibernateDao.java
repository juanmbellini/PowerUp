package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Authority;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;
import java.util.List;

/**
 * Created by julrodriguez on 28/10/16.
 */
@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String email, String hashedPassword, String username) throws UserExistsException {
        final User user = new User(email,username, hashedPassword, Authority.USER);
        em.persist(user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public User findById(long id) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.id = :id", User.class);
        query.setParameter("id", id);
        final List<User> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public boolean existsWithId(long id) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.id = :id", User.class);
        query.setParameter("id", id);
        final List<User> list = query.getResultList();
        return list.size() == 0 ? false : true;
    }

    @Override
    public boolean existsWithUsername(String username) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.username = :username", User.class);
        query.setParameter("username", username);
        final List<User> list = query.getResultList();
        return list.size() == 0 ? false : true;
    }

    @Override
    public boolean existsWithEmail(String email) {
        final TypedQuery<User> query = em.createQuery("from User as u where u.email = :email", User.class);
        query.setParameter("email", email);
        final List<User> list = query.getResultList();
        return list.size() == 0 ? false : true;
    }

    @Override
    public void scoreGame(User user, long gameId, int score) {

    }

    @Override
    public void scoreGame(User user, Game game, int score) {

    }

    @Override
    public void setPlayStatus(User user, long gameId, PlayStatus status) {

    }

    @Override
    public void setPlayStatus(User user, Game game, PlayStatus status) {

    }

    @Override
    public void removeScore(User u, long id) {

    }

    @Override
    public void removeStatus(User u, long id) {

    }

    @Override
    public Collection<Game> recommendGames(User user) {
        return null;
    }
}
