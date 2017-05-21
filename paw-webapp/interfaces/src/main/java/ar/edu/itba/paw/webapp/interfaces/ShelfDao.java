package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Shelf;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Data Access Object for game reviews.
 */
public interface ShelfDao {


    /**
     * @see ShelfService#getShelves(String, Long, String, Long, String, int, int, SortingType, SortDirection)
     */
    Page<Shelf> getShelves(String nameFilter, Long gameIdFilter, String gameNameFilter,
                           Long userIdFilter, String userNameFilter,
                           int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection);

    /**
     * @see ShelfService#findById(long)
     */
    Shelf findById(long shelfId);

    /**
     * @see ShelfService#findByName(String)
     */
    Shelf findByName(String name);

    /**
     * @see ShelfService#create(String, long)
     */
    Shelf create(String name, User creator);

    /**
     * @see ShelfService#update(long, String, long)
     */
    void update(Shelf shelf, String name);

    /**
     * @see ShelfService#delete(long, long)
     */
    void delete(Shelf shelf);


    /**
     * @see ShelfService#addGameToShelf(long, long, long)
     */
    void addGameToShelf(Shelf shelf, Game game);

    /**
     * @see ShelfService#removeGameFromShelf(long, long, long)
     */
    void removeGameFromShelf(Shelf shelf, Game game);

    /**
     * @see ShelfService#clearShelf(long, long)
     */
    void clearShelf(Shelf shelf);


    /**
     * Enum indicating the sorting type for the "get shelves" method.
     */
    enum SortingType {
        ID {
            @Override
            public String getFieldName() {
                return toString();
            }
        },
        NAME {
            @Override
            public String getFieldName() {
                return toString();
            }
        },
        UPDATED {
            @Override
            public String getFieldName() {
                return "updated_at";
            }
        };

        /**
         * Returns the "sorting by" field name.
         *
         * @return The name.
         */
        abstract public String getFieldName();

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        /**
         * Creates an enum from the given {@code name} (can be upper, lower or any case)
         *
         * @param name The value of the enum as a string.
         * @return The enum value.
         */
        public static SortingType fromString(String name) {
            return valueOf(name.toUpperCase());
        }
    }


}