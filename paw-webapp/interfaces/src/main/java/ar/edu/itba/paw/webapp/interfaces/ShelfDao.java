package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.model.Shelf;

import java.util.Set;

/**
 * Data Access Object for game reviews.
 */
public interface ShelfDao {

    /**
     * @see ShelfService#create(String, long, long...)
     */
    Shelf create(String name, long creatorUserId, long... initialGameIds) throws NoSuchEntityException;

    /**
     * @see ShelfService#findByGameId(long)
     */
    Set<Shelf> findByGameId(long id);

    /**
     * @see ShelfService#findByGameName(String)
     */
    Set<Shelf> findByGameName(String name);

    /**
     * @see ShelfService#findByUserId(long)
     */
    Set<Shelf> findByUserId(long id);

    /**
     * @see ShelfService#findByUsername(String)
     */
    Set<Shelf> findByUsername(String name);

    /**
     * @see ShelfService#findById(long)
     */
    Shelf findById(long shelfId);

    /**
     * @see ShelfService#findByName(String)
     */
    Set<Shelf> findByName(String shelfName);

    void addGame(long shelfId, long gameId) throws NoSuchEntityException;

    /**
     * @see ShelfService#removeGame(long, long)
     */
    void removeGame(long shelfId, long gameId) throws NoSuchEntityException;

    /**
     * @see ShelfService#clear(long)
     */
    void clear(long shelfId) throws NoSuchEntityException;

    /**
     * @see ShelfService#delete(long)
     */
    void delete(long shelfId) throws NoSuchEntityException;
}