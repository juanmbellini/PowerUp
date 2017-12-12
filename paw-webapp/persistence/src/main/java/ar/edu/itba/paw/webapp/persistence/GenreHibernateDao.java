package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GenreDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import ar.edu.itba.paw.webapp.utilities.Page;
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
    public Page<Genre> paginated(int pageNumber, int pageSize) {
        return DaoHelper.findPageWithoutConditions(em, Genre.class,
                "From Genre as genre", "genre", "id",
                pageNumber, pageSize, "id", SortDirection.ASC, true);
    }

    @Override
    public Genre findById(long id) {
        return id <= 0 ? null : em.find(Genre.class, id);
    }

    @Override
    public Genre findByName(String name) {
        // TODO: What if more than one genre has the same name?
        TypedQuery<Genre> baseQuery = em.createQuery("FROM Genre AS G where G.name = :name", Genre.class);
        baseQuery.setParameter("name", name);
        try {
            return baseQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Game> gamesWithGenre(Genre p) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(p.getGames()));
    }
}
