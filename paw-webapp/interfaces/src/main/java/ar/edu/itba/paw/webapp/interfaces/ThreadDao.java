package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Data Access Object for {@link Thread Threads}.
 */
public interface ThreadDao {


    /**
     * @see ThreadService#getThreads(String, Long, String, int, int, SortingType, SortDirection) .
     */
    Page<Thread> getThreads(String titleFilter, Long userIdFilter, String usernameFilter,
                            int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection);


    /**
     * @see ThreadService#create(String, String, User)
     */
    Thread create(String title, String creatorComment, User creator);


    /**
     * Updates the given {@link Thread}.
     *
     * @param thread         The thread to be updated.
     * @param title          The new title for the thread.
     * @param initialComment The new initial comment.
     */
    void update(Thread thread, String title, String initialComment);


    /**
     * Updates the given {@link Thread}'s hot value.
     *
     * @param thread The thread to be updated.
     */
    void updateHotValue(Thread thread);


    /**
     * Removes the given {@link Thread} from the database.
     *
     * @param thread The thread to be removed.
     */
    void delete(Thread thread);

    /**
     * @see ThreadService#findById(long threadId)
     */
    Thread findById(long threadId);


    /**
     * Enum indicating the sorting type for the "get threads" method.
     */
    enum SortingType {
        ID {
            @Override
            public String getFieldName() {
                return "id";
            }
        },
        HOT {
            @Override
            public String getFieldName() {
                return "hotValue";
            }
        },
        BEST {
            @Override
            public String getFieldName() {
                return "SIZE(thread.likes)";
            }
        },
        DATE {
            @Override
            public String getFieldName() {
                return "createdAt";
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
            return super.toString().toLowerCase().replace("_", "-");
        }

        /**
         * Creates an enum from the given {@code name} (can be upper, lower or any case)
         *
         * @param name The value of the enum as a string.
         * @return The enum value.
         */
        public static SortingType fromString(String name) {
            return valueOf(name.replace("-", "_").toUpperCase());
        }
    }


}