package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Platform;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

/**
 * Data Access Object for platforms.
 */
public interface PlatformDao {

    /**
     * @see PlatformService#all()
     */
    Collection<Platform> all();

    /**
     * @see PlatformService#findById(long)
     */
    Platform findById(long id);

    /**
     * @see PlatformService#findByName(String)
     */
    Platform findByName(String name);

    /**
     * @see PlatformService#gamesReleasedFor(Platform)
     */
    Set<Game> gamesReleasedFor(Platform p);

    /**
     * @see PlatformService#releaseDateForGame(Game, Platform)
     */
    LocalDate releaseDateForGame(Game g, Platform p);
}
