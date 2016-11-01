package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Publisher;

import java.util.Set;

/**
 * Service layer for game publishers. Exposes functionality available to publishers.
 */
public interface PublisherService {

    /**
     * Finds all publishers.
     *
     * @return An <b>unmodifiable</b> set containing all publishers.
     */
    Set<Publisher> all();

    /**
     * Finds a publisher by ID.
     *
     * @param id The ID to match.
     * @return The found publisher or {@code null} if not found.
     */
    Publisher findById(long id);

    /**
     * Finds a publisher by name.
     *
     * @param name The name to match. Case-sensitive.
     * @return The matched publisher, or {@code null} if none found.
     */
    Publisher findByName(String name);

    /**
     * Gets all games that have been published by a specified publisher.
     *
     * @param p The publisher to match.
     * @return An <b>unmodifiable</b> set containing the matching games.
     */
    Set<Game> gamesPublishedBy(Publisher p);
}
