package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Platform;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.time.LocalDate;
import java.util.Set;

/**
 * Data Access Object for platforms.
 */
public interface PlatformDao {

    /**
     * Retrieves a paginated view of the stored platforms.
     *
     * @param pageNumber The number of the {@link Page}.
     * @param pageSize   The size of the {@link Page}.
     * @return A Paginated view of the list of platforms.
     */
    Page<Platform> paginated(int pageNumber, int pageSize);

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
