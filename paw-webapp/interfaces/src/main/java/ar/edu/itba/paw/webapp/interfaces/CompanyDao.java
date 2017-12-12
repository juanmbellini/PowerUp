package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Data Access Object for Game Developers
 */
public interface CompanyDao {

    /**
     * Retrieves a paginated view of the stored developers.
     *
     * @param pageNumber The number of the {@link Page}.
     * @param pageSize   The size of the {@link Page}.
     * @return A Paginated view of the list of developers.
     */
    Page<Company> developersPaginated(int pageNumber, int pageSize);

    /**
     * Retrieves a paginated view of the stored publishers.
     *
     * @param pageNumber The number of the {@link Page}.
     * @param pageSize   The size of the {@link Page}.
     * @return A Paginated view of the list of publishers.
     */
    Page<Company> publishersPaginated(int pageNumber, int pageSize);

    /**
     * @see CompanyService#findById(long)
     */
    Company findById(long id);

    /**
     * @see CompanyService#findByName(String)
     */
    Company findByName(String name);
}
