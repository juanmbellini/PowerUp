package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
import ar.edu.itba.paw.webapp.exceptions.UserExistsException;
import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.interfaces.UserDao;
import ar.edu.itba.paw.webapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;



/**
 * Implementation of {@link ar.edu.itba.paw.webapp.interfaces.UserDao} oriented towards JDBC.
 */
@Repository
public class UserJdbcDao implements UserDao {

    private int MAX_FILTERS_CHECKS_RECOMMEND = 10;

    private final JdbcTemplate jdbcTemplate;
    private final GameDao gameDao;
    private final SimpleJdbcInsert userCreator,
            userAuthorityCreator,
            gameScoreInserter,
            gamePlayStatusInserter;
    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            //Fetch this user's authorities
            List<Authority> authorities = new ArrayList<>();
            for(Map<String, Object> row : jdbcTemplate.queryForList("SELECT authority FROM power_up.user_authorities WHERE username = ?", rs.getString("username"))) {
                authorities.add(Authority.valueOf((String)row.get("authority")));
            }
            return new User(rs.getLong("id"), rs.getString("email"), rs.getString("username"), rs.getString("hashed_password"), authorities);
        }
    };

    @Autowired
    public UserJdbcDao(DataSource dataSource, GameDao gameDao) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.gameDao = gameDao;
        userCreator = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("power_up")
                .withTableName("users")
                .usingColumns("email", "username", "hashed_password")
                .usingGeneratedKeyColumns("id");
        userAuthorityCreator = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("power_up")
                .withTableName("user_authorities")
                .usingColumns("username", "authority");
        gamePlayStatusInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("power_up")
                .withTableName("game_play_statuses")
                .usingGeneratedKeyColumns("id");
        gameScoreInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withSchemaName("power_up")
                .withTableName("game_scores")
                .usingGeneratedKeyColumns("id");
    }

    protected JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Override
    public User create(String email, String hashedPassword, String username) {
        if (email == null) {
            throw new IllegalArgumentException("Email can't be null");
        }
        if (hashedPassword == null) {
            throw new IllegalArgumentException("Hashed password can't be null");
        }
        if (username == null) {
            throw new IllegalArgumentException("Username can't be null");
        }

        if (existsWithEmail(email)) {
            throw new UserExistsException("Email " + email + " already taken");
        }
        if (existsWithUsername(username)) {
            throw new UserExistsException("Username " + username + " already taken");
        }

        final Map<String, Object> userArgs = new HashMap<>();
        userArgs.put("email", email);                         //TODO ensure it's a valid email
        userArgs.put("hashed_password", hashedPassword);
        userArgs.put("username", username);
        final Map<String, Object> authorityArgs = new HashMap<>();
        authorityArgs.put("username", username);
        authorityArgs.put("authority", Authority.USER.name());
        try {
            Number id = userCreator.executeAndReturnKey(userArgs);
            userAuthorityCreator.execute(authorityArgs);
            return new User(id.longValue(), email, username, hashedPassword, Authority.USER);
        } catch (Exception e) {
            throw new UserExistsException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        final String query = "SELECT * FROM power_up.users WHERE username = ? LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, username);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : completeUser(result.get(0));
    }

    @Override
    public User findByEmail(String email) {
        final String query = "SELECT * FROM power_up.users WHERE LOWER(email) = LOWER(?) LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, email);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : completeUser(result.get(0));
    }

    @Override
    public User findById(long id) {
        final String query = "SELECT * FROM power_up.users WHERE id = ? LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, id);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : completeUser(result.get(0));
    }

    @Override
    public boolean existsWithId(long id) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM power_up.users WHERE id = ?", new Object[]{id}, Integer.class);
        return count > 0;
    }

    @Override
    public boolean existsWithUsername(String username) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM power_up.users WHERE username = ?", new Object[]{username}, Integer.class);
        return count > 0;
    }

    @Override
    public boolean existsWithEmail(String email) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM power_up.users WHERE LOWER(email) = LOWER(?)", new Object[]{email}, Integer.class);
        return count > 0;
    }

    @Override
    public void scoreGame(User user, long gameId, int score) {
        if (user == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        //TODO make a function in GameDao that checks whether a game with a given ID exists
        if (!gameDao.existsWithId(gameId)) {
            throw new IllegalArgumentException("No game with ID " + gameId);
        }
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Score must be between 1 and 10");
        }

        //Update if exists, otherwise insert
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM power_up.game_scores WHERE user_id = ? AND game_id = ?", new Object[]{user.getId(), gameId}, Integer.class);
        if (count > 0) {
            jdbcTemplate.update("UPDATE power_up.game_scores SET score = ? WHERE user_id = ? AND game_id = ?", score, user.getId(), gameId);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", user.getId());
            params.put("game_id", gameId);
            params.put("score", score);
            gameScoreInserter.execute(params);
        }

        user.scoreGame(gameId, score);
        //TODO ver lo de merca y race condition
        //TODO cambiar para que sea mejor que en todas las veces
        String querySelect = "SELECT counter FROM power_up.games WHERE id = ?";
        int counter = 1 + jdbcTemplate.queryForObject(querySelect, new Object[]{gameId}, Integer.class);
        String queryUpdate = "UPDATE power_up.games SET counter=? WHERE id = ?";
        jdbcTemplate.update(queryUpdate, counter, gameId);
        if (counter % 1 == 0) {
            gameDao.updateAvgScore(gameId);
        }
    }

    @Override
    public void scoreGame(User user, Game game, int score) {
        if (game == null) {
            throw new IllegalArgumentException("Game can't be null");
        }
        scoreGame(user, game.getId(), score);
    }

    @Override
    public void setPlayStatus(User user, long gameId, PlayStatus status) {
        if (user == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status can't be null");
        }
        //TODO make a function in GameDao that checks whether a game with a given ID exists
        if (!gameDao.existsWithId(gameId)) {
            throw new IllegalArgumentException("No game with ID " + gameId);
        }

        //Update if exists, otherwise insert
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM power_up.game_play_statuses WHERE user_id = ? AND game_id = ?", new Object[]{user.getId(), gameId}, Integer.class);
        if (count > 0) {
            jdbcTemplate.update("UPDATE power_up.game_play_statuses SET status = ? WHERE user_id = ? AND game_id = ?", status.name(), user.getId(), gameId);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", user.getId());
            params.put("game_id", gameId);
            params.put("status", status.name());
            gamePlayStatusInserter.execute(params);
        }
        user.setPlayStatus(gameId, status);
    }

    @Override
    public void setPlayStatus(User user, Game game, PlayStatus status) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        setPlayStatus(user, game.getId(), status);
    }

    @Override
    public Collection<Game> recommendGames(User user) {

        //Get all scoredGames and give all filters a weight based on the score on each appearance

        Map<Long,Integer> scoredGamed = user.getScoredGames();
        if(scoredGamed==null) throw new IllegalStateException();
        //Map from a filter category, to a map from the filter value to an array with all the scores.
        Map<FilterCategory, Map<String,ArrayList<Integer>>> scoredGamesWithScoreArray = new HashMap<>();
        for (FilterCategory filterCategory: FilterCategory.values()){
            scoredGamesWithScoreArray.put(filterCategory,new HashMap<>());
        }
        for (long id: scoredGamed.keySet()) {
            Game game = gameDao.findById(id);

            int score = scoredGamed.get(game.getId());

            //Genre
            Map<String,ArrayList<Integer>> genreMap = scoredGamesWithScoreArray.get(FilterCategory.genre);
            addScoreOfScoredGamedToCategory(genreMap,game.getGenres(),score);

            //Platform
            Map<String,ArrayList<Integer>> platformMap = scoredGamesWithScoreArray.get(FilterCategory.platform);
            addScoreOfScoredGamedToCategory(platformMap,game.getPlatforms().keySet(),score);

            //Keywords
            Map<String,ArrayList<Integer>> keywordMap = scoredGamesWithScoreArray.get(FilterCategory.keyword);
            addScoreOfScoredGamedToCategory(keywordMap,game.getKeywords(),score);

            //Developers
            Map<String,ArrayList<Integer>> developerMap = scoredGamesWithScoreArray.get(FilterCategory.developer);
            addScoreOfScoredGamedToCategory(developerMap,game.getDevelopers(),score);

            //Publishers
            Map<String,ArrayList<Integer>> publisherMap = scoredGamesWithScoreArray.get(FilterCategory.publisher);
            addScoreOfScoredGamedToCategory(publisherMap,game.getPublishers(),score);
        }

        //Give all filter a weight
        int[] weightForScore = new int[]{-8,-4,-2,-1,0,1,2,4,8,16};

        Map<Integer,Map<FilterCategory,ArrayList<String>>> scoredGamesWithFiltersWeight = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        //TODO put all same type filters together

        for(FilterCategory filterCategory: scoredGamesWithScoreArray.keySet()){
            Map<String,ArrayList<Integer>> filterCategoryMap = scoredGamesWithScoreArray.get(filterCategory);
            for(String filter: filterCategoryMap.keySet()){
                int weight=0;
                for(int score: filterCategoryMap.get(filter)){
                    int scoreIndex = score-1;
                    if(scoreIndex<0 || scoreIndex>9) {
                        throw new IllegalStateException();
                    }
                    weight+=weightForScore[score-1];
                }
                if(!scoredGamesWithFiltersWeight.containsKey(weight)){
                    scoredGamesWithFiltersWeight.put(weight,new HashMap<>());
                    for (FilterCategory filterCategoryAux: FilterCategory.values()){
                        scoredGamesWithFiltersWeight.get(weight).put(filterCategoryAux,new ArrayList<>());
                    }
                }
                ArrayList<String> filterArray = scoredGamesWithFiltersWeight.get(weight).get(filterCategory);
                filterArray.add(filter);
            }
        }

        //Get all games with each of X filter with higher weight and give each a weight based on each appearance and avg_score(?

        HashMap<Long,Integer> gamesWeightMap = new HashMap();

        //Initialize all games in 0. eliminate this if it is taking too long. Only add something to the ones who have much
        //more negatives than positive or have too few games.
        Collection<Game> resultGames = gameDao.searchGames("",new HashMap(), OrderCategory.avg_score,false);
        for(Game game: resultGames){
            long gameId = game.getId();
            if(!scoredGamed.containsKey(gameId)){
                gamesWeightMap.put(gameId,0);
            }
        }

        int counter=0;
        //TODO change this to pick filters in a smart way. (one of each and repeat?) Less importance to platform?
        for(int weight: scoredGamesWithFiltersWeight.keySet()){
            Map<FilterCategory,ArrayList<String>> mapFilters = scoredGamesWithFiltersWeight.get(weight);
            for(FilterCategory filterCategory: mapFilters.keySet()){
                for (String filter: mapFilters.get(filterCategory)){

                    HashMap<FilterCategory,List<String>> filterParameterMap = new HashMap();
                    ArrayList filterArrayParameter = new ArrayList<String>();
                    filterArrayParameter.add(filter);
                    filterParameterMap.put(filterCategory,filterArrayParameter);

                    System.out.println(filter);
                    resultGames = gameDao.searchGames("",filterParameterMap, OrderCategory.avg_score,false);

                    for(Game game: resultGames){
                        long gameId = game.getId();
                        if(!scoredGamed.containsKey(gameId)){
                            if(!gamesWeightMap.containsKey(gameId)) gamesWeightMap.put(gameId,(int)game.getAvgScore());
                            gamesWeightMap.put(gameId,gamesWeightMap.get(gameId)+weight);
                        }
                    }
                    counter++;
                    if(counter>=MAX_FILTERS_CHECKS_RECOMMEND) break;
                }
                if(counter>=MAX_FILTERS_CHECKS_RECOMMEND) break;
            }
            if(counter>=MAX_FILTERS_CHECKS_RECOMMEND)break;
        }

        //Show all games ordered by weight. (And score if not used before)
        Map<Integer,ArrayList<Long>> gameWeightMapInOrder = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        Map<Long,Game> longGameMap = gameDao.findBasicDataGamesFromArrayId( gamesWeightMap.keySet());

        for (Long gameId: gamesWeightMap.keySet()){
            int gameWeight = gamesWeightMap.get(gameId);
            if(!gameWeightMapInOrder.containsKey(gameWeight)){
                gameWeightMapInOrder.put(gameWeight,new ArrayList<>());
            }

            gameWeightMapInOrder.get(gameWeight).add(gameId);

        }

        Collection<Game> finalResultList = null;
        if(!gameWeightMapInOrder.isEmpty()){
            finalResultList = new LinkedHashSet<>();

            for(Integer gameWeight : gameWeightMapInOrder.keySet()){
                for(Long id: gameWeightMapInOrder.get(gameWeight)){
                    finalResultList.add(longGameMap.get(id));
//                    System.out.println(longGameMap.get(id).getName()+" : "+ gameWeight );
                }
            }

        }
        //TODO make avgScore more important?

        if(finalResultList==null){
            finalResultList=gameDao.searchGames("",new HashMap<>(), OrderCategory.avg_score,false);
        }
        return finalResultList;
    }

    private void addScoreOfScoredGamedToCategory(Map<String, ArrayList<Integer>> filterCategoryMap, Collection<String> filters, Integer score) {

        for(String filter :  filters){
            if(!filterCategoryMap.containsKey(filter)){
                filterCategoryMap.put(filter,new ArrayList<>());
            }
            filterCategoryMap.get(filter).add(score);
        }
    }

    /**
     * Checks whether there is a row matching a specific condition on a special table.
     * TODO put this method in a more generic DAO, this can be used accross multiple tables.
     *
     * @param tableName   The name of the table. Can include schema prefix.
     * @param whereClause Condition to meet.
     * @return Whether there is such a row in the specified table.
     */
    private boolean rowExists(String tableName, String whereClause) { //TODO esto se habia borrado no? se duplico tal vez?
        final boolean[] result = {false};
        //No prepared statement here since this is run from a trusted source.
        jdbcTemplate.query("SELECT COUNT(*) AS ct FROM " + tableName + " WHERE " + whereClause, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                result[0] = rs.getInt("ct") > 0;
            }
        });
        return result[0];
    }

    /**
     * Adds relationship information for a specific user that is not readily available from the users table (e.g. scored
     * games, played games)
     *
     * @param u The user whose data to complete.
     * @return The completed user (returned value == u) so you can chain this call.
     */
    private User completeUser(User u) {
        //Scores
        Map<Long, Integer> scores = new HashMap<>();
        jdbcTemplate.query(
                "SELECT * FROM power_up.game_scores WHERE user_id = ?",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        scores.put(rs.getLong("game_id"), rs.getInt("score"));
                    }
                },
                u.getId());
        u.setScoredGames(scores);

        //Played games
        Map<Long, PlayStatus> statuses = new HashMap<>();
        jdbcTemplate.query(
                "SELECT * FROM power_up.game_play_statuses WHERE user_id = ?",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        statuses.put(rs.getLong("game_id"), PlayStatus.valueOf(rs.getString("status")));
                    }
                },
                u.getId());
        u.setPlayStatuses(statuses);

        return u;
    }
}
