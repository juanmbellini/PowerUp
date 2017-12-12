package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValidationHelper;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import ar.edu.itba.paw.webapp.model_wrappers.CommentableAndLikeableWrapper;
import ar.edu.itba.paw.webapp.model_wrappers.GameWithUserShelvesWrapper;
import ar.edu.itba.paw.webapp.model_wrappers.UserWithFollowCountsWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of user service layer.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService, ValidationExceptionThrower, NoSuchEntityThrower,
        ExistenceByIdChecker<User> {


    private final UserDao userDao;

    private final GameDao gameDao;

    private final ShelfDao shelfDao;

    private final UserFollowDao userFollowDao;

    private final UserFeedDao feedDao;

    private final CommentDao commentDao;

    private final ThreadLikeDao threadLikeDao;

    private final PasswordEncoder passwordEncoder;

    private final ResetPasswordTokenDao resetPasswordTokenDao;


    @Autowired
    public UserServiceImpl(UserDao userDao, GameDao gameDao, ShelfDao shelfDao, UserFollowDao userFollowDao,
                           UserFeedDao feedDao, CommentDao commentDao, ThreadLikeDao threadLikeDao,
                           PasswordEncoder passwordEncoder, ResetPasswordTokenDao resetPasswordTokenDao) {
        this.userDao = userDao;
        this.gameDao = gameDao;
        this.shelfDao = shelfDao;
        this.userFollowDao = userFollowDao;
        this.feedDao = feedDao;
        this.commentDao = commentDao;
        this.threadLikeDao = threadLikeDao;
        this.passwordEncoder = passwordEncoder;
        this.resetPasswordTokenDao = resetPasswordTokenDao;
    }


    @Override
    public Page<UserWithFollowCountsWrapper> getUsers(String usernameFilter, String emailFilter,
                                                      Authority authorityFilter,
                                                      int pageNumber, int pageSize,
                                                      UserDao.SortingType sortingType, SortDirection sortDirection,
                                                      User currentUser) {
        Page<User> page = userDao.getUsers(usernameFilter, emailFilter, authorityFilter,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<User, Long> followingCounts = userFollowDao.countFollowing(page.getData());
        final Map<User, Long> followersCounts = userFollowDao.countFollowers(page.getData());
        final Map<User, Boolean> following = Optional.ofNullable(currentUser)
                .map(user -> userFollowDao.following(page.getData(), user)).orElse(new HashMap<>());
        final Map<User, Boolean> followedBy = Optional.ofNullable(currentUser)
                .map(user -> userFollowDao.followedBy(page.getData(), user)).orElse(new HashMap<>());
        return ServiceHelper.fromAnotherPage(page, user ->
                new UserWithFollowCountsWrapper(user, followingCounts.get(user), followersCounts.get(user),
                        currentUser != null && user.getId() == currentUser.getId() ? null : following.get(user),
                        currentUser != null && user.getId() == currentUser.getId() ? null : followedBy.get(user)))
                .build();
    }

    @Override
    public UserWithFollowCountsWrapper findById(long id, User currentUser) {
        return getWithSocialStuff(userDao.findById(id), currentUser);
    }

    @Override
    public UserWithFollowCountsWrapper findById(long id) {
        return getWithoutSocialStuff(userDao.findById(id));
    }

    @Override
    public UserWithFollowCountsWrapper findByUsername(String username, User currentUser) {
        return getWithSocialStuff(userDao.findByUsername(username), currentUser);
    }

    @Override
    public UserWithFollowCountsWrapper findByUsername(String username) {
        return getWithoutSocialStuff(userDao.findByUsername(username));
    }

    @Override
    public UserWithFollowCountsWrapper findByEmail(String email, User currentUser) {
        return getWithSocialStuff(userDao.findByEmail(email), currentUser);
    }

    @Override
    public UserWithFollowCountsWrapper findByEmail(String email) {
        return getWithoutSocialStuff(userDao.findByEmail(email));
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

        validatePassword(password);
        final String hashedPassword = passwordEncoder.encode(password);
        return userDao.create(username, email, hashedPassword);
    }


    @Override
    public void changePassword(long userId, String newPassword, long updaterId) {
        validatePassword(newPassword);
        final String newHashedPassword = passwordEncoder.encode(newPassword);
        userDao.changePassword(checkUserValuesAndAuthoring(userId, updaterId), newHashedPassword);
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
    public Collection<Game> recommendGames(long userId) {
        return userDao.recommendGames(userId);
    }

    @Override
    public Collection<Game> recommendGames(long userId, List<String> shelfNameFilters) {
        final User user = Optional.ofNullable(userDao.findById(userId)).orElseThrow(NoSuchEntityException::new);
        final Set<Shelf> shelves = Optional.ofNullable(shelfNameFilters)
                .map(list -> list.stream()
                        .map(name -> shelfDao.findByName(user, name)) // Map each name to a shelf
                        .filter(Objects::nonNull) // Remove those that are null
                        .collect(Collectors.toSet())) // Store shelves into set
                .orElse(new HashSet<>()); // If list of names is null, return an empty hash set.
        return userDao.recommendGames(userId, shelves);
    }

    @Override
    public Page<GameWithUserShelvesWrapper> getGameList(long userId, List<String> shelfNames, List<PlayStatus> statuses,
                                                        int pageNumber, int pageSize,
                                                        UserDao.ListGameSortingType sortingType, SortDirection sortDirection) {
        if (shelfNames == null || statuses == null) {
            throw new IllegalArgumentException();
        }

        final User user = getUser(userId); // Will throw NoSuchEntityException if not exists
        final List<Shelf> shelves = shelfNames.stream().map(name -> getShelf(user, name)).collect(Collectors.toList());

        final Page<Game> page = userDao.getGameList(user, shelves, statuses,
                pageNumber, pageSize, sortingType, sortDirection);

        // Gets all shelves of the given user
        final int amountOfShelves = (int) shelfDao.getShelves(null, null, null, user.getId(), null, 1, 1,
                ShelfDao.SortingType.ID, SortDirection.ASC).getOverAllAmountOfElements();
        final Collection<Shelf> userShelves = shelfDao.getShelves(null, null, null, user.getId(), null, 1, amountOfShelves,
                ShelfDao.SortingType.ID, SortDirection.ASC).getData();

        return ServiceHelper.fromAnotherPage(page, game -> {
            final List<Shelf> shelvesHolding = userShelves.stream().filter(shelf -> shelf.getGames().contains(game))
                    .collect(Collectors.toList());

            final Iterator<Integer> scoresIt = userDao.getGameScores(user, game.getId(), null, 1, 1,
                    UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC)
                    .getData().stream().map(UserGameScore::getScore).iterator();
            final Iterator<PlayStatus> statusesIt = userDao.getPlayStatuses(user, game.getId(), null, 1, 1,
                    UserDao.PlayStatusAndGameScoresSortingType.GAME_ID, SortDirection.ASC)
                    .getData().stream().map(UserGameStatus::getPlayStatus).iterator();

            return new GameWithUserShelvesWrapper(user, game,
                    shelvesHolding,
                    scoresIt.hasNext() ? scoresIt.next() : null,
                    statusesIt.hasNext() ? statusesIt.next() : null);
        }).build();
    }

    @Override
    public Page<User> getFollowing(long userId, int pageNumber, int pageSize, SortDirection sortDirection) {
        final User follower = getUser(userId); // Will throw NoSuchEntityException if not present
        final Page<UserFollow> page = userFollowDao.getFollowing(follower, pageNumber, pageSize, sortDirection);
        return ServiceHelper.fromAnotherPage(page, UserFollow::getFollowed).build();
    }

    @Override
    public Page<User> getFollowers(long userId, int pageNumber, int pageSize, SortDirection sortDirection) {
        final User followed = getUser(userId); // Will throw NoSuchEntityException if not present
        final Page<UserFollow> page = userFollowDao.getFollowers(followed, pageNumber, pageSize, sortDirection);
        return ServiceHelper.fromAnotherPage(page, UserFollow::getFollower).build();
    }


    @Override
    public void followUser(long followedId, User follower) {
        if (follower == null) {
            throw new IllegalArgumentException();
        }
        final User followed = getUser(followedId);
        // If already followed, do nothing and be idempotent
        if (!userFollowDao.exists(follower, followed)) {
            userFollowDao.create(followed, follower);
        }
    }

    @Override
    public void unFollowUser(long unFollowedId, User unFollower) {
        if (unFollower == null) {
            throw new IllegalArgumentException();
        }
        final User user = getUser(unFollowedId);
        // If not followed, do nothing and be idempotent
        Optional.ofNullable(userFollowDao.find(unFollower, user)).ifPresent(userFollowDao::delete);
    }

    @Override
    public Page<CommentableAndLikeableWrapper<Thread>> getThreadsForFeed(User user, int pageNumber, int pageSize) {

        final Page<Thread> page = feedDao.getThreads(user, pageNumber, pageSize);
        final Map<Thread, Long> commentCounts = commentDao.countComments(page.getData());
        final Map<Thread, Long> likeCounts = threadLikeDao.countLikes(page.getData());
        final Map<Thread, Boolean> userLikes = Optional.ofNullable(user)
                .map(userOpt -> threadLikeDao.likedBy(page.getData(), userOpt)).orElse(new HashMap<>());
        return ServiceHelper.fromAnotherPage(page, (thread) -> new CommentableAndLikeableWrapper.Builder<Thread>()
                .setEntity(thread)
                .setCommentCount(commentCounts.get(thread))
                .setLikeCount(likeCounts.get(thread))
                .setLikedByCurrentUser(userLikes.get(thread))
                .build()
        ).build();
    }

    @Override
    public Page<Review> getReviewsForFeed(User user, int pageNumber, int pageSize) {
        return feedDao.getReviews(user, pageNumber, pageSize);
    }

    @Override
    public Page<UserGameStatus> getPlayStatusesForFeed(User user, int pageNumber, int pageSize) {
        return feedDao.getPlayStatuses(user, pageNumber, pageSize);
    }

    @Override
    public void requireResetPassword(long userId, String urlTemplate) {
        //remove this
        String url = "";
        // TODO: create token
        // TODO: base64url encode the nonce of the token (replace 1234 with the nonce)
        final String base64UrlNonce = Base64Utils.encodeToUrlSafeString(Long.toString(1234).getBytes());
        // TODO: generate url to be sent in the email:
        final String resetPasswordUrl = String.format(urlTemplate, base64UrlNonce);
        // TODO: Send email

    }

    @Override
    public void resetPassword(String resetPasswordTokenNonce, String newPassword) {
        final String stringNonce = new String(Base64Utils.decodeFromUrlSafeString(resetPasswordTokenNonce),
                StandardCharsets.UTF_8);
        final long nonce = Long.valueOf(stringNonce); // TODO: check if this does not throws exception
        final ResetPasswordToken token = Optional.ofNullable(resetPasswordTokenDao.findByNonce(nonce))
                .orElseThrow(UnauthorizedException::new);
        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new RuntimeException("Expired"); // TODO: define custom exception? or just Unauthorized?
        }

        userDao.changePassword(token.getOwner(), newPassword);
    }

    // =====================


    /**
     * Validates the given {@code rawPassword}.
     * Note: This validation is made in service layer because the password will be hashed here.
     *
     * @param rawPassword The password to be validated.
     * @throws ValidationException If the password is not valid.
     */
    private void validatePassword(String rawPassword) throws ValidationException {
        final List<ValueError> errors = new LinkedList<>();
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(rawPassword, PASSWORD_MIN_LENGTH, PASSWORD_MAX_LENGTH,
                errors, MISSING_PASSWORD, PASSWORD_TOO_SHORT, PASSWORD_TOO_LONG);
        throwValidationException(errors);
    }


    /**
     * Retrieves the {@link User} with the given {@code userId}.
     *
     * @param userId The user id.
     * @return The {@link User} whose id matches the given {@code userId}.
     * @throws NoSuchEntityException If no {@link User} exists with the given {@code userId}.
     */
    private User getUser(final long userId) throws NoSuchEntityException {
        return Optional.ofNullable(userDao.findById(userId))
                .orElseThrow(NoSuchEntityException::new);
    }


    /**
     * Retrieves the {@link Shelf} with the given {@code shelfName}, owned by the given {@code user}.
     *
     * @param user      The {@link User} who owns the {@link Shelf}.
     * @param shelfName The {@link Shelf} name.
     * @return The {@link Shelf} with the given {@code shelfName}, owned by the given {@code user}.
     * @throws NoSuchEntityException If the {@link Shelf} does not exists.
     */
    private Shelf getShelf(final User user, final String shelfName) throws NoSuchEntityException {
        return getShelfOptional(user, shelfName).orElseThrow(NoSuchEntityException::new);
    }

    /**
     * Retrieves a nullable {@link Optional} of {@link Shelf} with the given {@code shelfName},
     * owned by the given {@code user}.
     *
     * @param user      The {@link User} who owns the {@link Shelf}.
     * @param shelfName The {@link Shelf} name.
     * @return The nullable {@link Optional}.
     */
    private Optional<Shelf> getShelfOptional(final User user, final String shelfName) {
        if (user == null || shelfName == null) {
            throw new IllegalArgumentException("User and shelf name must not be null");
        }
        return Optional.ofNullable(shelfDao.findByName(user, shelfName));
    }

    /**
     * Returns a {@link UserWithFollowCountsWrapper} without social stuff being wrapped.
     *
     * @param retrievedUser he wrapped {@link User}.
     * @return The created {@link UserWithFollowCountsWrapper}.
     */
    private UserWithFollowCountsWrapper getWithoutSocialStuff(User retrievedUser) {
        return getRetrieveOptional(retrievedUser, null, null).orElse(null);
    }

    /**
     * Returns a {@link UserWithFollowCountsWrapper} with social stuff being wrapped.
     *
     * @param retrievedUser The wrapped {@link User}.
     * @param currentUser   The current {@link User}.
     * @return The created {@link UserWithFollowCountsWrapper}.
     */
    private UserWithFollowCountsWrapper getWithSocialStuff(User retrievedUser, User currentUser) {
        if (retrievedUser == null) {
            return null;
        }
        return getRetrieveOptional(retrievedUser,
                Optional.ofNullable(currentUser)
                        .filter(current -> current.getId() != retrievedUser.getId())
                        .map(current -> userFollowDao.exists(current, retrievedUser))
                        .orElse(null),
                Optional.ofNullable(currentUser)
                        .filter(current -> current.getId() != retrievedUser.getId())
                        .map(current -> userFollowDao.exists(retrievedUser, current))
                        .orElse(null))
                .orElse(null);
    }

    /**
     * Returns an {@link Optional} of {@link UserWithFollowCountsWrapper} according to the given params.
     *
     * @param retrievedUser The {@link User} to wrap in the {@link UserWithFollowCountsWrapper}.
     * @param followed      The flag indicating if the wrapped {@link User} is being followed by
     *                      the current {@link User}.
     * @param following     The flag indicating if the wrapped {@link User} is following the current {@link User}.
     * @return The {@link Optional}.
     */
    private Optional<UserWithFollowCountsWrapper> getRetrieveOptional(User retrievedUser,
                                                                      Boolean followed, Boolean following) {
        return Optional.ofNullable(retrievedUser)
                .map(user -> new UserWithFollowCountsWrapper(user, followed, following));
    }

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


    // ==== Password validation stuff ====

    private final static int PASSWORD_MIN_LENGTH = 6;

    private final static int PASSWORD_MAX_LENGTH = 100;

    private static final ValueError MISSING_PASSWORD = new ValueError(ValueError.ErrorCause.MISSING_VALUE,
            "password", "The password is missing.");

    private static final ValueError PASSWORD_TOO_SHORT = new ValueError(ValueError.ErrorCause.ILLEGAL_VALUE,
            "password", "The password is too short.");

    private static final ValueError PASSWORD_TOO_LONG = new ValueError(ValueError.ErrorCause.ILLEGAL_VALUE,
            "password", "The password is too long.");
}
