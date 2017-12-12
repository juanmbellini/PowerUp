package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Set;

/**
 * Data Access Object for Game Platforms
 */
public interface GenreDao {

    /**
     * Retrieves a paginated view of the stored genres.
     *
     * @param pageNumber The number of the {@link Page}.
     * @param pageSize   The size of the {@link Page}.
     * @return A Paginated view of the list of genres.
     */
    Page<Genre> paginated(int pageNumber, int pageSize);

    /**
     * @see GenreService#findById(long)
     */
    Genre findById(long id);

    /**
     * @see GenreService#findByName(String)
     */
    Genre findByName(String name);

    /**
     * @see GenreService#gamesWithGenre(Genre)
     */
    Set<Game> gamesWithGenre(Genre p);

}
