package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.PublisherDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Publisher;
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
public class PublisherHibernateDao implements PublisherDao {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Set<Publisher> all() {
        TypedQuery<Publisher> query = em.createQuery("FROM Publisher", Publisher.class);
        try {
            return Collections.unmodifiableSet(new LinkedHashSet<>(query.getResultList()));
        } catch(NoResultException e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Publisher findById(long id) {
        TypedQuery<Publisher> baseQuery = em.createQuery("FROM Publisher AS P where P.id = :id", Publisher.class);
        baseQuery.setParameter("id", id);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Publisher findByName(String name) {
        TypedQuery<Publisher> baseQuery = em.createQuery("FROM Publisher AS P where P.name = :name", Publisher.class);
        baseQuery.setParameter("name", name);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Game> gamesPublishedBy(Publisher p) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(p.getGames()));
    }
}
