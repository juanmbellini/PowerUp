package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model_wrappers.ThreadWithLikeCount;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;


@Service
@Transactional
public class ThreadServiceImpl implements ThreadService {

    /**
     * The dao for {@link Thread}.
     */
    private final ThreadDao threadDao;
    /**
     * The dao for {@link Comment}.
     */
    private final CommentDao commentDao;
    /**
     * The dao for {@link User}.
     */
    private final UserDao userDao;
    /**
     * The dao for {@link ThreadLike}.
     */
    private final ThreadLikeDao threadLikeDao;
    /**
     * The dao for {@link CommentLike}.
     */
    private final CommentLikeDao commentLikeDao;


    @Autowired
    public ThreadServiceImpl(ThreadDao threadDao, CommentDao commentDao, UserDao userDao,
                             ThreadLikeDao threadLikeDao, CommentLikeDao commentLikeDao) {
        this.threadDao = threadDao;
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.threadLikeDao = threadLikeDao;
        this.commentLikeDao = commentLikeDao;
    }

    @Override
    public Page<ThreadWithLikeCount> getThreads(String titleFilter, Long userIdFilter, String usernameFilter,
                                                int pageNumber, int pageSize,
                                                ThreadDao.SortingType sortingType, SortDirection sortDirection) {
        final Page<Thread> page = threadDao.getThreads(titleFilter, userIdFilter, usernameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<Thread, Long> likeCounts = threadLikeDao.countLikes(page.getData());

        return new Page.Builder<ThreadWithLikeCount>()
                .setTotalPages(page.getTotalPages())
                .setOverAllAmountOfElements(page.getOverAllAmountOfElements())
                .setPageSize(page.getPageSize())
                .setPageNumber(page.getPageNumber())
                .setData(page.getData().stream()
                        .map(thread -> new ThreadWithLikeCount(thread, likeCounts.get(thread)))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public ThreadWithLikeCount findById(long threadId) {
        return Optional.ofNullable(threadDao.findById(threadId))
                .map(thread -> new ThreadWithLikeCount(thread, thread.getLikeCount()))
                .orElse(null);
    }

    @Override
    public Thread create(String title, String threadBody, User creator) throws NoSuchEntityException {
        if (creator == null) {
            throw new IllegalArgumentException();
        }
        return threadDao.create(title, threadBody, creator);
    }

    @Override
    public void update(long threadId, String title, String threadBody, User updater) {
        if (updater == null) {
            throw new IllegalArgumentException();
        }
        final Thread thread = getThread(threadId);
        validateUpdatePermission(thread, updater);
        threadDao.update(thread, title, threadBody);
    }

    @Override
    public void delete(long threadId, User deleter) {
        if (deleter == null) {
            throw new IllegalArgumentException();
        }
        getThreadOptional(threadId).ifPresent(thread -> {
            validateDeletePermission(thread, deleter);
            threadDao.delete(thread);
        });
    }


    @Override
    public void likeThread(long threadId, User liker) {
        if (liker == null) {
            throw new IllegalArgumentException();
        }
        final Thread thread = getThread(threadId);
        // If already liked, do nothing and be idempotent
        if (!threadLikeDao.exists(thread, liker)) {
            threadLikeDao.create(thread, liker);
        }
    }

    @Override
    public void unlikeThread(long threadId, User unliker) {
        if (unliker == null) {
            throw new IllegalArgumentException();
        }
        final Thread thread = getThread(threadId);
        // If not liked, do nothing and be idempotent
        Optional.ofNullable(threadLikeDao.find(thread, unliker)).ifPresent(threadLikeDao::delete);
    }



    /*
     * Comments
     */

    @Override
    public Comment comment(long threadId, long commenterId, String comment) {
        final ThreadAndUserWrapper wrapper = getThreadAndUser(threadId, commenterId); // Checks existence
        threadDao.updateHotValue(wrapper.getThread());
        return commentDao.comment(wrapper.getThread(), wrapper.getUser(), comment);
    }

    @Override
    public Comment replyToComment(long commentId, long replierId, String reply) {
        final CommentAndUserWrapper wrapper = getCommentAndUser(commentId, replierId); // Checks existence
        threadDao.updateHotValue(wrapper.getComment().getThread());
        return commentDao.reply(wrapper.getComment(), wrapper.getUser(), reply);
    }

    @Override
    public void likeComment(long commentId, long userId) {
        final CommentAndUserWrapper wrapper = getCommentAndUser(commentId, userId); // Checks existence
        if (commentLikeDao.find(commentId, userId) != null) {
            commentLikeDao.create(wrapper.getComment(), wrapper.getUser());
        }
    }

    @Override
    public void unlikeComment(long commentId, long userId) {
        validateExistenceOfCommentAndUser(commentDao.findById(commentId), userDao.findById(userId));
        CommentLike like = commentLikeDao.find(commentId, userId);
        if (like != null) {
            commentLikeDao.delete(like);
        }
    }


    @Override
    public void editComment(long commentId, long userId, String newComment) {
        commentDao.update(getCommentAndUserCheckingAuthoring(commentId, userId).getComment(), newComment);
    }

    @Override
    public void deleteComment(long commentId, long userId) throws NoSuchEntityException {
        commentDao.delete(getCommentAndUserCheckingAuthoring(commentId, userId).getComment());
    }


    // ========================================


    /**
     * Retrieves the {@link Thread} with the given {@code threadId}.
     *
     * @param threadId The {@link Thread} id.
     * @return The {@link Thread} with the given {@code threadId}.
     * @throws NoSuchEntityException If no {@link Thread} exists with the given {@code threadId}.
     */
    private Thread getThread(long threadId) throws NoSuchEntityException {
        return getThreadOptional(threadId).orElseThrow(NoSuchEntityException::new);
    }

    /**
     * Retrieves a nullable {@link Optional} of {@link Thread} with the given {@code threadId}.
     *
     * @param threadId The {@link Thread} id.
     * @return The nullable {@link Optional}.
     */
    private Optional<Thread> getThreadOptional(long threadId) {
        return Optional.ofNullable(threadDao.findById(threadId));
    }

    /**
     * Checks that the given {@code updater} has permission to update the given {@link Thread}.
     *
     * @param thread  The {@link Thread} to be updated.
     * @param updater The {@link User} performing the operation.
     * @throws UnauthorizedException If the {@code updater} does not have permission to update the given {@code thread}.
     */
    private void validateUpdatePermission(final Thread thread, final User updater) throws UnauthorizedException {
        validatePermission(thread, updater, "update",
                (threadLambda, updaterUser) ->
                        Long.compare(threadLambda.getCreator().getId(), updaterUser.getId()) == 0);
    }

    /**
     * Checks that the given {@code deleter} has permission to delete the given {@link Thread}.
     *
     * @param thread  The {@link Thread} to be deleted.
     * @param deleter The {@link User} performing the operation.
     * @throws UnauthorizedException If the {@code deleter} does not have permission to delete the given {@code thread}.
     */
    private void validateDeletePermission(final Thread thread, final User deleter) throws UnauthorizedException {
        validatePermission(thread, deleter, "delete",
                (threadLambda, deleterUser) ->
                        Long.compare(threadLambda.getCreator().getId(), deleterUser.getId()) == 0);
    }

    /**
     * Validates that the given {@code operator} can perform the given {@code operationName}
     * over the given {@link Thread}, according to the given {@link BiPredicate}
     * (which must implement the {@link BiPredicate#test(Object, Object)} in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Thread}, or {@code false} otherwise).
     *
     * @param thread        The {@link Thread} to be operated over.
     * @param operator      The {@link User} who is operating over the {@link Thread}.
     * @param operationName A {@link String} indicating the operation being done (for informative purposes).
     * @param testFunction  A {@link BiPredicate} that implements logic to check if the operation is allowed.
     * @throws UnauthorizedException If the {@code operator} {@link User} does not have permission to operate over
     *                               the given {@link Thread}.
     * @implNote The {@link BiPredicate#test(Object, Object)} must be implemented in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Thread}, or {@code false} otherwise.
     */
    private void validatePermission(final Thread thread, final User operator,
                                    String operationName, BiPredicate<Thread, User> testFunction)
            throws UnauthorizedException {
        if (thread == null || operator == null) {
            throw new IllegalArgumentException("Thread and Operator must not be null");
        }
        if (testFunction.negate().test(thread, operator)) {
            throw new UnauthorizedException("User #" + operator.getId() + " does not have permission" +
                    " to " + operationName + " thread #" + thread.getId());
        }
    }





    /*
     * Helpers
     */


    /**
     * Creates a {@link ThreadAndUserWrapper} using the given {@code threadId} and {@code userId},
     * checking existence and authoring.
     * Upon success, it returns a {@link ThreadAndUserWrapper} containing the {@link Thread}
     * with the given {@code threadId}, and the {@link User} with the given {@code userId}.
     * Otherwise, an exception is thrown.
     *
     * @param threadId The thread id.
     * @param userId   The user id.
     * @return The wrapper object.
     * @throws NoSuchEntityException If no {@link Thread} exists with the given {@code threadId},
     *                               or if no {@link User} exists with the given {@code userId}.
     * @throws UnauthorizedException If the {@link User} with the given {@code userId} is not the creator
     *                               of the {@link Thread} with the given {@code threadId}
     */
    private ThreadAndUserWrapper getThreadAndUserCheckingAuthoring(long threadId, long userId)
            throws NoSuchEntityException, UnauthorizedException {
        return getThreadAndUser(threadId, userId).validateAuthoring(); // Checks existence and authoring
    }

    /**
     * Creates a {@link ThreadAndUserWrapper} using the given {@code threadId} and {@code userId}, checking existence.
     *
     * @param threadId The thread id.
     * @param userId   The user id.
     * @return The wrapper object.
     * @throws NoSuchEntityException If no {@link Thread} exists with the given {@code threadId},
     *                               or if no {@link User} exists with the given {@code userId}.
     */
    private ThreadAndUserWrapper getThreadAndUser(long threadId, long userId) throws NoSuchEntityException {
        final Thread thread = threadDao.findById(threadId);
        final User user = userDao.findById(userId);
        validateExistenceOfThreadAndUser(thread, user);

        return new ThreadAndUserWrapper(thread, user);
    }

    /**
     * Checks that the given {@link Thread} or the given {@link User} are not {@code null},
     * throwing a {@link NoSuchEntityException} containing those {@code null} fields in it.
     *
     * @param thread The {@link Thread} to be checked.
     * @param user   The {@link User} to be checked.
     * @throws NoSuchEntityException If the given {@link Thread} or the given {@link User} are null.
     */
    private void validateExistenceOfThreadAndUser(Thread thread, User user) throws NoSuchEntityException {
        List<String> missing = new LinkedList<>();
        if (thread == null) {
            missing.add("threadId");
        }
        if (user == null) {
            missing.add("userId");
        }
        if (!missing.isEmpty()) {
            throw new NoSuchEntityException(missing);
        }
    }


    /**
     * Creates a {@link CommentAndUserWrapper} using the given {@code commentId} and {@code userId},
     * checking existence and authoring.
     * Upon success, it returns a {@link CommentAndUserWrapper} containing the {@link Comment}
     * with the given {@code commentId}, and the {@link User} with the given {@code userId}.
     * Otherwise, an exception is thrown.
     *
     * @param commentId The comment id.
     * @param userId    The user id.
     * @return The wrapper object.
     * @throws NoSuchEntityException If no {@link Comment} exists with the given {@code commentId},
     *                               or if no {@link User} exists with the given {@code userId}.
     * @throws UnauthorizedException If the {@link User} with the given {@code userId} is not the commenter
     *                               of the {@link Comment} with the given {@code commentId}
     */
    private CommentAndUserWrapper getCommentAndUserCheckingAuthoring(long commentId, long userId)
            throws NoSuchEntityException, UnauthorizedException {
        return getCommentAndUser(commentId, userId).validateAuthoring();
    }

    /**
     * Creates a {@link CommentAndUserWrapper} using the given {@code commentId} and {@code userId}, checking existence.
     *
     * @param commentId The comment id.
     * @param userId    The user id.
     * @return The wrapper object.
     * @throws NoSuchEntityException If no {@link Comment} exists with the given {@code commentId},
     *                               or if no {@link User} exists with the given {@code userId}.
     */
    private CommentAndUserWrapper getCommentAndUser(long commentId, long userId) throws NoSuchEntityException {
        final Comment comment = commentDao.findById(commentId);
        final User user = userDao.findById(userId);
        validateExistenceOfCommentAndUser(comment, user);

        return new CommentAndUserWrapper(comment, user);
    }

    /**
     * Checks that the given {@link Comment} or the given {@link User} are not {@code null},
     * throwing a {@link NoSuchEntityException} containing those {@code null} fields in it.
     *
     * @param comment The {@link Comment} to be checked.
     * @param user    The {@link User} to be checked.
     * @throws NoSuchEntityException If the given {@link Comment} or the given {@link User} are null.
     */
    private void validateExistenceOfCommentAndUser(Comment comment, User user) throws NoSuchEntityException {
        List<String> missing = new LinkedList<>();
        if (comment == null) {
            missing.add("commentId");
        }
        if (user == null) {
            missing.add("userId");
        }
        if (!missing.isEmpty()) {
            throw new NoSuchEntityException(missing);
        }
    }


    /**
     * Class encapsulating a thread and a user.
     */
    private final static class ThreadAndUserWrapper {

        /**
         * The wrapped thread.
         */
        private final Thread thread;

        /**
         * The wrapper user.
         */
        private final User user;

        /**
         * Constructor.
         *
         * @param thread The thread to be wrapped.
         * @param user   The user to be wrapped.
         */
        private ThreadAndUserWrapper(Thread thread, User user) {
            this.thread = thread;
            this.user = user;
        }

        /**
         * Thread getter.
         *
         * @return The wrapped thread.
         */
        private Thread getThread() {
            return thread;
        }

        /**
         * User getter.
         *
         * @return The wrapped user.
         */
        private User getUser() {
            return user;
        }


        /**
         * Checks whether the wrapped {@link User} is the creator of the wrapped {@link Thread}.
         * If not, an {@link UnauthorizedException} is thrown.
         * Note that there is nothing wrong in wrapping a {@link Thread} and a {@link User} that is not the creator,
         * but this method just checks this for authoring purposes.
         *
         * @return this (for method chaining).
         * @throws UnauthorizedException If the wrapped {@link User} is not the creator of the wrapped {@link Thread}.
         */
        private ThreadAndUserWrapper validateAuthoring() throws UnauthorizedException {
            if (!user.equals(thread.getCreator())) {
                throw new UnauthorizedException("Thread #" + thread.getId() +
                        " does not belong to user #" + user.getId());
            }
            return this;
        }
    }

    /**
     * Class encapsulating a comment and a user.
     */
    private final static class CommentAndUserWrapper {

        /**
         * The wrapped comment.
         */
        private final Comment comment;

        /**
         * The wrapper user.
         */
        private final User user;

        /**
         * Constructor.
         *
         * @param comment The comment to be wrapped.
         * @param user    The user to be wrapped.
         */
        private CommentAndUserWrapper(Comment comment, User user) {
            this.comment = comment;
            this.user = user;
        }

        /**
         * Comment getter.
         *
         * @return The wrapped comment.
         */
        private Comment getComment() {
            return comment;
        }

        /**
         * User getter.
         *
         * @return The wrapped user.
         */
        private User getUser() {
            return user;
        }

        /**
         * Checks whether the wrapped {@link User} is the commenter of the wrapped {@link Comment}.
         * If not, an {@link UnauthorizedException} is thrown.
         * Note that there is nothing wrong in wrapping a {@link Comment} and a {@link User} that is not the commenter,
         * but this method just checks this for authoring purposes.
         *
         * @return this (for method chaining).
         * @throws UnauthorizedException If the wrapped {@link User} is not the commenter
         *                               of the wrapped {@link Comment}.
         */
        private CommentAndUserWrapper validateAuthoring() throws UnauthorizedException {
            if (!user.equals(comment.getCommenter())) {
                throw new UnauthorizedException("Comment #" + comment.getId() +
                        " does not belong to user #" + user.getId());
            }
            return this;
        }
    }


}
