package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GenreDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by juanlipuma on Nov/1/16.
 */
@Repository
public class GenreHibernateDao implements GenreDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Set<Genre> all() {
        TypedQuery<Genre> query = em.createQuery("FROM Genre", Genre.class);
        try {
            return Collections.unmodifiableSet(new LinkedHashSet<>(query.getResultList()));
        } catch(NoResultException e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Genre findById(long id) {
        TypedQuery<Genre> baseQuery = em.createQuery("FROM Genre AS G where G.id = :id", Genre.class);
        baseQuery.setParameter("id", id);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Genre findByName(String name) {
        TypedQuery<Genre> baseQuery = em.createQuery("FROM Genre AS G where G.name = :name", Genre.class);
        baseQuery.setParameter("name", name);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Game> gamesWithGenre(Genre p) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(p.getGames()));
    }
}
