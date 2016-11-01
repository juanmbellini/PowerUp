package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Developer;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Set;

/**
 * Data Access Object for Game Developers
 */
public interface DeveloperDao {

    /**
     * @see DeveloperService#all()
     */
    Set<Developer> all();

    /**
     * @see DeveloperService#findById(long)
     */
    Developer findById(long id);

    /**
     * @see DeveloperService#findByName(String)
     */
    Developer findByName(String name);

    /**
     * @see DeveloperService#gamesDevelopedBy(Developer)
     */
    Set<Game> gamesDevelopedBy(Developer p);
}
