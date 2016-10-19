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
import org.springframework.transaction.annotation.Transactional;

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
            for(Map<String, Object> row : jdbcTemplate.queryForList("SELECT authority FROM user_authorities WHERE username = ?", rs.getString("username"))) {
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
                .withTableName("users")
                .usingColumns("email", "username", "hashed_password")
                .usingGeneratedKeyColumns("id");
        userAuthorityCreator = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_authorities")
                .usingColumns("username", "authority");
        gamePlayStatusInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("game_play_statuses")
                .usingGeneratedKeyColumns("id");
        gameScoreInserter = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("game_scores")
                .usingGeneratedKeyColumns("id");
    }

    protected JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Transactional
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
        userArgs.put("email", email);   //Email validated by WebForm
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

    @Transactional
    @Override
    public User findByUsername(String username) {
        final String query = "SELECT * FROM users WHERE username = ? LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, username);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : completeUser(result.get(0));
    }

    @Transactional
    @Override
    public User findByEmail(String email) {
        final String query = "SELECT * FROM users WHERE LOWER(email) = LOWER(?) LIMIT 1";
        List<User> result;
        try {
            result = jdbcTemplate.query(query, userRowMapper, email);
        } catch (Exception e) {
            throw new FailedToProcessQueryException(e);
        }
        return result.isEmpty() ? null : completeUser(result.get(0));
    }

    @Transactional
    @Override
    public User findById(long id) {
        final String query = "SELECT * FROM users WHERE id = ? LIMIT 1";
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
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE id = ?", new Object[]{id}, Integer.class);
        return count > 0;
    }

    @Override
    public boolean existsWithUsername(String username) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE username = ?", new Object[]{username}, Integer.class);
        return count > 0;
    }

    @Override
    public boolean existsWithEmail(String email) {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users WHERE LOWER(email) = LOWER(?)", new Object[]{email}, Integer.class);
        return count > 0;
    }

    @Transactional
    @Override
    public void scoreGame(User user, long gameId, int score) {
        if (user == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        if (!gameDao.existsWithId(gameId)) {
            throw new IllegalArgumentException("No game with ID " + gameId);
        }
        if (score < 1 || score > 10) {
            throw new IllegalArgumentException("Score must be between 1 and 10");
        }

        //Update if exists, otherwise insert
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM game_scores WHERE user_id = ? AND game_id = ?", new Object[]{user.getId(), gameId}, Integer.class);
        if (count > 0) {
            jdbcTemplate.update("UPDATE game_scores SET score = ? WHERE user_id = ? AND game_id = ?", score, user.getId(), gameId);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", user.getId());
            params.put("game_id", gameId);
            params.put("score", score);
            gameScoreInserter.execute(params);
        }

        user.scoreGame(gameId, score);
        String querySelect = "SELECT counter FROM games WHERE id = ?";
        int counter = 1 + jdbcTemplate.queryForObject(querySelect, new Object[]{gameId}, Integer.class);
        String queryUpdate = "UPDATE games SET counter=? WHERE id = ?";
        jdbcTemplate.update(queryUpdate, counter, gameId);
        //TODO cambiar para que el updateAvgScore no se efectue siempre sino cada X cantidad de cambios, dependiendo de la cantidad de cambios X varie.
        if (counter % 1 == 0) {
            gameDao.updateAvgScore(gameId);
        }
    }

    @Transactional
    @Override
    public void scoreGame(User user, Game game, int score) {
        if (game == null) {
            throw new IllegalArgumentException("Game can't be null");
        }
        scoreGame(user, game.getId(), score);
    }

    @Transactional
    @Override
    public void setPlayStatus(User user, long gameId, PlayStatus status) {
        if (user == null) {
            throw new IllegalArgumentException("User can't be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status can't be null");
        }
        if (!gameDao.existsWithId(gameId)) {
            throw new IllegalArgumentException("No game with ID " + gameId);
        }

        //Update if exists, otherwise insert
        int count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM game_play_statuses WHERE user_id = ? AND game_id = ?", new Object[]{user.getId(), gameId}, Integer.class);
        if (count > 0) {
            jdbcTemplate.update("UPDATE game_play_statuses SET status = ? WHERE user_id = ? AND game_id = ?", status.name(), user.getId(), gameId);
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put("user_id", user.getId());
            params.put("game_id", gameId);
            params.put("status", status.name());
            gamePlayStatusInserter.execute(params);
        }
        user.setPlayStatus(gameId, status);
    }

    @Transactional
    @Override
    public void setPlayStatus(User user, Game game, PlayStatus status) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        setPlayStatus(user, game.getId(), status);
    }

    @Transactional
    @Override
    public void removeScore(User u, long id) {
        if(u==null) throw new IllegalArgumentException();
        if(u.hasScoredGame(id)){
            u.getScoredGames().remove(id);
            jdbcTemplate.update("DELETE FROM game_scores where user_id = ? and game_id = ?",new Object[]{u.getId(),id});
        }
        gameDao.updateAvgScore(id);
    }

    @Transactional
    @Override
    public void removeStatus(User u, long id) {
        if(u==null) throw new IllegalArgumentException();
        if(u.hasPlayStatus(id)){
            u.getPlayStatuses().remove(id);
            jdbcTemplate.update("DELETE FROM game_play_statuses where user_id = ? and game_id = ?",new Object[]{u.getId(),id});
        }
        gameDao.updateAvgScore(id);
    }

    @Override
    public Collection<Game> recommendGames(User user) { //TODO change this to make something more efficient.


        Map<Long,Integer> scoredGames = user.getScoredGames();

        if(scoredGames==null || scoredGames.size()==0) return new LinkedHashSet<>();

        Map<FilterCategory,Map<String,Double>> filtersScoresMap  = new HashMap();

        //Genres
        String genreQuery = "select genres.name as genreName, SUM(score) AS scoreSum, COUNT(score) as countScores " +
                        "FROM games INNER JOIN game_scores ON games.id = game_scores.game_id " +
                        "INNER JOIN game_genres ON games.id = game_genres.game_id " +
                        "INNER JOIN genres ON genres.id = game_genres.genre_id "+
                        "GROUP BY genreName "+
                        "ORDER BY scoreSum DESC "+
                        "LIMIT ? ";
        Map<String,Double> mapFilterToFilterScoreGenre = new HashMap<>();
        jdbcTemplate.query(genreQuery.toLowerCase(), new Object[]{3}, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        mapFilterToFilterScoreGenre.put(rs.getString("genreName"),(double)rs.getInt("scoreSum")/rs.getInt("countScores"));
                    }
                }
        );
        filtersScoresMap.put(FilterCategory.genre,mapFilterToFilterScoreGenre);

        //Keywords
        String keywordQuery = "select keywords.name as keywordName, SUM(score) AS scoreSum, COUNT(score) as countScores " +
                "FROM games INNER JOIN game_scores ON games.id = game_scores.game_id " +
                "INNER JOIN game_keywords ON games.id = game_keywords.game_id " +
                "INNER JOIN keywords ON keywords.id = game_keywords.keyword_id "+
                "GROUP BY keywordName "+
                "ORDER BY scoreSum DESC "+
                "LIMIT ? ";
        Map<String,Double> mapFilterToFilterScoreKeyword = new HashMap<>();
        jdbcTemplate.query(keywordQuery.toLowerCase(), new Object[]{3}, new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        mapFilterToFilterScoreKeyword.put(rs.getString("keywordName"),(double)rs.getInt("scoreSum")/rs.getInt("countScores"));
                    }
                }
        );
        filtersScoresMap.put(FilterCategory.keyword,mapFilterToFilterScoreKeyword);

        //Get all games with each of X filter with higher weight and give each a weight based on each appearance and avg_score(?

        HashMap<Game,Integer> gamesWeightMap = new HashMap();

        //Initialize all games in 0. eliminate this if it is taking too long. Only add something to the ones who have much
        //more negatives than positive or have too few games.
        Collection<Game> resultGames = gameDao.searchGames("",new HashMap(), OrderCategory.avg_score,false);
        for(Game game: resultGames){
            if(!scoredGames.containsKey(game.getId())){
                gamesWeightMap.put(game,(int)(2*game.getAvgScore()));
            }
        }

        for(FilterCategory filterCategory : filtersScoresMap.keySet()){
            Map<String,Double> mapFilter = filtersScoresMap.get(filterCategory);
            for (String filter: mapFilter.keySet()){
                double filterScore = mapFilter.get(filter);
                HashMap<FilterCategory,List<String>> filterParameterMap = new HashMap();
                ArrayList filterArrayParameter = new ArrayList<String>();
                filterArrayParameter.add(filter);
                filterParameterMap.put(filterCategory,filterArrayParameter);
                System.out.println(filter);
                resultGames = gameDao.searchGames("",filterParameterMap, OrderCategory.avg_score,false);
                for(Game game: resultGames){
                    if(gamesWeightMap.containsKey(game)){
                        gamesWeightMap.put(game,gamesWeightMap.get(game)+(int)filterScore);
                    }
                }
            }
        }


        //Show all games ordered by weight.
        Map<Integer,ArrayList<Game>> gameWeightMapInOrder = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });

        for (Game game: gamesWeightMap.keySet()){
            int gameWeight = gamesWeightMap.get(game);
            if(!gameWeightMapInOrder.containsKey(gameWeight)){
                gameWeightMapInOrder.put(gameWeight,new ArrayList<>());
            }
            gameWeightMapInOrder.get(gameWeight).add(game);

        }


        Collection<Game>  finalResultList = new LinkedHashSet<>();
        int counter = 0;
        if(!gameWeightMapInOrder.isEmpty()){
            Iterator<Integer> weightIterator = gameWeightMapInOrder.keySet().iterator();
            while(weightIterator.hasNext() && counter<100){
                int gameWeight = weightIterator.next();
                Iterator<Game> gameIterator = gameWeightMapInOrder.get(gameWeight).iterator();
                while(gameIterator.hasNext() && counter<100){
                    finalResultList.add(gameIterator.next());
                    counter++;
                }
            }
        }

        return finalResultList;
    }

//    private void addScoreOfScoredGamedToCategory(Map<String, ArrayList<Integer>> filterCategoryMap, Collection<String> filters, Integer score) {
//
//        for(String filter :  filters){
//            if(!filterCategoryMap.containsKey(filter)){
//                filterCategoryMap.put(filter,new ArrayList<>());
//            }
//            filterCategoryMap.get(filter).add(score);
//        }
//    }

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
                "SELECT * FROM game_scores WHERE user_id = ?",
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
                "SELECT * FROM game_play_statuses WHERE user_id = ?",
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
