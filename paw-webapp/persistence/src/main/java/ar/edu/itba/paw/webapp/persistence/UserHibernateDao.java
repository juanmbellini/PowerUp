package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.GenreDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

/**
 * Created by julrodriguez on 28/10/16.
 */
@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    private final GameDao gameDao;


    @Autowired
    public UserHibernateDao(GameDao gameDao, GenreDao genreDao) {
        this.gameDao = gameDao;
    }

    @Override
    public Page<User> getUsers(String usernameFilter, String emailFilter, Authority authorityFilter,
                               int pageNumber, int pageSize, SortingType sortingType, SortDirection sortDirection) {


        final StringBuilder query = new StringBuilder()
                .append("FROM User user INNER JOIN user.authorities authority");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();
        int conditionNumber = 0;
        if (usernameFilter != null && !usernameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(user.username) LIKE ?" + conditionNumber,
                    "%" + usernameFilter.toLowerCase() + "%", conditionNumber));
            conditionNumber++;
        }
        if (emailFilter != null && !emailFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(user.email) LIKE ?" + conditionNumber,
                    "%" + emailFilter.toLowerCase() + "%", conditionNumber));
            conditionNumber++;
        }
        if (authorityFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("authority = ?" + conditionNumber,
                    authorityFilter, conditionNumber));
        }
        return DaoHelper.findPageWithConditions(em, User.class, query, "user", "user.id", conditions,
                pageNumber, pageSize, "user." + sortingType.getFieldName(), sortDirection, true);
    }


    @Override
    public User findById(long id) {
        return DaoHelper.findSingle(em, User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        return DaoHelper.findSingleWithConditions(em, User.class, "FROM User WHERE username = ?1", username);
    }

    @Override
    public User findByEmail(String email) {
        return DaoHelper.findSingleWithConditions(em, User.class, "FROM User WHERE email = ?1", email);
    }


    @Override
    public User create(String username, String email, String password) {
        final User user = new User(username, email, password).addAuthority(Authority.USER);
        em.persist(user);
        return user;
    }

    @Override
    public void changePassword(User user, String newPassword) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.changePassword(newPassword);
        em.merge(user);
    }


    @Override
    public void changeProfilePicture(User user, byte[] picture, String mimeType) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.changeProfilePicture(picture, mimeType);
        em.merge(user);
    }


    @Override
    public Page<UserGameStatus> getPlayStatuses(User user, Long gameIdFilter, String gameNameFilter,
                                                int pageNumber, int pageSize,
                                                PlayStatusAndGameScoresSortingType sortingType,
                                                SortDirection sortDirection) {

        return getPageOfRelationObject(user, gameIdFilter, gameNameFilter, pageNumber, pageSize,
                sortingType.getFieldName(), sortDirection, UserGameStatus.class, em);
    }


    @Override
    public void setPlayStatus(User user, Game game, PlayStatus playStatus) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.setPlayStatus(game, playStatus);
        em.merge(user);
    }

    @Override
    public void removePlayStatus(User user, Game game) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        UserGameStatus gameStatus = user.removePlayStatus(game);
        if (gameStatus == null) {
            return;
        }
        em.remove(em.merge(gameStatus));
        em.merge(user);
    }


    @Override
    public Page<UserGameScore> getGameScores(User user, Long gameIdFilter, String gameNameFilter,
                                             int pageNumber, int pageSize,
                                             PlayStatusAndGameScoresSortingType sortingType,
                                             SortDirection sortDirection) {

        return getPageOfRelationObject(user, gameIdFilter, gameNameFilter, pageNumber, pageSize,
                sortingType.getFieldName(), sortDirection, UserGameScore.class, em);
    }

    @Override
    public void setGameScore(User user, Game game, Integer score) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        UserGameScore gameScore = user.scoreGame(game, score);
        if (gameScore != null) {
            em.remove(em.merge(gameScore)); // Removes relationship between user and game.
        }
        em.merge(user);
    }

    @Override
    public void removeGameScore(User user, Game game) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        UserGameScore gameScore = user.unscoreGame(game);
        if (gameScore == null) {
            return;
        }
        em.remove(em.merge(gameScore));
        em.merge(user);
    }


    @Override
    public void addAuthority(User user, Authority authority) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.addAuthority(authority);
        em.merge(user);
    }

    @Override
    public void removeAuthority(User user, Authority authority) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        user.removeAuthority(authority);
        em.merge(user);
    }


    @Override
    public void delete(User user) {
        throw new NotImplementedException();

//            TODO implement, decide:
//            1) Is the user actually deleted or disabled? Does the user become something like <Deleted User>?
//            2) Are their shelves, etc. deleted? (Probably)
//            2) What happens to their reviews? If they are deleted, to they affect the game's average score?
//            3) What happens to their scored games? If their score is reverted, is the average game's score re-computed?
//            4) What happens to their threads? And their thread comments?
//            5) What happens to their likes?
//            etc.
//
    }


    @Override
    public Page<Game> recommendedGames(User user, int pageNumber, int pageSize, SortDirection sortDirection) {
        return null;
    }

    @Override
    public Page<Game> recommendedGames(User user, Set<Shelf> shelves,
                                       int pageNumber, int pageSize, SortDirection sortDirection) {
        return null;
    }

    @Override
    public Collection<Game> recommendGames(long userId) {

        User user = findById(userId);
        if (user == null) return new HashSet<>();


        return recommendGames(user.getPlayedGames().keySet(), userId);


    }


    @Override
    public Collection<Game> recommendGames(long userId, Set<Shelf> shelves) {
        if (shelves == null || shelves.isEmpty()) return recommendGames(userId);

        Set<Long> gameIds = new HashSet<>();

        for (Shelf shelf : shelves) {
            for (Game game : shelf.getGames()) {
                gameIds.add(game.getId());
            }
        }

        return recommendGames(gameIds, userId);
    }


