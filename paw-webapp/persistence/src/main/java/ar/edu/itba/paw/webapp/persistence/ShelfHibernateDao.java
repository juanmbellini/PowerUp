package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.ShelfDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Shelf;
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

@Repository
public class ShelfHibernateDao implements ShelfDao {

    @PersistenceContext
    private EntityManager em;

    private final UserDao userDao;

    private final GameDao gameDao;

    @Autowired
    public ShelfHibernateDao(GameDao gameDao, UserDao userDao) {
        this.gameDao = gameDao;
        this.userDao = userDao;
    }

    @Override
    public Shelf create(String name, long creatorUserId, long... initialGameIds) throws NoSuchEntityException {
        User creator = userDao.findById(creatorUserId);
        if(creator == null) {
            throw new NoSuchUserException(creatorUserId);
        }
        Set<Game> games = new LinkedHashSet<>();
        for(long id : initialGameIds) {
            Game g = gameDao.findById(id);
            if(g == null) {
                throw new NoSuchGameException(id);
            }
            games.add(g);
        }
        Shelf result = new Shelf(name, creator, games);
        em.persist(result);
        return result;
    }

    @Override
    public Set<Shelf> findByGameId(long id) {
        TypedQuery<Shelf> baseQuery = em.createQuery("FROM Shelf AS S JOIN S.games AS game WHERE game.id = :id", Shelf.class);
        baseQuery.setParameter("id", id);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Shelf> findByGameName(String name) {
        TypedQuery<Shelf> baseQuery = em.createQuery("FROM Shelf AS S JOIN S.games AS game WHERE LOWER(game.name) = LOWER(:name)", Shelf.class);
        baseQuery.setParameter("name", name);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Shelf> findByUserId(long id) {
        TypedQuery<Shelf> baseQuery = em.createQuery("FROM Shelf AS S WHERE S.user.id = :id", Shelf.class);
        baseQuery.setParameter("id", id);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Set<Shelf> findByUsername(String name) {
        TypedQuery<Shelf> baseQuery = em.createQuery("FROM Shelf AS S WHERE LOWER(S.user.name) = LOWER(:name)", Shelf.class);
        baseQuery.setParameter("name", name);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public Shelf findById(long shelfId) {
        return em.find(Shelf.class, shelfId);
    }

    @Override
    public boolean belongsTo(long shelfId, long userId) throws NoSuchEntityException {
        return getFreshShelf(shelfId).getUser().getId() == userId;
    }

    @Override
    public void update(long shelfId, long... newGameIds) throws NoSuchEntityException {
        Shelf shelf = getFreshShelf(shelfId);
        shelf.clear();
        for(long id : newGameIds) {
            Game g = gameDao.findById(id);
            if(g == null) {
                throw new NoSuchGameException(id);
            }
            shelf.addGame(g);
        }
        em.persist(shelf);
    }

    @Override
    public Set<Shelf> findByName(String shelfName) {
        TypedQuery<Shelf> baseQuery = em.createQuery("FROM Shelf AS S WHERE LOWER(S.name) = LOWER(:name)", Shelf.class);
        baseQuery.setParameter("name", shelfName);
        try {
            return new LinkedHashSet<>(baseQuery.getResultList());
        } catch(NoResultException e) {
            return Collections.emptySet();
        }
    }

    @Override
    public void addGame(long shelfId, long gameId) throws NoSuchEntityException {
        Shelf shelf = getFreshShelf(shelfId);
        Game g = gameDao.findById(gameId);
        if(g == null) {
            throw new NoSuchGameException(gameId);
        }
        shelf.addGame(g);
        em.persist(shelf);
    }

    @Override
    public void removeGame(long shelfId, long gameId) throws NoSuchEntityException {
        Shelf shelf = getFreshShelf(shelfId);
        Game g = gameDao.findById(gameId);
        if(g == null) {
            throw new NoSuchGameException(gameId);
        }
        shelf.removeGame(g);
        em.persist(shelf);
    }

    @Override
    public void clear(long shelfId) throws NoSuchEntityException {
        Shelf shelf = getFreshShelf(shelfId);
        shelf.clear();
        em.persist(shelf);
    }

    @Override
    public void delete(long shelfId) throws NoSuchEntityException {
        em.remove(getFreshShelf(shelfId));
    }

    /**
     * Gets a shelf by the specified ID that is transaction-safe (i.e. lazily-initialized collections can be accessed)
     * and throws exception if not found. If present in current transaction context, the shelf is returned from there
     * instead of querying the database.
     *
     * @param shelfId The ID of the shelf to fetch.
     * @return The found shelf.
     * @throws NoSuchEntityException If no such shelf exists.
     */
    private Shelf getFreshShelf(long shelfId) {
        Shelf result = em.find(Shelf.class, shelfId);
        if (result == null) {
            throw new NoSuchEntityException(Shelf.class, shelfId);
        }
        return result;
    }
}
