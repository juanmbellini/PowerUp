package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.OrderCategory;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Service layer for game shelves. Exposes functionality available to shelves.
 */
public interface ShelfService {


    /**
     * Returns a {@link Page} with the shelves owned by the {@link User} with the given ownerId,
     * applying filters, pagination and sorting.
     *
     * @param ownerId        The id of the {@link User} owning the returned shelves.
     * @param nameFilter     Filter for the name.
     * @param gameIdFilter   Filter for game id.
     * @param gameNameFilter Filter for game name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingType    The sorting type (id, game id, or creation date).
     * @param sortDirection  The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<Shelf> getUserShelves(long ownerId, String nameFilter, Long gameIdFilter, String gameNameFilter,
                               int pageNumber, int pageSize, ShelfDao.SortingType sortingType,
                               SortDirection sortDirection);

    /**
     * Finds a shelf by ID.
     *
     * @param shelfId The ID to match.
     * @return The matching shelf or {@code null} if not found.
     */
    Shelf findById(long shelfId);

    /**
     * Finds a shelf by its name, given a {@link User} whose id is the given {@code userId}.
     *
     * @param userId The owner's id.
     * @param name   The shelf's name.
     * @return The shelf if the user and the shelf exist, or {@code null} otherwise.
     */
    Shelf findByName(long userId, String name);

    /**
     * Creates a new {@link Shelf} using the specified data.
     *
     * @param ownerId The id of the {@link User} that will own the new shelf.
     * @param name    The name for the shelf.
     * @param creator The {@link User} creating the shelf.
     * @return The created shelf.
     */
    Shelf create(long ownerId, String name, User creator);


    /**
     * Updates the {@link Shelf} with the given {@code name}, owned by the given {@code ownerId}.
     *
     * @param ownerId The owner of the shelf.
     * @param name    The shelf's name.
     * @param newName The new shelf's name.
     * @param updater The user performing the operation.
     */
    void update(long ownerId, String name, String newName, User updater);


    /**
     * Deletes the {@link Shelf} with the given {@code name}, owned by the given {@code ownerId}.
     *
     * @param ownerId The owner of the shelf.
     * @param name    The shelf's name.
     * @param deleter The user performing the operation.
     */
    void delete(long ownerId, String name, User deleter);


    /**
     * Returns a paginated list of games of the {@link Shelf} with the given {@code shelfName},
     * belonging to the {@link User} with the given {@code ownerId}.
     *
     * @param ownerId       The owner's id.
     * @param shelfName     The shelf's name.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param orderCategory The ordering category.
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return
     */
    Page<Game> getShelfGames(long ownerId, String shelfName,
                             int pageNumber, int pageSize, OrderCategory orderCategory, SortDirection sortDirection);


    /**
     * Adds the {@link Game} with the given {@code gameId} to the {@link Shelf} with the given {@code shelfName},
     * owned by the given {@code ownerId}.
     *
     * @param ownerId   The id of the {@link User} owning the {@link Shelf} with the given {@code shelfName}.
     * @param shelfName The name of the {@link Shelf}.
     * @param gameId    The game id.
     * @param updater   The user performing the operation.
     */
    void addGameToShelf(long ownerId, String shelfName, long gameId, User updater);

    /**
     * Removes the {@link Game} with the given {@code gameId} from the {@link Shelf} with the given {@code shelfName},
     * owned by the given {@code ownerId}.
     *
     * @param ownerId   The id of the {@link User} owning the {@link Shelf} with the given {@code shelfName}.
     * @param shelfName The name of the {@link Shelf}.
     * @param gameId    The game id.
     * @param updater   The user performing the operation.
     */
    void removeGameFromShelf(long ownerId, String shelfName, long gameId, User updater);

    /**
     * Removes all {@link Game}s from the {@link Shelf} with the given {@code shelfName},
     * owned by the given {@code ownerId}.
     *
     * @param ownerId   The id of the {@link User} owning the {@link Shelf} with the given {@code shelfName}.
     * @param shelfName The name of the {@link Shelf}.
     * @param updater   The user performing the operation.
     */
    void clearShelf(long ownerId, String shelfName, User updater);
}