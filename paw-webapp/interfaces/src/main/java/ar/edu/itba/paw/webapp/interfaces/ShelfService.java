package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Service layer for game shelves. Exposes functionality available to shelves.
 */
public interface ShelfService {


    /**
     * Returns a {@link Page} with the shelves, applying filters, pagination and sorting.
     *
     * @param nameFilter     Filter for the name.
     * @param gameIdFilter   Filter for game id.
     * @param gameNameFilter Filter for game name.
     * @param userIdFilter   Filter for user id.
     * @param userNameFilter Filter for user name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type (id, game id, or creation date).
     * @param sortDirection  The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<Shelf> getShelves(String nameFilter, Long gameIdFilter, String gameNameFilter,
                           Long userIdFilter, String userNameFilter,
                           int pageNumber, int pageSize, ShelfDao.SortingType sortingType, SortDirection sortDirection);

    /**
     * Finds a shelf by ID.
     *
     * @param shelfId The ID to match.
     * @return The matching shelf or {@code null} if not found.
     */
    Shelf findById(long shelfId);

    /**
     * Creates a new {@link Shelf} using the specified data.
     *
     * @param name      The name for the shelf.
     * @param creatorId The id of the {@link User} creating the shelf.
     * @return The created shelf.
     */
    Shelf create(String name, long creatorId);


    /**
     * Updates the {@link Shelf} with the given {@code shelfId}.
     *
     * @param shelfId   The shelf id.
     * @param name      The new name.
     * @param updaterId The user performing the operation.
     */
    void update(long shelfId, String name, long updaterId);


    /**
     * Deletes {@link Shelf} with the given {@code shelfId}.
     *
     * @param shelfId   The shelf id.
     * @param deleterId The user performing the operation.
     */
    void delete(long shelfId, long deleterId);


    /**
     * Adds the {@link Game} with the given {@code gameId} to the {@link Shelf} with the given {@code shelfId}.
     *
     * @param shelfId   The shelf id.
     * @param gameId    The game id.
     * @param updaterId The user performing the operation.
     */
    void addGameToShelf(long shelfId, long gameId, long updaterId);

    /**
     * Removes the {@link Game} with the given {@code gameId} from the {@link Shelf} with the given {@code shelfId}.
     *
     * @param shelfId   The shelf id.
     * @param gameId    The game id.
     * @param updaterId The user performing the operation.
     */
    void removeGameFromShelf(long shelfId, long gameId, long updaterId);

    /**
     * Removes all {@link Game}s from the {@link Shelf} with the given {@code shelfId}.
     *
     * @param shelfId   The shelf id.
     * @param updaterId The user performing the operation.
     */
    void clearShelf(long shelfId, long updaterId);


}