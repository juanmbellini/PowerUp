package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameService;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.interfaces.UserService;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    //TODO consider making this a HibernateUserDao type so we can remove the bindToCurrentTransaction method from the interface, it doesn't belong there
    private UserDao userDao;

    @Autowired
    private GameService gameService;

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
    public Set<Game> getGamesByStatus(User user, PlayStatus status) {
        user = userDao.bindToCurrentTransaction(user);
        final Set<Game> result = new LinkedHashSet<>();
        for(Map.Entry<Long, PlayStatus> entry : user.getPlayStatuses().entrySet()) {
            if(entry.getValue() == status) {
                result.add(gameService.findById(entry.getKey()));
            }
        }
        return result;
    }

    public boolean hasScoredGame(User user, long gameId) {
        user = userDao.bindToCurrentTransaction(user);
        return user.getScoredGames().containsKey(gameId);
    }
    public int getGameScore(User user, long gameId) {
        user = userDao.bindToCurrentTransaction(user);
        if(!user.hasScoredGame(gameId)) {
            throw new IllegalArgumentException(user.getUsername() + " has not scored game with ID " + gameId);
        }
        return user.getScoredGames().get(gameId);
    }

    public PlayStatus getPlayStatus(User user, long gameId) {
        user = userDao.bindToCurrentTransaction(user);
        if(!user.hasPlayStatus(gameId)) {
            throw new IllegalArgumentException(user.getUsername() + " has no play status for game with ID " + gameId);
        }
        return user.getPlayStatus(gameId);
    }

    public boolean hasPlayStatus(User user, long gameId){
        user = userDao.bindToCurrentTransaction(user);
        return user.getPlayStatuses().containsKey(gameId);
    }
    @Override
    public Map<Game, Integer> getScoredGames(User user) {
        user = userDao.bindToCurrentTransaction(user);
        final Map<Game, Integer> result = new LinkedHashMap<>();
        final Map<Long, Integer> scores = user.getScoredGames();
        for(long gameId : scores.keySet()) {
            result.put(gameService.findById(gameId), scores.get(gameId));
        }
        return result;
    }

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
