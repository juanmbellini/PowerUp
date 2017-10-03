package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.Map;

/**
 * Data Access Object for {@link ar.edu.itba.paw.webapp.model.Comment Comments}.
 */
public interface CommentDao {


    /**
     * Finds a {@link Page} of top level {@link Comment}s to the given {@link Thread}.
     * Sorting and Pagination can be applied.
     *
     * @param thread        The commented {@link Thread}.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (date or best).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<Comment> getThreadComments(Thread thread, int pageNumber, int pageSize,
                                    SortingType sortingType, SortDirection sortDirection);


    /**
     * Finds a comment or a reply by ID.
     *
     * @param id The ID.
     * @return The matching comment or reply, or {@code null} if not found.
     */
    Comment findById(long id);

    /**
     * Creates a comment in a {@link Thread}.
     *
     * @param thread    The commented {@link Thread}.
     * @param body      The comment content.
     * @param commenter The {@link User} commenting.
     * @return The new {@link Comment}.
     */
    Comment comment(Thread thread, String body, User commenter);


    /**
     * Changes the given {@link Comment}.
     *
     * @param comment    The comment to be changed.
     * @param newComment The new content of the message.
     */
    void update(Comment comment, String newComment);


    /**
     * Removes the given {@link Comment} from the database.
     *
     * @param comment The comment to be removed.
     */
    void delete(Comment comment);

    /**
     * Finds a {@link Page} of {@link Comment}s that are immediate reply to the {@link Comment}
     * with the given {@code commentId}.
     * Sorting and Pagination can be applied.
     *
     * @param comment       The replied {@link Comment}.
     * @param pageNumber    The page number.
     * @param pageSize      The page size.
     * @param sortingType   The sorting type (date or best).
     * @param sortDirection The sort direction (i.e ASC or DESC).
     * @return The resulting page.
     */
    Page<Comment> getCommentReplies(Comment comment, int pageNumber, int pageSize,
                                    SortingType sortingType, SortDirection sortDirection);

    /**
     * Replies a comment (i.e comments a comment). Note that a reply is also a {@link Comment}.
     *
     * @param comment The {@link Comment} to be replied.
     * @param body    The comment content.
     * @param replier The {@link User} replying
     * @return The new {@link Comment} (i.e the reply to the given {@link Comment}).
     */
    Comment reply(Comment comment, String body, User replier);


    /**
     * Counts the amount of {@link Comment}s the given {@link Thread}s have.
     *
     * @param threads A {@link Collection} of {@link Thread} to count their comments.
     * @return A {@link Map} containing each {@link Thread} as keys, and the amount of {@link Comment}s each has.
     */
    Map<Thread, Long> countComments(Collection<Thread> threads);

    /**
     * Counts the amount of {@link Comment}s in reply to the given {@link Comment}s each one has.
     *
     * @param comments A {@link Collection} of {@link Comment} to count their replies.
     * @return A {@link Map} containing each {@link Comment} as keys, and the amount of {@link Comment}s each has.
     */
    Map<Comment, Long> countReplies(Collection<Comment> comments);


    /**
     * Finds a top-level comment for a given thread by a given user.
     *
     * @param threadId The thread ID.
     * @param userId   The user ID.
     * @return The found comment, or {@code null} if not found.
     */
    Comment findComment(long threadId, long userId);

    /**
     * Finds a reply to a given comment by a given user. Does <b>NOT</b> search recursively down further replies.
     *
     * @param commentId The parent comment ID.
     * @param userId    The user ID.
     * @return The found reply, or {@code null} if not found.
     */
    Comment findReply(long commentId, long userId);


    /**
     * Enum indicating the sorting type for the "get threads" method.
     */
    enum SortingType {
        BEST {
            @Override
            public String getFieldName() {
                return "SIZE(comment.likes)";
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