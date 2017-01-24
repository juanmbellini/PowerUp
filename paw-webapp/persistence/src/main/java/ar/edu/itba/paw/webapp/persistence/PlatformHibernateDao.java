package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.PlatformDao;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.GamePlatformReleaseDate;
import ar.edu.itba.paw.webapp.model.Genre;
import ar.edu.itba.paw.webapp.model.Platform;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.Collection;
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
    public Collection<Platform> all() {
        return DaoHelper.findAll(em, Platform.class);
    }

    @Override
    public Platform findById(long id) {
        return id <= 0 ? null : em.find(Platform.class, id);
    }

    @Override
    public Platform findByName(String name) {
        // TODO: What if more than one platform has the same name?
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
