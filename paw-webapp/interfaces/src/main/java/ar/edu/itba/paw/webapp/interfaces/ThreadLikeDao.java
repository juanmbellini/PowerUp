package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.ThreadLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.Map;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.ThreadLike Thread likes}.
 */
public interface ThreadLikeDao {


    /**
     * Returns a {@link Page} of {@link ThreadLike} belonging to the given {@link Thread}
     *
     * @param thread        The liked {@link Thread}.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (id, or creation date).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting {@link Page}.
     */
    Page<ThreadLike> getLikes(Thread thread, int pageNumber, int pageSize,
                              SortingType sortingType, SortDirection sortDirection);

    /**
     * Finds a {@link ThreadLike} made by a given {@link User} to a given {@link Thread}
     *
     * @param thread The liked {@link Thread}
     * @param user   The {@link User} that liked the {@link Thread}.
     * @return The matching {@link ThreadLike}, or {@code null} if not present.
     */
    ThreadLike find(Thread thread, User user);

    /**
     * Checks whether a given user has liked a given thread.
     *
     * @param thread The liked {@link Thread}
     * @param user   The {@link User} that liked the {@link Thread}.
     * @return Whether the user has liked the specified thread.
     */
    boolean exists(Thread thread, User user);

    /**
     * Creates a new {@link ThreadLike} (i.e likes a {@link Thread} by a given {@link User}).
     *
     * @param thread The {@link Thread} being liked
     * @param user   The {@link User} liking the {@link Thread}.
     * @return The new {@link ThreadLike}.
     */
    ThreadLike create(Thread thread, User user);

    /**
     * Deletes the given {@link ThreadLike} (i.e unlikes a {@link Thread}).
     *
     * @param threadLike The {@link ThreadLike} representing the {@link Thread} being liked.
     */
    void delete(ThreadLike threadLike);

    /**
     * Counts the amount of likes for each {@link Thread} in the given collection.
     *
     * @param threads The {@link Thread} whose likes must be counted.
     * @return A {@link Map} holding the amount of likes for each {@link Thread}.
     */
    Map<Thread, Long> countLikes(Collection<Thread> threads);


    /**
     * Enum indicating the sorting type for the "get users liking" method.
     */
    enum SortingType {
        ID {
            @Override
            public String getFieldName() {
                return "id";
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