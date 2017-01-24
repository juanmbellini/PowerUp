package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.Set;

/**
 * Service layer for game companies. Exposes functionality available to companies.
 */
public interface CompanyService {

    /**
     * Finds all companies.
     *
     * @return An <b>unmodifiable</b> set containing all companies.
     */
    Collection<Company> all();

    /**
     * Finds a company by ID.
     *
     * @param id The ID to match.
     * @return The found company or {@code null} if not found.
     */
    Company findById(long id);

    /**
     * Finds a company by name.
     *
     * @param name The name to match. Case-sensitive.
     * @return The matched company, or {@code null} if none found.
     */
    Company findByName(String name);

    /**
     * Gets all games that have been developed by a specified company.
     *
     * @param p The company to match.
     * @return An <b>unmodifiable</b> set containing the matching games.
     */
    Set<Game> gamesDevelopedBy(Company p);

    /**
     * Gets all games that have been published by a specified company.
     *
     * @param p The company to match.
     * @return An <b>unmodifiable</b> set containing the matching games.
     */
    Set<Game> gamesPublishedBy(Company p);
}
