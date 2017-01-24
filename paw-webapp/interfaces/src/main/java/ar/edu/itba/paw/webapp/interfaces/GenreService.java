package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;

import java.util.Collection;
import java.util.Set;

/**
 * Service layer for game genres. Exposes functionality available to genres.
 */
public interface GenreService {

    /**
     * Finds all genres.
     *
     * @return An <b>unmodifiable</b> set containing all genres.
     */
    Collection<Genre> all();

    /**
     * Finds a genre by ID.
     *
     * @param id The ID to match.
     * @return The found genre or {@code null} if not found.
     */
    Genre findById(long id);

    /**
     * Finds a genre by name.
     *
     * @param name The name to match. Case-sensitive.
     * @return The matched genre, or {@code null} if none found.
     */
    Genre findByName(String name);

    /**
     * Gets all games of the specified genre.
     *
     * @param p The genre to match.
     * @return An <b>unmodifiable</b> set containing the matching games.
     */
    Set<Game> gamesWithGenre(Genre p);
}
