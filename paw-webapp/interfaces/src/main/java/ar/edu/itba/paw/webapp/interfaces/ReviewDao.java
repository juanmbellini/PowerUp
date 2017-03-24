package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.exceptions.NoSuchGameException;
import ar.edu.itba.paw.webapp.exceptions.NoSuchUserException;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.utilities.Page;

/**
 * Data Access Object for game reviews.
 */
public interface ReviewDao {

    /**
     * @see ReviewService#getReviews(Long, String, Long, String, int, int, SortingType, SortDirection).
     */
    Page<Review> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String userNameFilter,
                            int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection);

    /**
     * @see ReviewService#create(long, long, String, int, int, int, int, int)
     */
    Review create(User reviewer, Game game,
                  String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                  Integer controlsScore, Integer funScore);

    /**
     * @see ReviewService#update(long, long, String, Integer, Integer, Integer, Integer, Integer) .
     */
    void update(Review review, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                Integer controlsScore, Integer funScore);

    /**
     * @see ReviewService#delete(long, long)
     */
    void delete(Review review);

    /**
     * @see ReviewService#findById(long)
     */
    Review findById(long reviewId);

    /**
     * @see ReviewService#find(long, long)
     */
    @Deprecated
    Review find(long userId, long gameId) throws NoSuchUserException, NoSuchGameException;


    /**
     * Enum indicating the sorting type for the "get reviews" method.
     */
    enum SortingType {
        ID {
            @Override
            public String getFieldName() {
                return "id";
            }
        },
        GAME_ID {
            @Override
            public String getFieldName() {
                return "game.id";
            }
        },
        DATE {
            @Override
            public String getFieldName() {
                return "date";
            }
        };

        /**
         * Returns the "sorting by" field name.
         *
         * @return The name.
         */
        abstract public String getFieldName();
    }
}
