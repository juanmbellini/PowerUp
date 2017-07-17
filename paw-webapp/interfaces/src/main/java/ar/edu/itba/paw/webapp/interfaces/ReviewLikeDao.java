package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.ReviewLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.Map;

/**
 * Data Access Object for ReviewLike review likes.
 */
public interface ReviewLikeDao {


    /**
     * Returns a {@link Page} of ReviewLike belonging to the given review
     *
     * @param review        The liked review.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (id, or creation date).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting {@link Page}.
     */
    Page<ReviewLike> getLikes(Review review, int pageNumber, int pageSize,
                              SortingType sortingType, SortDirection sortDirection);

    /**
     * Finds a {@link ReviewLike} made by a given {@link User} to a given {@link Review}
     *
     * @param review The liked {@link Review}
     * @param user   The {@link User} that liked the {@link Review}.
     * @return The matching {@link ReviewLike}, or {@code null} if not present.
     */
    ReviewLike find(Review review, User user);

    /**
     * Checks whether a given user has liked a given review.
     *
     * @param review The liked {@link Review}
     * @param user   The {@link User} that liked the {@link Review}.
     * @return Whether the user has liked the specified review.
     */
    boolean exists(Review review, User user);

    /**
     * Creates a new {@link ReviewLike} (i.e likes a {@link Review} by a given {@link User}).
     *
     * @param review The {@link Review} being liked
     * @param user   The {@link User} liking the {@link Review}.
     * @return The new {@link ReviewLike}.
     */
    ReviewLike create(Review review, User user);

    /**
     * Deletes the given {@link ReviewLike} (i.e unlikes a {@link Review}).
     *
     * @param reviewLike The {@link ReviewLike} representing the {@link Review} being liked.
     */
    void delete(ReviewLike reviewLike);

    /**
     * Counts the amount of likes for each {@link Review} in the given collection.
     *
     * @param reviews The {@link Review} whose likes must be counted.
     * @return A {@link Map} holding the amount of likes for each {@link Review}.
     */
    Map<Review, Long> countLikes(Collection<Review> reviews);

    /**
     * Indicates whether the given {@link User} liked (or not) the given {@code reviews}.
     *
     * @param reviews The {@link Review} whose likes must be checked.
     * @param user    The {@link User} liking (or not) the {@link Review}s.
     * @return A {@link Map} holding a flag for each {@link Review}, which indicates if its liked or not.
     */
    Map<Review, Boolean> likedBy(Collection<Review> reviews, User user);


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