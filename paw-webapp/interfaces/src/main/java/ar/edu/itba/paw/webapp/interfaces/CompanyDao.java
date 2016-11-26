package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Set;

/**
 * Data Access Object for Game Developers
 */
public interface CompanyDao {

    /**
     * @see CompanyService#all()
     */
    Set<Company> all();

    /**
     * @see CompanyService#findById(long)
     */
    Company findById(long id);

    /**
     * @see CompanyService#findByName(String)
     */
    Company findByName(String name);

    /**
     * @see CompanyService#gamesDevelopedBy(Company)
     */
    Set<Game> gamesDevelopedBy(Company p);

    /**
     * @see CompanyService#gamesPublishedBy(Company)
     */
    Set<Game> gamesPublishedBy(Company p);
}