//    private Page<Game> recommendedGames(User user, Set<Game> relatedGames,
//                                        int pageNumber, int pageSize, SortDirection sortDirection) {
//        Map<Long, Integer> scoredGames = user.getScoredGames();
//
//        return null;
//    }


    public Collection<Game> recommendGames(Set<Long> relatedGamesId, long userId) {

        User user = findById(userId);
        Map<Long, Integer> scoredGames = user.getScoredGames();

        Map<FilterCategory, Map<String, Double>> filtersScoresMap = new HashMap();

        if (relatedGamesId == null || relatedGamesId.size() == 0) return new LinkedHashSet<>();

        //Genres
        Map<Genre, Integer> countGenre = new HashMap<>();
        Map<Genre, Long> sumGenre = new HashMap<>();
        for (long gameId : relatedGamesId) {
            Game scoredGame = gameDao.findById(gameId);
            for (Genre genre : scoredGame.getGenres()) {
                if (!countGenre.containsKey(genre)) {
                    countGenre.put(genre, 0);
                }
                if (!sumGenre.containsKey(genre)) {
                    sumGenre.put(genre, 0l);
                }
                countGenre.put(genre, countGenre.get(genre) + 1);
                if (scoredGames.keySet().contains(gameId)) {
                    sumGenre.put(genre, sumGenre.get(genre) + scoredGames.get(gameId));
                } else {
                    sumGenre.put(genre, sumGenre.get(genre) + 5);
                }
            }
        }
        Map<String, Double> mapFilterToFilterScoreGenre = new HashMap<>();
        for (Genre genre : countGenre.keySet()) {
            mapFilterToFilterScoreGenre.put(genre.getName(), ((double) sumGenre.get(genre)) / countGenre.get(genre));
        }
        filtersScoresMap.put(FilterCategory.genre, mapFilterToFilterScoreGenre);

        //Keywords
        Map<Keyword, Integer> countKeyword = new HashMap<>();
        Map<Keyword, Long> sumKeyword = new HashMap<>();
        for (long gameId : relatedGamesId) {
            Game scoredGame = gameDao.findById(gameId);
            for (Keyword keyword : scoredGame.getKeywords()) {
                if (!countKeyword.containsKey(keyword)) {
                    countKeyword.put(keyword, 0);
                }
                if (!sumKeyword.containsKey(keyword)) {
                    sumKeyword.put(keyword, 0l);
                }
                countKeyword.put(keyword, countKeyword.get(keyword) + 1);
                if (scoredGames.keySet().contains(gameId)) {
                    sumKeyword.put(keyword, sumKeyword.get(keyword) + scoredGames.get(gameId));
                } else {
                    sumKeyword.put(keyword, sumKeyword.get(keyword) + 5);
                }
            }
        }
        Map<String, Double> mapFilterToFilterScoreKeyword = new HashMap<>();
        for (Keyword keyword : countKeyword.keySet()) {
            mapFilterToFilterScoreKeyword.put(keyword.getName(), ((double) sumKeyword.get(keyword)) / countKeyword.get(keyword));
        }
        filtersScoresMap.put(FilterCategory.keyword, mapFilterToFilterScoreKeyword);

        //Get all games with each of X filter with higher weight and give each a weight based on avg_score we gave.

        Collection<Game> finalResultList = gameDao.getRecommendedGames(relatedGamesId, filtersScoresMap);
        return finalResultList;
    }


    // ======== Helpers ========

    /**
     * Returns the relationship object of type {@code T}, applying filtering, sorting and pagination.
     *
     * @param user           The user.
     * @param gameIdFilter   Filter for game id.
     * @param gameNameFilter Filter for game name.
     * @param pageNumber     The page number.
     * @param pageSize       The page size.
     * @param sortingField   The sorting field.
     * @param sortDirection  The sort direction (i.e ASC or DESC)
     * @param klass          The class of the return type.
     * @param em             The entity manager to perform operations.
     * @param <T>            The type of the object in the page.
     * @return The resulting page.
     */
    private static <T> Page<T> getPageOfRelationObject(User user, Long gameIdFilter, String gameNameFilter,
                                                       int pageNumber, int pageSize,
                                                       String sortingField, SortDirection sortDirection,
                                                       Class<T> klass, EntityManager em) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        final StringBuilder query = new StringBuilder().append("FROM ").append(klass.getSimpleName())
                .append(" relationObject");

        // Conditions
        final List<DaoHelper.ConditionAndParameterWrapper> conditions = new LinkedList<>();

        conditions.add(new DaoHelper.ConditionAndParameterWrapper("user.id = ?" + 0, user.getId(), 0));
        int conditionsNumber = 1;
        if (gameIdFilter != null) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("game.id = ?" + conditionsNumber, gameIdFilter, conditionsNumber));
            conditionsNumber++;
        }
        if (gameNameFilter != null && !gameNameFilter.isEmpty()) {
            conditions.add(new DaoHelper.ConditionAndParameterWrapper("LOWER(game.name) LIKE ?" + conditionsNumber,
                    "%" + gameNameFilter.toLowerCase() + "%", conditionsNumber));
        }
        return DaoHelper.findPageWithConditions(em, klass, query, "relationObject", "id", conditions,
                pageNumber, pageSize, sortingField, sortDirection, false);
    }

}
