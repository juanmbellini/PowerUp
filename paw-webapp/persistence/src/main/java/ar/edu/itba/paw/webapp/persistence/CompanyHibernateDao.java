package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.CompanyDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by juanlipuma on Nov/1/16.
 */
@Repository
public class CompanyHibernateDao implements CompanyDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Company> developersPaginated(int pageNumber, int pageSize) {
        return DaoHelper.findPageWithoutConditions(em, Company.class,
                "From Company as company inner join company.gamesDeveloped",
                "company", "company.id",
                pageNumber, pageSize, "company.id", SortDirection.ASC, true);
    }

    @Override
    public Page<Company> publishersPaginated(int pageNumber, int pageSize) {
        return DaoHelper.findPageWithoutConditions(em, Company.class,
                "From Company as company inner join company.gamesPublished",
                "company", "company.id",
                pageNumber, pageSize, "company.id", SortDirection.ASC, true);
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
}
