package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Company;

/**
 * Service layer for game companies. Exposes functionality available to companies.
 */
public interface CompanyService {

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
}
