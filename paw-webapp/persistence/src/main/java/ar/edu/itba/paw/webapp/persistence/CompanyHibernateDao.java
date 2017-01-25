package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CompanyDao;
import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by juanlipuma on Nov/1/16.
 */
@Repository
public class CompanyHibernateDao implements CompanyDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Company> all() {
        return DaoHelper.findAll(em, Company.class);
    }

    @Override
    public Company findById(long id) {
        return id <= 0 ? null : em.find(Company.class, id);
    }

    @Override
    public Company findByName(String name) {
        // TODO: What if more than one company has the same name?
        TypedQuery<Company> baseQuery = em.createQuery("FROM Company AS C where C.name = :name", Company.class);
        baseQuery.setParameter("name", name);
        try {
            return baseQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    // TODO: Check these two methods bellow --> They aren't needed here

    @Override
    public Set<Game> gamesDevelopedBy(Company d) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(d.getGamesDeveloped()));
    }

    @Override
    public Set<Game> gamesPublishedBy(Company p) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(p.getGamesPublished()));
    }
}
