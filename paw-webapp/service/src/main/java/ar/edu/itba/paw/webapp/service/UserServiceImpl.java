package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
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

/**
 * Implementation of user service layer.
 * <b>WARNING:</b> Most of these methods may throw {@link NoSuchUserException} if an invalid user ID is provided.
 */
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
    public void scoreGame(long userId, long gameId, int score) {
        userDao.scoreGame(userId, gameId, score);
    }

    @Override
    public Set<Game> getGamesByStatus(long userId, PlayStatus status) {
        User user = getFreshUser(userId);
        final Set<Game> result = new LinkedHashSet<>();
        for (Map.Entry<Long, PlayStatus> entry : user.getPlayStatuses().entrySet()) {
            if (entry.getValue() == status) {
                result.add(gameService.findById(entry.getKey()));
            }
        }
        return result;
    }

    @Override
    public boolean hasScoredGame(long userId, long gameId) {
        return getScoredGames(userId).containsKey(gameService.findById(gameId));
    }

    @Override
    public int getGameScore(long userId, long gameId) {
        User user = getFreshUser(userId);
        if (!user.hasScoredGame(gameId)) {
            throw new IllegalArgumentException(user.getUsername() + " has not scored game with ID " + gameId);
        }
        return user.getScoredGames().get(gameId);
    }

    @Override
    public PlayStatus getPlayStatus(long userId, long gameId) {
        User user = getFreshUser(userId);
        if (!user.hasPlayStatus(gameId)) {
            throw new IllegalArgumentException(user.getUsername() + " has no play status for game with ID " + gameId);
        }
        return user.getPlayStatus(gameId);
    }

    @Override
    public boolean hasPlayStatus(long userId, long gameId) {
        User user = getFreshUser(userId);
        return user.getPlayStatuses().containsKey(gameId);
    }

    @Override
    public Map<Game, Integer> getScoredGames(long userId) {
        User user = getFreshUser(userId);
        final Map<Game, Integer> result = new LinkedHashMap<>();
        final Map<Long, Integer> scores = user.getScoredGames();
        for (long gameId : scores.keySet()) {
            result.put(gameService.findById(gameId), scores.get(gameId));
        }
        return result;
    }

    @Override
    public Map<Integer, Set<Long>> getScoredGamesRev(long userId) {
        User user = getFreshUser(userId);
        final Map<Integer, Set<Long>> result = new HashMap<>();
        for (Map.Entry<Long, Integer> entry : user.getScoredGames().entrySet()) {
            long gameId = entry.getKey();
            int score = entry.getValue();
            if(!result.containsKey(score)) {
                result.put(score, new LinkedHashSet<>());
            }
            result.get(score).add(gameId);
        }
        return result;
    }

    @Override
    public void setPlayStatus(long userId, long gameId, PlayStatus status) {
        userDao.setPlayStatus(userId, gameId, status);
    }

    @Override
    public void removeScore(long userId, long gameId) {
        userDao.removeScore(userId, gameId);
    }

    @Override
    public void removeStatus(long userId, long gameId) {
        userDao.removeStatus(userId, gameId);
    }

    @Override
    public Collection<Game> recommendGames(long userId) {
        return userDao.recommendGames(userId);
    }

    @Override
    public Map<PlayStatus, Set<Game>> getGameList(long userId) {
        User user = getFreshUser(userId);
        Map<PlayStatus, Set<Game>> result = new HashMap<>();
        for(Map.Entry<Long, PlayStatus> entry : user.getPlayStatuses().entrySet()) {
            Game game = gameService.findById(entry.getKey());
            PlayStatus status = entry.getValue();
            if(!result.containsKey(status)) {
                result.put(status, new LinkedHashSet<>());
            }
            result.get(status).add(game);
        }
        return result;
    }

    /**
     * Gets a user by the specified ID that is transaction-safe (i.e. lazily-initialized collections can be accessed)
     * and throws exception if not found. Helper method used to reduce code in other service methods.
     *
     * @param userId The ID of the user to fetch.
     * @return The found user.
     * @throws NoSuchUserException If no such user exists.
     */
    private User getFreshUser(long userId) {
        User result = findById(userId);
        if (result == null) {
            throw new NoSuchUserException(userId);
        }
        return result;
    }
}