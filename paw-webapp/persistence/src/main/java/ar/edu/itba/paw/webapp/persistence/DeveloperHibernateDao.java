package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.DeveloperDao;
import ar.edu.itba.paw.webapp.model.Developer;
import ar.edu.itba.paw.webapp.model.Game;
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
public class DeveloperHibernateDao implements DeveloperDao {
    @PersistenceContext
    private EntityManager em;
    
    @Override
    public Set<Developer> all() {
        TypedQuery<Developer> query = em.createQuery("FROM Developer", Developer.class);
        try {
            return Collections.unmodifiableSet(new LinkedHashSet<>(query.getResultList()));
        } catch(NoResultException e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Developer findById(long id) {
        TypedQuery<Developer> baseQuery = em.createQuery("FROM Developer AS D where D.id = :id", Developer.class);
        baseQuery.setParameter("id", id);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Developer findByName(String name) {
        TypedQuery<Developer> baseQuery = em.createQuery("FROM Developer AS D where D.name = :name", Developer.class);
        baseQuery.setParameter("name", name);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Game> gamesDevelopedBy(Developer d) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(d.getGames()));
    }
}
