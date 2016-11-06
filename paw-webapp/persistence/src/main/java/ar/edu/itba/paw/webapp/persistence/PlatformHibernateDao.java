package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.PlatformDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.GamePlatformReleaseDate;
import ar.edu.itba.paw.webapp.model.Platform;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by juanlipuma on Nov/1/16.
 */
@Repository
public class PlatformHibernateDao implements PlatformDao {
    @PersistenceContext
    private EntityManager em;

    private PlatformHibernateDao() {}

    @Override
    public Set<Platform> all() {
        TypedQuery<Platform> query = em.createQuery("FROM Platform", Platform.class);
        try {
            return Collections.unmodifiableSet(new LinkedHashSet<>(query.getResultList()));
        } catch(NoResultException e) {
            return Collections.EMPTY_SET;
        }
    }

    @Override
    public Platform findById(long id) {
        TypedQuery<Platform> baseQuery = em.createQuery("FROM Platform AS P where P.id = :id", Platform.class);
        baseQuery.setParameter("id", id);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Platform findByName(String name) {
        TypedQuery<Platform> baseQuery = em.createQuery("FROM Platform AS P where P.name = :name", Platform.class);
        baseQuery.setParameter("name", name);
        try {
            return baseQuery.getSingleResult();
        } catch(NoResultException e) {
            return null;
        }
    }

    @Override
    public Set<Game> gamesReleasedFor(Platform p) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(p.getGames()));
    }

    @Override
    public LocalDate releaseDateForGame(Game g, Platform p) {
        GamePlatformReleaseDate joinData = g.getPlatforms().get(p);
        return joinData == null ? null : joinData.getReleaseDate();
    }
}
