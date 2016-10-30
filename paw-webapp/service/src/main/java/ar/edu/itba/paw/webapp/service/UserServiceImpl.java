package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    public UserServiceImpl() {}

    @Override
    public User create(String email, String password, String username) throws UserExistsException {
        return userDao.create(email, password, username);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public User findById(long id) {
        return userDao.findById(id);
    }

    @Override
    public boolean existsWithId(long id) {
        return userDao.existsWithId(id);
    }

    @Override
    public boolean existsWithUsername(String username) {
        return userDao.existsWithUsername(username);
    }

    @Override
    public boolean existsWithEmail(String email) {
        return userDao.existsWithEmail(email);
    }

    @Override
    public void scoreGame(User user, long gameId, int score) { userDao.scoreGame(user,gameId,score); }

    @Override
    public void scoreGame(User user, Game game, int score) { userDao.scoreGame(user,game,score); }

    @Override
    public void setPlayStatus(User user, long gameId, PlayStatus status) { userDao.setPlayStatus(user,gameId,status); }

    @Override
    public void setPlayStatus(User user, Game game, PlayStatus status) { userDao.setPlayStatus(user,game,status); }

    @Override
    public void removeScore(User u, long id) {
        userDao.removeScore(u,id);
    }

    @Override
    public void removeStatus(User u, long id) {
        userDao.removeStatus(u,id);
    }

    @Override
    public Collection<Game> recommendGames(User user) { return userDao.recommendGames(user); }
}
