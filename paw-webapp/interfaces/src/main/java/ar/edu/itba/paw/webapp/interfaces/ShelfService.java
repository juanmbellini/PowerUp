package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Shelf;

import java.util.Set;

/**
 * Service layer for game shelves. Exposes functionality available to shelves.
 */
public interface ShelfService {

    /**
     * Creates a shelf with a given name, created by a specified user, and with an optional number of initial games.
     *
     * @param creatorUserId The user ID of the creator of the shelf.
     * @param initialGameIds (Optional) list of game IDs to add to the shelf upon creation.
     * @return The created shelf.
     * @throws NoSuchEntityException If the creator or at least one of the games don't exist.
     */
    Shelf create(String name, long creatorUserId, long... initialGameIds) throws NoSuchEntityException;

    /**
     * Returns a set of shelves for a specified game, identified by ID.
     *
     * @param id The ID of the game whose shelves to fetch.
     * @return The resulting set of shelves.
     */
    Set<Shelf> findByGameId(long id);

    /**
     * Returns a set of shelves for a specified game, identified by name.
     *
     * @param name The name of the game whose shelves to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of shelves.
     */
    Set<Shelf> findByGameName(String name);

    /**
     * Returns a set of shelves created by a specified user, identified by ID.
     *
     * @param id The ID of the user whose shelves to fetch.
     * @return The resulting set of shelves.
     */
    Set<Shelf> findByUserId(long id);

    /**
     * Returns a set of shelves created by a specified user, identified by username.
     *
     * @param name The name of the user whose shelves to fetch. Case <b>in</b>sensitive.
     * @return The resulting set of shelves.
     */
    Set<Shelf> findByUsername(String name);

    /**
     * Finds a shelf by ID.
     *
     * @param shelfId The ID to match.
     * @return The matching shelf or {@code null} if not found.
     */
    Shelf findById(long shelfId);

    /**
     * Finds shelves by name.
     *
     * @param shelfName The name to match. Case <b>in</b>sensitive.
     * @return The matching shelves.
     */
    Set<Shelf> findByName(String shelfName);

    /**
     * Checks whether a specified shelf belongs to a specified user.
     *
     * @param shelfId The shelf ID.
     * @param userId The user ID.
     * @return Whether the shelf belongs to the user.
     * @throws NoSuchEntityException If the shelf doesn't exist.
     */
    boolean belongsTo(long shelfId, long userId) throws NoSuchEntityException;

    /**
     * Updates the contents of the specified shelf, overwriting previous content.
     *
     * @param shelfId The ID of the shelf to update.
     * @param newGameIds The new game IDs. Note that the previous game IDs will be overwritten with these.
     * @throws NoSuchEntityException If the shelf or at least one of the games don't exist.
     */
    void update(long shelfId, long... newGameIds) throws NoSuchEntityException;

    /**
     * Adds a specified game to a specified shelf.
     *
     * @param shelfId The ID of the shelf that the game is being added to.
     * @param gameId The ID of the game being added.
     * @throws NoSuchEntityException If the shelf doesn't exist.
     */
    void addGame(long shelfId, long gameId) throws NoSuchEntityException;

    /**
     * Removes a specified game to a specified shelf.
     *
     * @param shelfId The ID of the shelf that the game is being removed from.
     * @param gameId The ID of the game being removed.
     * @throws NoSuchEntityException If the shelf doesn't exist.
     */
    void removeGame(long shelfId, long gameId) throws NoSuchEntityException;

    /**
     * Clears a shelf from all its games.
     *
     * @param shelfId The ID of the shelf to clear.
     * @throws NoSuchEntityException If the shelf doesn't exist.
     */
    void clear(long shelfId) throws NoSuchEntityException;

    /**
     * Deletes a shelf.
     *
     * @param shelfId The ID of the shelf to delete.
     * @throws NoSuchEntityException If the shelf doesn't exist.
     */
    void delete(long shelfId) throws NoSuchEntityException;
}