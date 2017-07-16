package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.NumberOfPageBiggerThanTotalAmountException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.SortDirection;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import ar.edu.itba.paw.webapp.utilities.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by julrodriguez on 28/10/16.
 */
@Repository
public class UserHibernateDao implements UserDao {

    @PersistenceContext
    private EntityManager em;

    private final GameDao gameDao;

    @Autowired
    public UserHibernateDao(GameDao gameDao) {
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
        user.setPlayStatus(game, playStatus); // TODO: make it through queries
        em.merge(user);
    }

    @Override
    public void removePlayStatus(User user, Game game) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        UserGameStatus gameStatus = user.removePlayStatus(game); // TODO: make it through queries
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
        UserGameScore gameScore = user.scoreGame(game, score); // TODO: make it through queries
//        if (gameScore != null) {
//            em.remove(em.merge(gameScore)); // Removes relationship between user and game.
//        }
        em.merge(user);
    }

    @Override
    public void removeGameScore(User user, Game game) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        UserGameScore gameScore = user.unscoreGame(game); // TODO: make it through queries
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
    public Page<Game> getGameList(User user, List<Shelf> shelves, List<PlayStatus> statuses,
                                  int pageNumber, int pageSize,
                                  ListGameSortingType sortingType, SortDirection sortDirection) {

        if (shelves == null || statuses == null) {
            throw new IllegalArgumentException();
        }

        final List<String> statusesString = statuses.stream().map(PlayStatus::asInDatabase).map(String::toLowerCase)
                .collect(Collectors.toList());
        final List<Long> shelfIds = shelves.stream().map(Shelf::getId).collect(Collectors.toList());


        // Count and filtering
        final String subQuery = getListSubQuery(!shelfIds.isEmpty(), !statusesString.isEmpty());
        final long count = countList(subQuery, user, shelfIds, statusesString, em);
        if (count == 0) {
            return Page.emptyPage();
        }

        int totalAmountOfPages = Math.max((int) Math.ceil((double) count / pageSize), 1);
        // Avoid making the query if pageSize is wrong
        if (pageNumber > totalAmountOfPages) {
            throw new NumberOfPageBiggerThanTotalAmountException();
        }

        // Data extraction, paging and sorting
        final String query = getListQuery(sortingType == ListGameSortingType.SCORE, subQuery,
                sortingType.getFieldName(), sortDirection.getQLKeyword());
        final List<Game> games = getList(query, pageNumber, pageSize, user, shelfIds, statusesString, em);


        return DaoHelper.createPage(games, pageSize, pageNumber, totalAmountOfPages, count);
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

        final Set<Long> gameIds = shelves.stream()
                .map(Shelf::getGames) // Maps each shelf to a list of games
                .flatMap(Collection::stream) // Maps each list of games to a stream of games, and join each stream
                .map(Game::getId) // Map each game to its id
                .collect(Collectors.toSet()); // Collect all ids into a set

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


    // ==== User list  ====


    /**
     * Prepares the {@link String} holding the sub-query used to count and get matching games
     * to build the {@link User}'s list.
     *
     * @param includeShelvesFilter  A flag indicating if {@link Shelf} filtering must be applied
     * @param includeStatusesFilter A flag indicating if {@link PlayStatus} filtering must be applied
     * @return The {@link String} holding the sub-query used to count and get matching games
     * to build the {@link User}'s list
     */
    private static String getListSubQuery(boolean includeShelvesFilter, boolean includeStatusesFilter) {
        final StringBuilder selectAndFromStatements = new StringBuilder().append("SELECT DISTINCT g.id FROM games g");
        final StringBuilder statusesPart = new StringBuilder()
                .append(" INNER JOIN game_play_statuses gps ON g.id = gps.game_id AND gps.user_id = :userId");
        final StringBuilder shelvesPart = new StringBuilder()
                .append(" INNER JOIN shelf_games sh ON g.id = sh.game_id")
                .append(" INNER JOIN shelves s1 ON s1.id = sh.shelf_id AND s1.user_id = :userId");

        if (includeStatusesFilter) {
            statusesPart.append(" WHERE lower(status) IN :statuses");
        }
        if (includeShelvesFilter) {
            shelvesPart.append(" WHERE NOT EXISTS(")
                    .append("SELECT * FROM shelves s2 WHERE id IN :shelves AND NOT EXISTS(")
                    .append("SELECT * FROM shelf_games aux WHERE s2.id = aux.shelf_id AND aux.game_id = sh.game_id))");
        }

        //noinspection StringBufferReplaceableByString
        return new StringBuilder()
                .append(selectAndFromStatements).append(statusesPart)
                .append(" UNION ")
                .append(selectAndFromStatements).append(shelvesPart)
                .toString();
    }

    /**
     * Prepares the {@link String} holding the query used to build the {@link User}'s list.
     *
     * @param joinWithScore A flag indicating if a join must be done with the {@link UserGameScore} table.
     * @param subQuery      The {@link String} holding the sub-query used to count and filter.
     * @param orderBy       Indicates how the result must be filtered.
     * @param sortDirection Indicates if sorting must be done asc or desc.
     * @return The {@link String} holding the query used to build the {@link User}'s list.
     */
    private static String getListQuery(boolean joinWithScore, String subQuery, String orderBy, String sortDirection) {
        final StringBuilder query = new StringBuilder()
                .append("SELECT games.id")
                .append(", games.name")
                .append(", games.summary")
                .append(", games.avg_score")
                .append(", games.release")
                .append(", games.cover_picture_cloudinary_id")
                .append(", games.counter")
                .append(" FROM games");
        if (joinWithScore) {
            query.append(" LEFT JOIN game_scores ON games.id = game_scores.game_id AND game_scores.user_id = :userId");
        }
        query.append(" WHERE games.id IN (").append(subQuery).append(")")
                .append(" ORDER BY ").append(orderBy).append(" ").append(sortDirection);
        return query.toString();
    }

    /**
     * Counts the amount of {@link Game} does the given {@link User} holds,
     * according to filtering set in the given {@code subQuery}.
     *
     * @param subQuery       The {@link String} holding the SQL query.
     * @param user           The {@link User} owning the list.
     * @param shelfIds       A {@link List} of ids of {@link Shelf} to be used for filtering.
     * @param statusesString A {@link List} of {@link PlayStatus} to be used for filtering.
     * @param em             The {@link EntityManager} used to perform the query.
     * @return The amount of {@link Game}s the list holds.
     */
    private static long countList(String subQuery, User user, List<Long> shelfIds, List<String> statusesString,
                                  EntityManager em) {
        final Query countQuery = em.createNativeQuery("SELECT COUNT(*) FROM (" + subQuery + ") AS c")
                .setParameter("userId", user.getId());
        if (!shelfIds.isEmpty()) {
            countQuery.setParameter("shelves", shelfIds);
        }
        if (!statusesString.isEmpty()) {
            countQuery.setParameter("statuses", statusesString);
        }
        return ((BigInteger) countQuery.getSingleResult()).longValue();
    }

    /**
     * Returns the {@link List} of games that belongs to the {@link Page} with the given {@code pageNumber}
     * (whose size is the given {@code pageSize}) of the list of games being owned by the given {@link User},
     * according to filtering set in the given {@code subQuery}.
     *
     * @param query          The {@link String} holding the SQL query.
     * @param pageNumber     The {@link Page} number.
     * @param pageSize       The {@link Page} size.
     * @param user           The {@link User} owning the list.
     * @param shelfIds       A {@link List} of ids of {@link Shelf} to be used for filtering.
     * @param statusesString A {@link List} of {@link PlayStatus} to be used for filtering.
     * @param em             The {@link EntityManager} used to perform the query.
     * @return The {@link List} of games that will be included in the resulting {@link Page}.
     */
    private static List<Game> getList(String query, int pageNumber, int pageSize,
                                      User user, List<Long> shelfIds, List<String> statusesString, EntityManager em) {
        // Data extraction, paging and sorting query
        final Query dataQuery = em.createNativeQuery(query, Game.class)
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .setParameter("userId", user.getId());
        if (!shelfIds.isEmpty()) {
            dataQuery.setParameter("shelves", shelfIds.isEmpty() ? "''" : shelfIds);
        }
        if (!statusesString.isEmpty()) {
            dataQuery.setParameter("statuses", statusesString.isEmpty() ? "''" : statusesString);
        }
        //noinspection unchecked
        return dataQuery.getResultList();
    }
}
