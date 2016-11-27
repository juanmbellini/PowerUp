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
        return new LinkedHashSet<>(DaoHelper.findAll(em, Developer.class));
    }

    @Override
    public Developer findById(long id) {
        return DaoHelper.findSingle(em, Developer.class, id);
    }

    @Override
    public Developer findByName(String name) {
        return DaoHelper.findSingleWithConditions(em, Developer.class, "FROM Developer AS D WHERE D.name = ?1", name);
    }

    @Override
    public Set<Game> gamesDevelopedBy(Developer d) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(d.getGames()));
    }
}
