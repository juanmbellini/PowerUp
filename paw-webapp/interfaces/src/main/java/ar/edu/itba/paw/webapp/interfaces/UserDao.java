package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;

import java.util.Collection;
import java.util.Set;

/**
 * Data Access Object for users.
 */
public interface UserDao extends FindByIdDao<User> {


    /**
     * @see UserService#getUsers(String, String, Authority, int, int, SortingType, SortDirection) .
     */
    Page<User> getUsers(String usernameFilter, String emailFilter, Authority authorityFilter,
                        int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection);

    /**
     * @see UserService#findByUsername(String)
     */
    User findByUsername(String username);

    /**
     * @see UserService#findByEmail(String)
     */
    User findByEmail(String email);


    /**
     * @see UserService#create(String, String, String)
     */
    User create(String username, String email, String password);

    /**
     * @see UserService#changePassword(long, String, long)
     */
    void changePassword(User user, String newPassword);

    /**
     * @see UserService#changeProfilePicture(long, byte[], String, long)
     */
    void changeProfilePicture(User user, byte[] picture, String mimeType);

    /**
     * @see UserService#getPlayStatuses(long, Long, String, int, int, PlayStatusAndGameScoresSortingType, SortDirection)
     */
    Page<UserGameStatus> getPlayStatuses(User user, Long gameIdFilter, String gameNameFilter,
                                         int pageNumber, int pageSize, PlayStatusAndGameScoresSortingType sortingType,
                                         SortDirection sortDirection);


    /**
     * @see UserService#setPlayStatus(long, Long, PlayStatus, long)
     */
    void setPlayStatus(User user, Game game, PlayStatus playStatus);

    /**
     * @see UserService#removePlayStatus(long, Long, long)
     */
    void removePlayStatus(User user, Game game);


    /**
     * @see UserService#getGameScores(long, Long, String, int, int, PlayStatusAndGameScoresSortingType, SortDirection)
     */
    Page<UserGameScore> getGameScores(User user, Long gameIdFilter, String gameNameFilter,
                                      int pageNumber, int pageSize, PlayStatusAndGameScoresSortingType sortingType,
                                      SortDirection sortDirection);


    /**
     * @see UserService#setGameScore(long, long, Integer, long)
     */
    void setGameScore(User user, Game game, Integer score);

    /**
     * @see UserService#removeGameScore(long, long, long)
     */
    void removeGameScore(User user, Game game);


    /**
     * @see UserService#addAuthority(long, Authority, long)
     */
    void addAuthority(User user, Authority authority);

    /**
     * @see UserService#removeAuthority(long, Authority, long)
     */
    void removeAuthority(User user, Authority authority);


    /**
     * @see UserService#delete(long, long)
     */
    void delete(User user);


    /**
     * @see UserService#recommendedGames(long, int, int, SortDirection)
     */
    Page<Game> recommendedGames(User user, int pageNumber, int pageSize, SortDirection sortDirection);

    /**
     * @see UserService#recommendedGames(long, Set, int, int, SortDirection)
     */
    Page<Game> recommendedGames(User user, Set<Shelf> shelves,
                                int pageNumber, int pageSize, SortDirection sortDirection);

    /**
     * @see UserService#getGameList(long, int, int, PlayStatusAndGameScoresSortingType, SortDirection)
     */
    Page<UserGameStatus> getGameList(User user, int pageNumber, int pageSize, PlayStatusAndGameScoresSortingType sortingType, SortDirection sortDirection);


    /**
     * @see UserService#recommendGames(long)
     */
    @Deprecated
    Collection<Game> recommendGames(long userId);

    /**
     * @see UserService#recommendGames(long, Set)
     */
    @Deprecated
    Collection<Game> recommendGames(long userId, Set<Shelf> shelves);

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
        USERNAME {
            @Override
            public String getFieldName() {
                return "username";
            }
        },
        EMAIL {
            @Override
            public String getFieldName() {
                return "email";
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

    /**
     * Enum indicating the sorting type for the get play statuses / get scored games methods.
     */
    enum PlayStatusAndGameScoresSortingType {
        GAME_ID {
            @Override
            public String getFieldName() {
                return "game.id";
            }
        },
        GAME_NAME {
            @Override
            public String getFieldName() {
                return "game.name";
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
        public static PlayStatusAndGameScoresSortingType fromString(String name) {
            return valueOf(name.replace("-", "_").toUpperCase());
        }
    }
}
