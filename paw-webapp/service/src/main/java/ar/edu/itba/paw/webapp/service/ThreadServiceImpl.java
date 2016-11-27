package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.CommentDao;
import ar.edu.itba.paw.webapp.interfaces.ThreadDao;
import ar.edu.itba.paw.webapp.interfaces.ThreadService;
import ar.edu.itba.paw.webapp.model.Comment;
import ar.edu.itba.paw.webapp.model.Thread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@Transactional
public class ThreadServiceImpl implements ThreadService {

    private final ThreadDao threadDao;

    private final CommentDao commentDao;

    @Autowired
    public ThreadServiceImpl(ThreadDao threadDao, CommentDao commentDao) {
        this.threadDao = threadDao;
        this.commentDao = commentDao;
    }

    @Override
    public Thread create(String title, long creatorUserId, String creatorComment) throws NoSuchEntityException {
        return threadDao.create(title, creatorUserId, creatorComment);
    }

    @Override
    public Set<Thread> findRecent(int limit) {
        return threadDao.findRecent(limit);
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

    @Override
    public void changeTitle(long threadId, String newTitle) throws NoSuchEntityException, IllegalArgumentException {
        threadDao.changeTitle(threadId, newTitle);
    }

    @Override
    public int likeThread(long threadId, long userId) {
        return threadDao.likeThread(threadId, userId);
    }

    @Override
    public int unlikeThread(long threadId, long userId) {
        return threadDao.unlikeThread(threadId, userId);
    }

    @Override
    public Comment comment(long threadId, long commenterId, String comment) {
        return threadDao.comment(threadId, commenterId, comment);
    }

    @Override
    public Comment replyToComment(long commentId, long replierId, String reply) {
        return threadDao.replyToComment(commentId, replierId, reply);
    }

    @Override
    public int likeComment(long id, long userId) {
        return threadDao.likeComment(id, userId);
    }

    @Override
    public int unlikeComment(long id, long userId) {
        return threadDao.unlikeComment(id, userId);
    }

    @Override
    public void editComment(long commentId, long userId, String newComment) {
        if(commentDao.findById(commentId).getCommenter().getId() != userId) {
            throw new UnauthorizedException("Comment #" + commentId + " does not belong to user #" + userId + ", can't edit");
        }
        threadDao.editComment(commentId, newComment);
    }

    @Override
    public void deleteComment(long commentId, long userId) throws NoSuchEntityException {
        if(commentDao.findById(commentId).getCommenter().getId() != userId) {
            throw new UnauthorizedException("Comment #" + commentId + " does not belong to user #" + userId + ", can't delete");
        }
        threadDao.deleteComment(commentId);
    }

    @Override
    public void deleteThread(long threadId) throws NoSuchEntityException {
        threadDao.deleteThread(threadId);
    }
}
