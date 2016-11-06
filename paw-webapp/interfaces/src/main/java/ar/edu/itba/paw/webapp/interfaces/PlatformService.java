package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Platform;

import java.time.LocalDate;
import java.util.Set;

/**
 * Service layer for game platforms. Exposes functionality available to platforms.
 */
public interface PlatformService {

    /**
     * Finds all platforms.
     *
     * @return An <b>unmodifiable</b> set containing all platforms.
     */
    Set<Platform> all();

    /**
     * Finds a platform by ID.
     *
     * @param id The ID to match.
     * @return The found platform or {@code null} if not found.
     */
    Platform findById(long id);

    /**
     * Finds a platform by name.
     *
     * @param name The name to match. Case-sensitive.
     * @return The matched platform, or {@code null} if none found.
     */
    Platform findByName(String name);

    /**
     * Gets all games that have been released for a specified platform.
     *
     * @param p The platform to match.
     * @return An <b>unmodifiable</b> set containing the matching games.
     */
    Set<Game> gamesReleasedFor(Platform p);

    /**
     * Gets the date at which the specified game was released for the specified platform. This is equivalent to calling
     * {@code g.getPlatforms().get(p).getReleaseDate()}.
     *
     * @param g The game to find.
     * @param p The platform to match.
     * @return The matching release date, or {@code null} if not found (e.g. game never released for {@code p}).
     */
    LocalDate releaseDateForGame(Game g, Platform p);
}
