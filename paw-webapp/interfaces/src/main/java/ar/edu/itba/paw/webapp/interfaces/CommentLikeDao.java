package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.CommentLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.Map;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.CommentLike}.
 */
public interface CommentLikeDao {


    /**
     * Returns a {@link Page} of {@link CommentLike} belonging to the given {@link Comment}
     *
     * @param comment       The liked {@link Comment}.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (id, or creation date).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting {@link Page}.
     */
    Page<CommentLike> getLikes(Comment comment, int pageNumber, int pageSize,
                               SortingType sortingType, SortDirection sortDirection);


    /**
     * Finds a {@link CommentLike} made by a given {@link User} to a given {@link Comment}
     *
     * @param comment The liked {@link Comment}
     * @param user    The {@link User} that liked the {@link Comment}.
     * @return The matching {@link CommentLike}, or {@code null} if not present.
     */
    CommentLike find(Comment comment, User user);

    /**
     * Checks whether a given {@link User} has liked a given {@link Comment}.
     *
     * @param comment The liked {@link Comment}
     * @param user    The {@link User} that liked the {@link Comment}.
     * @return Whether the {@link User} has liked the specified {@link Comment}.
     */
    boolean exists(Comment comment, User user);

    /**
     * Creates a {@link CommentLike} (i.e. makes the given {@link User} like the given {@link Comment}).
     *
     * @param comment The comment to be liked.
     * @param user    The user liking the comment.
     * @return The created {@link CommentLike}.
     */
    CommentLike create(Comment comment, User user);

    /**
     * Deletes the given {@link CommentLike} (i.e. makes the {@link User} unlike the {@link Comment}).
     *
     * @param commentLike The {@link CommentLike} to be removed.
     */
    void delete(CommentLike commentLike);

    /**
     * Counts the amount of likes for each {@link Comment} in the given collection.
     *
     * @param comments The {@link Comment} whose likes must be counted.
     * @return A {@link Map} holding the amount of likes for each {@link Comment}.
     */
    Map<Comment, Long> countLikes(Collection<Comment> comments);

    /**
     * Indicates whether the given {@link User} liked (or not) the given {@code comments}.
     *
     * @param comments The {@link Comment} whose likes must be checked.
     * @param user     The {@link User} liking (or not) the {@link Comment}s.
     * @return A {@link Map} holding a flag for each {@link Comment}, which indicates if its liked or not.
     */
    Map<Comment, Boolean> likedBy(Collection<Comment> comments, User user);


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