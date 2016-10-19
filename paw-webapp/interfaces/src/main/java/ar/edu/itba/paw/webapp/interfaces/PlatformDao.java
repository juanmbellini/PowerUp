package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import org.joda.time.LocalDate;

import java.util.Map;
import java.util.Set;

/**
 * Created by Juan Marcos Bellini on 19/10/16.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 *
 * Data Access Object for Game Platforms
 */
public interface PlatformDao {

    /**
     * Returns a map of platforms-localDates with the platforms for the given game, and the corresponding release date.
     *
     * @param game The game whose platforms will be returned.
     * @return The map with platform-localDates for the given game.
     */
    Map<String, LocalDate> getGamePlatforms(Game game);

    /**
     * Returns a map of platforms-localDates with the platforms for the game with the given id,
     * and the corresponding release date.
     *
     * @param gameId The game's id whose platforms will be returned.
     * @return The map with platform-localDates for game with the given id.
     */
    Map<String, LocalDate> getGamePlatforms(long gameId);
}
