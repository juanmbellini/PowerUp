package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.exceptions.NoSuchEntityException;
import ar.edu.itba.paw.webapp.exceptions.UnauthorizedException;
import ar.edu.itba.paw.webapp.interfaces.*;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.ReviewLike;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model.model_interfaces.Like;
import ar.edu.itba.paw.webapp.model.model_interfaces.Likeable;
import ar.edu.itba.paw.webapp.model.validation.ValidationException;
import ar.edu.itba.paw.webapp.model.validation.ValidationExceptionThrower;
import ar.edu.itba.paw.webapp.model.validation.ValueError;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableWrapper;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of the review service.
 */
@Service
@Transactional
public class ReviewServiceImpl implements ReviewService, ValidationExceptionThrower {

    private final ReviewDao reviewDao;

    private final GameDao gameDao;

    private final ReviewLikeDao reviewLikeDao;


    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, GameDao gameDao, ReviewLikeDao reviewLikeDao) {
        this.reviewDao = reviewDao;
        this.gameDao = gameDao;
        this.reviewLikeDao = reviewLikeDao;
    }


    @Override
    public Page<LikeableWrapper<Review>> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter,
                                                    String usernameFilter,
                                                    int pageNumber, int pageSize,
                                                    ReviewDao.SortingType sortingType, SortDirection sortDirection,
                                                    User currentUser) {
        final Page<Review> page = reviewDao.getReviews(gameIdFilter, gameNameFilter, userIdFilter, usernameFilter,
                pageNumber, pageSize, sortingType, sortDirection);
        final Map<Review, Long> likeCounts = reviewLikeDao.countLikes(page.getData());
        final Map<Review, Boolean> userLikes = Optional.ofNullable(currentUser)
                .map(user -> reviewLikeDao.likedBy(page.getData(), user))
                .orElse(new HashMap<>());
        return createLikeableNewPage(page, likeCounts, userLikes);
    }

    @Override
    public LikeableWrapper<Review> findById(long reviewId, User currentUser) {
        return Optional.ofNullable(reviewDao.findById(reviewId))
                .map(review -> new LikeableWrapper<>(review,
                        // If currentUser is present check if it liked the review. If not present, get null.
                        Optional.ofNullable(currentUser)
                                .map(user -> reviewLikeDao.exists(review, user))
                                .orElse(null)))
                .orElse(null);
    }

    @Override
    public Review create(Long gameId, String reviewBody, User reviewer)
            throws NoSuchEntityException, ValidationException {
        if (reviewer == null) {
            throw new IllegalArgumentException();
        }
        // Holds a Game if gameId is not null and a Game exists with that id, else will hold null.
        Optional<Game> gameOptional = getGameOptional(gameId);
        // Will check existence if game exists.
        gameOptional.ifPresent(game -> validateReviewExistence(game, reviewer));
        // If optional holds null, ask for null. Else ask for the game it holds.
        return reviewDao.create(reviewer, gameOptional.orElse(null), reviewBody);
    }

    @Override
    public void likeReview(long reviewId, User liker) {
        if (liker == null) {
            throw new IllegalArgumentException();
        }
        final Review review = getReview(reviewId);
        // If already liked, do nothing and be idempotent
        if (!reviewLikeDao.exists(review, liker)) {
            reviewLikeDao.create(review, liker);
        }
    }

    @Override
    public void unlikeReview(long reviewId, User unliker) {
        if (unliker == null) {
            throw new IllegalArgumentException();
        }
        final Review review = getReview(reviewId);
        // If not liked, do nothing and be idempotent
        Optional.ofNullable(reviewLikeDao.find(review, unliker)).ifPresent(reviewLikeDao::delete);
    }

    @Override
    public Page<User> getUsersLikingTheReview(long reviewId, int pageNumber, int pageSize,
                                              ReviewLikeDao.SortingType sortingType, SortDirection sortDirection) {
        final Review thread = getReview(reviewId);
        final Page<ReviewLike> page = reviewLikeDao.getLikes(thread, pageNumber, pageSize, sortingType, sortDirection);
        return createLikersPage(page);
    }

    @Override
    public void changeReviewBody(long reviewId, String newBody, User updater) {
        if (updater == null) {
            throw new IllegalArgumentException();
        }
        final Review review = getReview(reviewId);
        validateUpdatePermission(review, updater);
        reviewDao.changeReviewBOdy(review, newBody);
    }

    @Override
    public void delete(long reviewId, User deleter) {
        if (deleter == null) {
            throw new IllegalArgumentException();
        }
        getReviewOptional(reviewId).ifPresent(review -> {
            validateDeletePermission(review, deleter);
            reviewDao.delete(review);
        });
    }

    private Page<User> createLikersPage(Page<? extends Like> oldPage) {
        return ServiceHelper.fromAnotherPage(oldPage, Like::getUser).build();
    }

    /**
     * Creates a new {@link Page} of {@link LikeableWrapper} according to the given {@code oldPage}
     * and the {@link Map} of {@code likeCounts}.
     *
     * @param oldPage    The old {@link Page} from which data is taken.
     * @param likeCounts The {@link Map} containing the amount of likes.
     * @param <T>        The type of elements in the {@link Page}.
     * @return The new {@link Page}.
     */
    private <T extends Likeable> Page<LikeableWrapper<T>> createLikeableNewPage(Page<T> oldPage,
                                                                                Map<T, Long> likeCounts,
                                                                                Map<T, Boolean> likes) {
        return ServiceHelper.fromAnotherPage(oldPage, entity ->
                new LikeableWrapper<>(entity, likeCounts.get(entity), likes.get(entity)))
                .build();
    }

    /**
     * Returns an optional holding a possible {@link Game} (if it exists and if {@code gameId} is not null).
     *
     * @param gameId The {@link Game} id.
     * @return The Optional of {@link Game} possibly holding a {@link Game} with the given {@code gameId}.
     */
    private Optional<Game> getGameOptional(Long gameId) {
        return Optional.ofNullable(gameId).map(gameDao::findById);
    }

    /**
     * Checks that the given {@code updater} has permission to update the given {@link Review}.
     *
     * @param review  The {@link Review} to be updated.
     * @param updater The {@link User} performing the operation.
     * @throws UnauthorizedException If the {@code updater} does not have permission to update the given {@code review}.
     */
    private void validateUpdatePermission(final Review review, final User updater) throws UnauthorizedException {
        validatePermission(review, updater, "update",
                (reviewLambda, updaterUser) -> Long.compare(reviewLambda.getUser().getId(), updaterUser.getId()) == 0);
    }

    /**
     * Checks that the given {@code deleter} has permission to delete the given {@link Review}.
     *
     * @param review  The {@link Review} to be deleted.
     * @param deleter The {@link User} performing the operation.
     * @throws UnauthorizedException If the {@code deleter} does not have permission to delete the given {@code review}.
     */
    private void validateDeletePermission(final Review review, final User deleter) throws UnauthorizedException {
        validatePermission(review, deleter, "delete",
                (reviewLambda, deleterUser) -> Long.compare(reviewLambda.getUser().getId(), deleterUser.getId()) == 0);
    }

    /**
     * Validates that the given {@code operator} can perform the given {@code operationName}
     * over the given {@link Review}, according to the given {@link BiPredicate}
     * (which must implement the {@link BiPredicate#test(Object, Object)} in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Review}, or {@code false} otherwise).
     *
     * @param review        The {@link Review} to be operated over.
     * @param operator      The {@link User} who is operating over the {@link Review}.
     * @param operationName A {@link String} indicating the operation being done (for informative purposes).
     * @param testFunction  A {@link BiPredicate} that implements logic to check if the operation is allowed.
     * @throws UnauthorizedException If the {@code operator} {@link User} does not have permission to operate over
     *                               the given {@link Review}.
     * @implNote The {@link BiPredicate#test(Object, Object)} must be implemented in a way that it returns
     * {@code true} if the operator has permission to operate over the {@link Review}, or {@code false} otherwise.
     */
    private void validatePermission(final Review review, final User operator,
                                    String operationName, BiPredicate<Review, User> testFunction)
            throws UnauthorizedException {
        if (review == null || operator == null) {
            throw new IllegalArgumentException("Review and Operator must not be null");
        }
        if (testFunction.negate().test(review, operator)) {
            throw new UnauthorizedException("User #" + operator.getId() + " does not have permission" +
                    " to " + operationName + " review #" + review.getId());
        }
    }


    /**
     * Checks whether a {@link Review} was written to the given {@link Game}, by the given {@link User}.
     *
     * @param game The {@link Game} to check if it was reviewed by the given {@code user}.
     * @param user The {@link User} to check if it wrote a review for the given {@code game}.
     */
    private void validateReviewExistence(Game game, User user) {
        if (game == null || user == null) {
            throw new IllegalArgumentException("Game and user must not be null");
        }
        // TODO: create method for checking existence
        if (!reviewDao.getReviews(game.getId(), null, user.getId(), null, 1, 1, ReviewDao.SortingType.GAME_ID,
                SortDirection.ASC).getData().isEmpty()) {
            throwValidationException(Stream.of(GAME_ALREADY_REVIEWED_BY_USER).collect(Collectors.toList()));
        }
    }


    /**
     * Retrieves the {@link Review} with the given {@code reviewId}.
     *
     * @param reviewId The {@link Review} id.
     * @return The {@link Review} with the given {@code reviewId}.
     * @throws NoSuchEntityException If no {@link Review} exists with the given {@code reviewId}.
     */
    private Review getReview(long reviewId) throws NoSuchEntityException {
        return getReviewOptional(reviewId).orElseThrow(NoSuchEntityException::new);
    }

    /**
     * Retrieves a nullable {@link Optional} of {@link Review} with the given {@code reviewId}.
     *
     * @param reviewId The {@link Review} id.
     * @return The nullable {@link Optional}.
     */
    private Optional<Review> getReviewOptional(long reviewId) {
        return Optional.ofNullable(reviewDao.findById(reviewId));
    }


    private static final ValueError GAME_ALREADY_REVIEWED_BY_USER =
            new ValueError(ValueError.ErrorCause.ALREADY_EXISTS,
                    "(reviewerId, gameId)", "That user already reviewed that game");
}
