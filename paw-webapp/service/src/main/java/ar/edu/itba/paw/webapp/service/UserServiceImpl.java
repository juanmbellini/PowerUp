package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of user service layer.
 * <b>WARNING:</b> Most of these methods may throw {@link NoSuchUserException} if an invalid user ID is provided.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService, ValidationExceptionThrower, NoSuchEntityThrower,
        ExistenceByIdChecker<User> {


    private final UserDao userDao;

    private final GameDao gameDao;

    private final ShelfDao shelfDao;


    @Autowired
    public UserServiceImpl(UserDao userDao, GameDao gameDao, ShelfDao shelfDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.shelfDao = shelfDao;
    }


    @Override
    public Page<User> getUsers(String usernameFilter, String emailFilter, Authority authorityFilter,
                               int pageNumber, int pageSize,
                               UserDao.SortingType sortingType, SortDirection sortDirection) {
        return userDao.getUsers(usernameFilter, emailFilter, authorityFilter,
                pageNumber, pageSize, sortingType, sortDirection);
    }

    @Override
    public User findById(long id) {
        return userDao.findById(id);
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
    public User create(String username, String email, String password) {
        List<ValueError> errorList = new LinkedList<>();
        if (userDao.findByUsername(username) != null) {
            errorList.add(USERNAME_IN_USE);
        }
        if (userDao.findByEmail(email) != null) {
            errorList.add(EMAIL_IN_USE);
        }
        // TODO: create methods to check existence using count instead of select
        throwValidationException(errorList);

        return userDao.create(username, email, password);
    }


    @Override
    public void changePassword(long userId, String newPassword, long updaterId) {
        userDao.changePassword(checkUserValuesAndAuthoring(userId, updaterId), newPassword);
    }

    @Override
    public void changeProfilePicture(long userId, byte[] picture, String mimeType, long updaterId) {
        userDao.changeProfilePicture(checkUserValuesAndAuthoring(userId, updaterId), picture, mimeType);
    }

    @Override
    public void removeProfilePicture(long userId, long updaterId) {
        changeProfilePicture(userId, null, null, updaterId);
    }


    @Override
    public Page<UserGameStatus> getPlayStatuses(long userId, Long gameIdFilter, String gameNameFilter,
                                                int pageNumber, int pageSize,
                                                UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                                SortDirection sortDirection) {
        return userDao.getPlayStatuses(checkUserExistence(userId), gameIdFilter, gameNameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
    }


    @Override
    public void setPlayStatus(long userId, Long gameId, PlayStatus status, long updaterId) {
        checkPresentGameId(gameId);
        userDao.setPlayStatus(checkUserValuesAndAuthoring(userId, updaterId), gameDao.findById(gameId), status);
    }

    @Override
    public void removePlayStatus(long userId, Long gameId, long updaterId) {
        checkPresentGameId(gameId);
        userDao.removePlayStatus(checkUserValuesAndAuthoring(userId, updaterId), gameDao.findById(gameId));
    }

    @Override
    public Page<UserGameScore> getGameScores(long userId, Long gameIdFilter, String gameNameFilter,
                                             int pageNumber, int pageSize,
                                             UserDao.PlayStatusAndGameScoresSortingType sortingType,
                                             SortDirection sortDirection) {
        return userDao.getGameScores(checkUserExistence(userId), gameIdFilter, gameNameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
    }

    @Override
    public void setGameScore(long userId, long gameId, Integer score, long updaterId) {
        checkPresentGameId(gameId);
        userDao.setGameScore(checkUserValuesAndAuthoring(userId, updaterId), gameDao.findById(gameId), score);
        gameDao.updateAvgScore(gameId); // TODO: receive object
    }

    @Override
    public void removeGameScore(long userId, long gameId, long updaterId) {
        checkPresentGameId(gameId);
        userDao.removeGameScore(checkUserValuesAndAuthoring(userId, updaterId), gameDao.findById(gameId));
        gameDao.updateAvgScore(gameId); // TODO: receive object
    }


    @Override
    public void addAuthority(long userId, Authority authority, long updaterId) {
        userDao.addAuthority(checkUserValuesAndAuthoring(userId, updaterId), authority);
    }

    @Override
    public void removeAuthority(long userId, Authority authority, long updaterId) {
        userDao.removeAuthority(checkUserValuesAndAuthoring(userId, updaterId), authority); // TODO: check USER is not removed?
    }

    @Override
    public void delete(long userId, long deleterId) {

    }

    @Override
    public Collection<Game> recommendGames(long userId) {
        return userDao.recommendGames(userId);
    }

    @Override
    public Collection<Game> recommendGames(long userId, List<String> shelfNameFilters) {
        final User user = Optional.ofNullable(userDao.findById(userId)).orElseThrow(NoSuchEntityException::new);
        final Set<Shelf> shelves = Optional.ofNullable(shelfNameFilters)
                .map(list -> list.stream()
                        .map(name -> shelfDao.findByName(user, name)) // Map each name to a shelf
                        .filter(each -> each != null) // Remove those that are null
                        .collect(Collectors.toSet())) // Store shelves into set
                .orElse(new HashSet<>()); // If list of names is null, return an empty hash set.
        return userDao.recommendGames(userId, shelves);
    }

    @Override
    public Page<UserGameStatus> getGameList(long userId, int pageNumber, int pageSize, UserDao.PlayStatusAndGameScoresSortingType sortingType, SortDirection sortDirection) {
        return userDao.getGameList(checkUserExistence(userId), pageNumber, pageSize, sortingType, sortDirection);
    }


    /**
     * Returns a new randomly generated password.
     * Credits to erickson, a StackOverflow user.
     *
     * @return The generated password.
     */
    public String generateNewPassword() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(8);
    }

    /*
     * Helpers
     */


    /**
     * Checks that the game id is not missing.
     *
     * @param gameId The game id to be checked.
     * @throws ValidationException If the game id is not present.
     */
    private void checkPresentGameId(Long gameId) throws ValidationException {
        if (gameId == null) {
            throwValidationException(Stream.of(MISSING_GAME_ID).collect(Collectors.toList()));
        }
    }

    /**
     * Checks that a {@link User} with the given {@code userId} exists.
     *
     * @param userId The user id.
     * @return The User with the given {@code userId}.
     * @throws NoSuchEntityException If the user does not exist.
     */
    private User checkUserExistence(long userId) throws NoSuchEntityException {
        List<String> missing = new LinkedList<>();
        User user = checkExistenceById(userId, missing, "userId", userDao);
        throwNoSuchEntityException(missing);
        return user;
    }


    /**
     * Checks if the values are correct, including authoring (i.e userId == operatorId).
     * Upon success,  it returns the {@link User} with the given {@code userId}. Otherwise, an exception is thrown.
     *
     * @param userId     The user id.
     * @param operatorId The operator id.
     * @return The {@link User} with the given {@code userId}.
     * @throws NoSuchEntityException If no {@link User} exists with the given {@code userId},
     *                               or if no {@link User} exists with the given {@code operatorId}.
     * @throws UnauthorizedException If the {@link User} with the given {@code operatorId}
     *                               is not the same {@link User} with the given {@code userId}.
     */
    private User checkUserValuesAndAuthoring(long userId, long operatorId) throws NoSuchEntityException,
            UnauthorizedException {
        User user = checkUserValues(userId, operatorId);
        if (operatorId != user.getId()) {
            throw new UnauthorizedException("User #" + userId + " is not the same user #" + operatorId);
        }
        return user;
    }


    /**
     * Checks if the values are correct (without authoring).
     * Upon success,  it returns the {@link User} with the given {@code operatorId}. Otherwise, an exception is thrown.
     *
     * @param userId     The user id.
     * @param operatorId The operator id.
     * @return The {@link User} with the given {@code userId}.
     * @throws NoSuchEntityException If no {@link User} exists with the given {@code operatorId},
     *                               or if no {@link User} exists with the given {@code operatorId}.
     */
    private User checkUserValues(long userId, long operatorId) throws NoSuchEntityException {
        User user = userDao.findById(userId);
        List<String> missing = new LinkedList<>();
        checkExistenceById(userId, missing, "userId", userDao);
        checkExistenceById(operatorId, missing, "operatorId", userDao);
        throwNoSuchEntityException(missing);
        return user;
    }


    private static final ValueError USERNAME_IN_USE =
            new ValueError(ValueError.ErrorCause.ALREADY_EXISTS, "username", "That username is already in use.");

    private static final ValueError EMAIL_IN_USE =
            new ValueError(ValueError.ErrorCause.ALREADY_EXISTS, "email", "That email is already in use.");

    private static final ValueError MISSING_GAME_ID =
            new ValueError(ValueError.ErrorCause.MISSING_VALUE, "gameId", "The game id is missing.");


}
