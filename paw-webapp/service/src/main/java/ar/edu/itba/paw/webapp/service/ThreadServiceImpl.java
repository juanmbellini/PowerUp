package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.model_interfaces.Like;
import ar.edu.itba.paw.webapp.model_wrappers.CommentableAndLikeableWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;


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
    public Page<CommentableAndLikeableWrapper<Thread>> getThreads(String titleFilter, Long userIdFilter,
                                                                  String usernameFilter, int pageNumber, int pageSize,
                                                                  ThreadDao.SortingType sortingType,
                                                                  SortDirection sortDirection, User currentUser) {
        final Page<Thread> page = threadDao.getThreads(titleFilter, userIdFilter, usernameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<Thread, Long> commentCounts = commentDao.countComments(page.getData());
        final Map<Thread, Long> likeCounts = threadLikeDao.countLikes(page.getData());
        final Map<Thread, Boolean> userLikes = Optional.ofNullable(currentUser)
                .map(user -> threadLikeDao.likedBy(page.getData(), user)).orElse(new HashMap<>());

        return ServiceHelper.fromAnotherPage(page, (thread) -> new CommentableAndLikeableWrapper.Builder<Thread>()
                .setEntity(thread)
                .setCommentCount(commentCounts.get(thread))
                .setLikeCount(likeCounts.get(thread))
                .setLikedByCurrentUser(userLikes.get(thread))
                .build()
        ).build();
    }

    @Override
    public CommentableAndLikeableWrapper<Thread> findById(long threadId, User currentUser) {
        return Optional.ofNullable(threadDao.findById(threadId))
                .map(thread -> new CommentableAndLikeableWrapper.Builder<Thread>()
                        .setEntity(thread)
                        .setCommentCount(Optional.ofNullable(commentDao
                                .countComments(Collections.singletonList(thread)))
                                .map(map -> map.get(thread)).orElseThrow(IllegalStateException::new))
                        .setLikedByCurrentUser(Optional.ofNullable(currentUser)
                                .map(user -> threadLikeDao.exists(thread, user)).orElse(null)).build())
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
        validateThreadUpdatePermission(thread, updater);
        threadDao.update(thread, title, threadBody);
    }

    @Override
    public void delete(long threadId, User deleter) {
        if (deleter == null) {
            throw new IllegalArgumentException();
        }
        getThreadOptional(threadId).ifPresent(thread -> {
            validateThreadDeletePermission(thread, deleter);
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

    @Override
    public Page<User> getUsersLikingTheThread(long threadId, int pageNumber, int pageSize,
                                              ThreadLikeDao.SortingType sortingType, SortDirection sortDirection) {
        final Thread thread = getThread(threadId);
        final Page<ThreadLike> page = threadLikeDao.getLikes(thread, pageNumber, pageSize, sortingType, sortDirection);
        return createLikersPage(page);
    }

    /*
     * Comments
     */

    @Override
    public Page<CommentableAndLikeableWrapper<Comment>> getThreadComments(long threadId, int pageNumber, int pageSize,
                                                                          CommentDao.SortingType sortingType,
                                                                          SortDirection sortDirection,
                                                                          User currentUser) {
        final Thread thread = getThread(threadId); // Throws NoSuchEntityException if not exists
        final Page<Comment> page = commentDao.getThreadComments(thread, pageNumber, pageSize,
                sortingType, sortDirection);
        final Map<Comment, Long> commentCounts = commentDao.countReplies(page.getData());
        final Map<Comment, Long> likeCounts = commentLikeDao.countLikes(page.getData());
        final Map<Comment, Boolean> userLikes = Optional.ofNullable(currentUser)
                .map(user -> commentLikeDao.likedBy(page.getData(), user)).orElse(new HashMap<>());
        return ServiceHelper.fromAnotherPage(page, (comment) -> new CommentableAndLikeableWrapper.Builder<Comment>()
                .setEntity(comment)
                .setCommentCount(commentCounts.get(comment))
                .setLikeCount(likeCounts.get(comment))
                .setLikedByCurrentUser(userLikes.get(comment))
                .build()
        ).build();

    }

    @Override
    public CommentableAndLikeableWrapper<Comment> findCommentById(long commentId, User currentUser) {
        return Optional.ofNullable(commentDao.findById(commentId))
                .map(comment -> new CommentableAndLikeableWrapper.Builder<Comment>()
                        .setEntity(comment)
                        .setCommentCount(Optional.ofNullable(commentDao
                                .countReplies(Collections.singletonList(comment)))
                                .map(map -> map.get(comment)).orElseThrow(IllegalStateException::new))
                        .setLikedByCurrentUser(Optional.ofNullable(currentUser)
                                .map(user -> commentLikeDao.exists(comment, user)).orElse(null)).build())
                .orElse(null);
    }

    @Override
    public Comment comment(long threadId, String body, User commenter) {
        if (commenter == null) {
            throw new IllegalArgumentException();
        }
        final Thread thread = getThread(threadId);
        threadDao.updateHotValue(thread);
        return commentDao.comment(thread, body, commenter);
    }

    @Override
    public void editComment(long commentId, String newBody, User updater) {
        final Comment comment = getComment(commentId);
        validateCommentUpdatePermission(comment, updater);
        commentDao.update(comment, newBody);
    }

    @Override
    public void deleteComment(long commentId, User deleter) throws NoSuchEntityException {
        getCommentOptional(commentId).ifPresent(comment -> {
            validateCommentDeletePermission(comment, deleter);
            commentDao.delete(comment);
        });
    }

    @Override
    public Page<CommentableAndLikeableWrapper<Comment>> getCommentReplies(long commentId, int pageNumber, int pageSize,
                                                                          CommentDao.SortingType sortingType,
                                                                          SortDirection sortDirection,
                                                                          User currentUser) {
        final Comment comment = getComment(commentId); // Throws NoSuchEntityException if not exists
        final Page<Comment> page = commentDao.getCommentReplies(comment, pageNumber, pageSize,
                sortingType, sortDirection);
        final Map<Comment, Long> commentCounts = commentDao.countReplies(page.getData());
        final Map<Comment, Long> likeCounts = commentLikeDao.countLikes(page.getData());
        final Map<Comment, Boolean> userLikes = Optional.ofNullable(currentUser)
                .map(user -> commentLikeDao.likedBy(page.getData(), user)).orElse(new HashMap<>());

        return ServiceHelper.fromAnotherPage(page, (commentLambda) ->
                new CommentableAndLikeableWrapper.Builder<Comment>()
                        .setEntity(commentLambda)
                        .setCommentCount(commentCounts.get(commentLambda))
                        .setLikeCount(likeCounts.get(commentLambda))
                        .setLikedByCurrentUser(userLikes.get(commentLambda))
                        .build()
        ).build();
    }

    @Override
    public Comment replyToComment(long commentId, String body, User replier) {
        if (replier == null) {
            throw new IllegalArgumentException();
        }
        final Comment comment = getComment(commentId);
        threadDao.updateHotValue(comment.getThread());
        return commentDao.reply(comment, body, replier);
    }


    @Override
    public void likeComment(long commentId, User liker) {
        if (liker == null) {
            throw new IllegalArgumentException();
        }
        final Comment comment = getComment(commentId);
        // If already liked, do nothing and be idempotent
        if (!commentLikeDao.exists(comment, liker)) {
            commentLikeDao.create(comment, liker);
        }
    }

    @Override
    public void unlikeComment(long commentId, User unliker) {
        if (unliker == null) {
            throw new IllegalArgumentException();
        }
        final Comment comment = getComment(commentId);
        // If not liked, do nothing and be idempotent
        Optional.ofNullable(commentLikeDao.find(comment, unliker)).ifPresent(commentLikeDao::delete);
    }

    @Override
    public Page<User> getUsersLikingTheComment(long commentId, int pageNumber, int pageSize,
                                               CommentLikeDao.SortingType sortingType, SortDirection sortDirection) {
        final Comment comment = getComment(commentId);
        final Page<CommentLike> page = commentLikeDao
                .getLikes(comment, pageNumber, pageSize, sortingType, sortDirection);
        return createLikersPage(page);
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
    private void validateThreadUpdatePermission(final Thread thread, final User updater) throws UnauthorizedException {
        validateThreadPermission(thread, updater, "update",
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
    private void validateThreadDeletePermission(final Thread thread, final User deleter) throws UnauthorizedException {
        validateThreadPermission(thread, deleter, "delete",
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
    private void validateThreadPermission(final Thread thread, final User operator,
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


    /**
     * Retrieves the {@link Comment} with the given {@code commentId}.
     *
     * @param commentId The {@link Comment} id.
     * @return The {@link Comment} with the given {@code commentId}.
     * @throws NoSuchEntityException If no {@link Comment} exists with the given {@code commentId}.
     */
    private Comment getComment(long commentId) throws NoSuchEntityException {
        return getCommentOptional(commentId).orElseThrow(NoSuchEntityException::new);
    }


    /**
     * Retrieves a nullable {@link Optional} of {@link Comment} with the given {@code commentId}.
     *
     * @param commentId The {@link Comment} id.
     * @return The nullable {@link Optional}.
     */
    private Optional<Comment> getCommentOptional(long commentId) {
        return Optional.ofNullable(commentDao.findById(commentId));
    }

    /**
     * Creates a new {@link Page} of {@link User} according to the given {@code oldPage} of {@link Like}.
     *
     * @param oldPage The old {@link Page} from which data is taken.
     * @return The new {@link Page}.
     */
    private Page<User> createLikersPage(Page<? extends Like> oldPage) {
        return ServiceHelper.fromAnotherPage(oldPage, Like::getUser).build();
    }


    /**
     * Checks that the given {@code updater} has permission to update the given {@link Comment}.
     *
     * @param comment The {@link Comment} to be updated.
     * @param updater The {@link User} performing the operation.
     * @throws UnauthorizedException If the {@code updater} does not have permission
     *                               to update the given {@code comment}.
     */
    private void validateCommentUpdatePermission(final Comment comment, final User updater)
            throws UnauthorizedException {
        validateCommentPermission(comment, updater, "update",
                (commentLambda, updaterUser) ->
                        Long.compare(commentLambda.getCommenter().getId(), updaterUser.getId()) == 0);
    }

    /**
     * Checks that the given {@code deleter} has permission to delete the given {@link Comment}.
     *
     * @param comment The {@link Comment} to be deleted.
     * @param deleter The {@link User} performing the operation.
     * @throws UnauthorizedException If the {@code deleter} does not have permission
     *                               to delete the given {@code comment}.
     */
    private void validateCommentDeletePermission(final Comment comment, final User deleter)
            throws UnauthorizedException {
        validateCommentPermission(comment, deleter, "delete",
                (commentLambda, deleterUser) ->
                        Long.compare(commentLambda.getCommenter().getId(), deleterUser.getId()) == 0);
    }

    /**
     * Validates that the given {@code operator} can perform the given {@code operationName}
     * over the given {@link Comment}, according to the given {@link BiPredicate}
     * (which must implement the {@link BiPredicate#test(Object, Object)} in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Comment}, or {@code false} otherwise).
     *
     * @param comment       The {@link Comment} to be operated over.
     * @param operator      The {@link User} who is operating over the {@link Comment}.
     * @param operationName A {@link String} indicating the operation being done (for informative purposes).
     * @param testFunction  A {@link BiPredicate} that implements logic to check if the operation is allowed.
     * @throws UnauthorizedException If the {@code operator} {@link User} does not have permission to operate over
     *                               the given {@link Comment}.
     * @implNote The {@link BiPredicate#test(Object, Object)} must be implemented in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Comment}, or {@code false} otherwise.
     */
    private void validateCommentPermission(final Comment comment, final User operator,
                                           String operationName, BiPredicate<Comment, User> testFunction)
            throws UnauthorizedException {
        if (comment == null || operator == null) {
            throw new IllegalArgumentException("Comment and Operator must not be null");
        }
        if (testFunction.negate().test(comment, operator)) {
            throw new UnauthorizedException("User #" + operator.getId() + " does not have permission" +
                    " to " + operationName + " comment #" + comment.getId());
        }
    }

}
