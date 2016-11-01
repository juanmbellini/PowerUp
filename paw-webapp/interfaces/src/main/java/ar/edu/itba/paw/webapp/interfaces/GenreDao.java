package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;

import java.util.Set;

/**
 * Data Access Object for Game Platforms
 */
public interface GenreDao {
    /**
     * @see GenreService#all()
     */
    Set<Genre> all();

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
