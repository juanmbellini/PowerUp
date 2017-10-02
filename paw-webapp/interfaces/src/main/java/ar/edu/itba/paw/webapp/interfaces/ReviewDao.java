package ar.edu.itba.paw.webapp.interfaces;

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
    Page<Review> getReviews(Long gameIdFilter, String gameNameFilter, Long userIdFilter, String usernameFilter,
                            int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection);

    /**
     * @see ReviewService#create(Long, String, Integer, Integer, Integer, Integer, Integer, User)
     */
    Review create(User reviewer, Game game,
                  String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                  Integer controlsScore, Integer funScore);

    /**
     * @see ReviewService#update(long, String, Integer, Integer, Integer, Integer, Integer, User) .
     */
    void update(Review review, String reviewBody, Integer storyScore, Integer graphicsScore, Integer audioScore,
                Integer controlsScore, Integer funScore);

    /**
     * @see ReviewService#delete(long, User)
     */
    void delete(Review review);

    /**
     * @see ReviewService#findById(long, User)
     */
    Review findById(long reviewId);

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
        BEST {
            @Override
            public String getFieldName() {
                return "SIZE(review.likes)";
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

        @Override
        public String toString() {
            return super.toString().toLowerCase().replace("_", "-");
        }

        /**
         * Creates an enum from the given {@code name} (can be upper, lower or any case)
         *
         * @param name The value of the enum as a string.
         * @return The enum value.
         */
        public static SortingType fromString(String name) {
            return valueOf(name.replace("-", "_").toUpperCase());
        }
    }
}
