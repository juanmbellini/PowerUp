package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


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
    public Thread create(String title, long creatorUserId, String threadBody) throws NoSuchEntityException {
        return threadDao.create(title, creatorUserId, threadBody);
    }

    @Override
    public void update(long threadId, String title, String threadBody, long userId) {
        Thread thread = checkThreadValuesAndAuthoring(threadId, userId);
        threadDao.update(thread, title, threadBody);
    }

    @Override
    public void deleteThread(long threadId, long userId) throws NoSuchEntityException {
        threadDao.deleteThread(checkThreadValuesAndAuthoring(threadId, userId));
    }


    @Override
    public Set<Thread> findRecent(int limit) {
        return threadDao.findRecent(limit);
    }

    @Override
    public Set<Thread> findBestPointed(int limit) {
        return threadDao.findBestPointed(limit);
    }

    @Override
    public Set<Thread> findHottest(int limit) {
        return threadDao.findHottest(limit);
    }

    @Override
    public Set<Thread> findByUserId(long id) {
        return threadDao.findByUserId(id);
    }

    @Override
    public Set<Thread> findByTitle(String title) {
        return threadDao.findByTitle(title);
    }

    @Override
    public Thread findById(long threadId) {
        return threadDao.findById(threadId);
    }

    /*
     * Likes
     */

    @Override
    public int likeThread(long threadId, long userId) {
        ThreadLike threadLike = checkThreadLikeValues(threadId, userId);
        Thread thread = threadDao.findById(threadId); // Not null, as the checker would have thrown exception
        int count = thread.getLikeCount();
        if (threadLike == null) {
            // When null, the user with the given userId didn't like the thread with the given id
            User user = userDao.findById(userId); // Not null, as the checker would have thrown exception
            threadLikeDao.create(thread, user);
            ++count;
        }
        return count;
    }

    @Override
    public int unlikeThread(long threadId, long userId) {
        ThreadLike threadLike = checkThreadLikeValues(threadId, userId);
        int count = threadDao.findById(threadId).getLikeCount(); // Not null, as the checker would have thrown exception
        if (threadLike != null) {
            // When not null, the user with the given userId has liked the thread with the given id
            threadLikeDao.delete(threadLike);
            --count;
        }
        return count;
    }



    /*
     * Comments
     */

    @Override
    public Comment comment(long threadId, long commenterId, String comment) {
        Thread thread = checkThreadValues(threadId, commenterId);
        threadDao.updateHotValue(thread);
        return commentDao.comment(thread, thread.getCreator(), comment);
    }

    @Override
    public Comment replyToComment(long commentId, long replierId, String reply) {
        Comment comment = checkCommentValues(commentId, replierId);
        // TODO: check cycle?
        threadDao.updateHotValue(comment.getThread());
        return commentDao.reply(comment, comment.getCommenter(), reply); // TODO: modificar para que reciba los objetos
    }

    @Override
    public int likeComment(long id, long userId) {
        CommentLike commentLike = checkCommentLikeValues(id, userId);
        Comment comment = commentDao.findById(id); // Not null, as the checker would have thrown exception
        int count = comment.getLikeCount();
        if (commentLike == null) {
            // When null, the user with the given userId didn't like the comment with the given id
            User user = userDao.findById(userId); // Not null, as the checker would have thrown exception
            commentLikeDao.create(comment, user);
            ++count;
        }
        return count;
    }

    @Override
    public int unlikeComment(long id, long userId) {
        CommentLike commentLike = checkCommentLikeValues(id, userId);
        int count = commentDao.findById(id).getLikeCount(); // Not null, as the checker would have thrown exception
        if (commentLike != null) {
            // When not null, the user with the given userId has liked the comment with the given id
            commentLikeDao.delete(commentLike);
            --count;
        }
        return count;
    }


    @Override
    public void editComment(long commentId, long userId, String newComment) {
        Comment comment = checkCommentValuesAndAuthoring(commentId, userId);
        commentDao.update(comment, newComment);
    }

    @Override
    public void deleteComment(long commentId, long userId) throws NoSuchEntityException {
        Comment comment = checkCommentValuesAndAuthoring(commentId, userId);
        commentDao.delete(comment);
    }



    /*
     * Helpers
     */

    /**
     * Checks if the values are correct (including authoring).
     * Upon success,  it returns the {@link Thread} with the given {@code threadId}. Otherwise, an exception is thrown.
     *
     * @param threadId The thread id.
     * @param userId   The user id.
     * @return The thread with the given {@code threadId}
     */
    private Thread checkThreadValuesAndAuthoring(long threadId, long userId) {
        Thread thread = checkThreadValues(threadId, userId);
        if (userId != thread.getCreator().getId()) {
            throw new UnauthorizedException("Thread #" + threadId + " does not belong to user #" + userId
                    + ", can't delete");
        }
        return thread;
    }

    /**
     * Checks if the values are correct (without authoring).
     * Upon success,  it returns the {@link Thread} with the given {@code threadId}. Otherwise, an exception is thrown.
     *
     * @param threadId The thread id.
     * @param userId   The user id.
     * @return The thread with the given {@code threadId}
     */
    private Thread checkThreadValues(long threadId, long userId) {
        Thread thread = threadDao.findById(threadId);
        User user = userDao.findById(userId);
        // TODO: Use a collection to save which ones are conflicting
        if (thread == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        if (user == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        return thread;
    }

    /**
     * Checks if the values are correct.
     * Upon success,  it returns the {@link ThreadLike} with the given {@code threadId}.
     * Otherwise, an exception is thrown.
     *
     * @param threadId The thread id.
     * @param userId   The user id.
     * @return The {@link ThreadLike} done to the {@link Thread} with the given {@code threadId}.
     */
    private ThreadLike checkThreadLikeValues(long threadId, long userId) {
        Thread thread = threadDao.findById(threadId);
        User user = userDao.findById(userId);
        // TODO: Use a collection to save which ones are conflicting
        if (thread == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        if (user == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        return threadLikeDao.find(threadId, userId);
    }


    /**
     * Checks if the values are correct.
     * Upon success,  it returns the {@link Comment} with the given {@code commentId}. Otherwise, an exception is thrown.
     *
     * @param commentId The thread id.
     * @param userId    The user id.
     * @return The comment with the given {@code commentId}
     */
    private Comment checkCommentValuesAndAuthoring(long commentId, long userId) {
        Comment comment = checkCommentValues(commentId, userId);
        if (userId != comment.getCommenter().getId()) {
            throw new UnauthorizedException("Comment #" + commentId + " does not belong to user #" + userId);
        }
        return comment;
    }

    /**
     * Checks if the values are correct.
     * Upon success,  it returns the {@link Comment} with the given {@code commentId}. Otherwise, an exception is thrown.
     *
     * @param commentId The thread id.
     * @param userId    The user id.
     * @return The comment with the given {@code commentId}
     */
    private Comment checkCommentValues(long commentId, long userId) {
        Comment comment = commentDao.findById(commentId);
        User user = userDao.findById(userId);
        // TODO: Use a collection to save which ones are conflicting
        if (comment == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        if (user == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        return comment;
    }


    /**
     * Checks if the values are correct.
     * Upon success,  it returns the {@link CommentLike} with the given {@code commentId}. Otherwise, an exception is thrown.
     *
     * @param commentId The thread id.
     * @param userId    The user id.
     * @return The {@link CommentLike} done to the {@link Comment} with the given {@code commentId}.
     */
    private CommentLike checkCommentLikeValues(long commentId, long userId) {
        Comment comment = commentDao.findById(commentId);
        User user = userDao.findById(userId);
        // TODO: Use a collection to save which ones are conflicting
        if (comment == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        if (user == null) {
            throw new NoSuchEntityException(); // TODO: avisar que objeto está faltando
        }
        return commentLikeDao.find(commentId, userId);
    }


}
