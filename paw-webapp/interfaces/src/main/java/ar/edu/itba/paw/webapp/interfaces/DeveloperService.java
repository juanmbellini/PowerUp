package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Developer;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Set;

/**
 * Service layer for game developers. Exposes functionality available to developers.
 */
public interface DeveloperService {

    /**
     * Finds all developers.
     *
     * @return An <b>unmodifiable</b> set containing all developers.
     */
    Set<Developer> all();

    /**
     * Finds a developer by ID.
     *
     * @param id The ID to match.
     * @return The found developer or {@code null} if not found.
     */
    Developer findById(long id);

    /**
     * Finds a developer by name.
     *
     * @param name The name to match. Case-sensitive.
     * @return The matched developer, or {@code null} if none found.
     */
    Developer findByName(String name);

    /**
     * Gets all games that have been developed by a specified developer.
     *
     * @param p The developer to match.
     * @return An <b>unmodifiable</b> set containing the matching games.
     */
    Set<Game> gamesDevelopedBy(Developer p);
}
