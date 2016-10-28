//package ar.edu.itba.paw.webapp.persistence;
//
//import ar.edu.itba.paw.webapp.exceptions.FailedToProcessQueryException;
//import ar.edu.itba.paw.webapp.exceptions.IllegalPageException;
//import ar.edu.itba.paw.webapp.interfaces.*;
//import ar.edu.itba.paw.webapp.model.FilterCategory;
//import ar.edu.itba.paw.webapp.model.Game;
//import ar.edu.itba.paw.webapp.model.OrderCategory;
//import ar.edu.itba.paw.webapp.utilities.Page;
//import org.atteo.evo.inflector.English;
//import org.joda.time.LocalDate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.RowCallbackHandler;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.sql.DataSource;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.*;
//
//
///**
// * Implementation of {@link GameDao} oriented towards JDBC.
// */
//@Repository
//public class GameJdbcDao extends BaseJdbcDao implements GameDao {
//
//    private static final int STRING_BUILDER_INITIAL_CAPACITY = 2048;
//    private static final int STRING_BUILDER_SMALL_INITIAL_CAPACITY = 128;
//    private static final int MAX_PAGE_SIZE = 150;
//
//
//    private PlatformDao platformDao;
//    private GenreDao genreDao;
//    private PublisherDao publisherDao;
//    private DeveloperDao developerDao;
//    private KeywordDao keywordDao;
//    private PictureDao picturesDao;
//
//    @Autowired
//    public GameJdbcDao(DataSource dataSource,
//                       PlatformDao platformDao,
//                       GenreDao genreDao,
//                       PublisherDao publisherDao,
//                       DeveloperDao developerDao,
//                       KeywordDao keywordDao,
//                       PictureDao pictureDao) {
//        super(dataSource);
//        this.platformDao = platformDao;
//        this.genreDao = genreDao;
//        this.publisherDao = publisherDao;
//        this.developerDao = developerDao;
//        this.keywordDao = keywordDao;
//        this.picturesDao = pictureDao;
//    }
//
//    @Transactional
//    @Override
//    public Collection<Game> searchGames(String name, Map<FilterCategory, List<String>> filters,
//                                        OrderCategory orderCategory, boolean ascending)
//            throws IllegalArgumentException {
//        return doSearchGames(name, filters, orderCategory, ascending, 0, 0).getData();
//    }
//
//
//    @Transactional
//    @Override
//    public Page<Game> searchGames(String name, Map<FilterCategory, List<String>> filters,
//                                  OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber)
//            throws IllegalArgumentException {
//        if (pageSize <= 0 || pageNumber <= 0 || pageSize > MAX_PAGE_SIZE) {
//            throw new IllegalPageException();
//        }
//        return doSearchGames(name, filters, orderCategory, ascending, pageSize, pageNumber);
//    }
//
//
////    @Transactional
////    @Override
////    public Set<Game> findRelatedGames(Game baseGame, Set<FilterCategory> filters) {
////
////        Map<FilterCategory, List<String>> filtersMap = new HashMap<>();
////        if (filters.contains(FilterCategory.publisher)) {
////            filtersMap.put(FilterCategory.publisher,
////                    new ArrayList<>(baseGame.getPublishers()));
////        }
////        if (filters.contains(FilterCategory.developer)) {
////            filtersMap.put(FilterCategory.developer,
////                    new ArrayList<>(baseGame.getDevelopers()));
////        }
////        if (filters.contains(FilterCategory.genre)) {
////            filtersMap.put(FilterCategory.genre,
////                    new ArrayList<>(baseGame.getGenres()));
////        }
////        if (filters.contains(FilterCategory.keyword)) {
////            filtersMap.put(FilterCategory.keyword,
////                    new ArrayList<>(baseGame.getKeywords()));
////        }
////        if (filters.contains(FilterCategory.platform)) {
////            filtersMap.put(FilterCategory.platform,
////                    new ArrayList<>(baseGame.getPlatforms().keySet()));
////        }
////        Set<Game> result = new HashSet<>(searchGames("", filtersMap, OrderCategory.name, true));
////        result.remove(baseGame);
////        return result;
////
////    }
//
//    @Override
//    @Transactional
//    public Game findById(long id) {
//        final Game.GameBuilder gb = new Game.GameBuilder();
//        Object[] parameters = new Object[1];
//        parameters[0] = id;
//
//        String query = "SELECT games.id, games.name, summary, release, avg_score, cover_picture_cloudinary_id" +
//                " FROM games WHERE games.id = ?";
//        try {
//            getJdbcTemplate().query(query.toLowerCase(), parameters, new RowCallbackHandler() {
//                @Override
//                public void processRow(ResultSet rs) throws SQLException {
//                    gb.setId(rs.getLong("id"))
//                            .setName(rs.getString("name"))
//                            .setSummary(rs.getString("summary"))
//                            .setAvgScore(rs.getDouble("avg_score"))
//                            .setReleaseDate(new LocalDate(rs.getString("release")))
//                            .setCoverPictureUrl(rs.getString("cover_picture_cloudinary_id"));
//                }
//            });
//        } catch (Exception e) {
//            throw new FailedToProcessQueryException();
//        }
//        return gb.startedBuilding() ? completeGame(gb).build() : null;
//    }
//
//    @Override
//    public boolean existsWithId(long id) {
//        int count = getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM games WHERE id = ?", new Object[]{id}, Integer.class);
//        return count > 0;
//    }
//
//    @Override
//    public boolean existsWithTitle(String title) {
//        int count = getJdbcTemplate().queryForObject("SELECT COUNT(*) FROM games WHERE LOWER(name) = LOWER(?)", new Object[]{title}, Integer.class);
//        return count > 0;
//    }
//
//    @Override
//    public Collection<String> getFiltersByType(FilterCategory filterCategory) {
//        String tableName = English.plural(filterCategory.name());
//        Set<String> result = new LinkedHashSet<>();
//        StringBuilder query = new StringBuilder().append("SELECT ");
//        StringBuilder fromSentence = new StringBuilder().append(" FROM ");
//
//        if (filterCategory != FilterCategory.developer && filterCategory != FilterCategory.publisher) {
//            query.append(tableName);
//            fromSentence.append(tableName);
//        } else {
//            query.append("companies");
//            fromSentence.append("companies");
//            fromSentence.append(" INNER JOIN game_")
//                    .append(tableName)
//                    .append(" ON companies.id = game_")
//                    .append(tableName)
//                    .append(".")
//                    .append(filterCategory.name())
//                    .append("_id");
//        }
//        query.append(".name")
//                .append(fromSentence)
//                .append(" ORDER BY name ASC LIMIT 500;");
//        try {
//            getJdbcTemplate().query(query.toString().toLowerCase(), (Object[]) null, new RowCallbackHandler() {
//                @Override
//                public void processRow(ResultSet rs) throws SQLException {
//                    result.add(rs.getString("name"));
//                }
//            });
//        } catch (Exception e) {
//            throw new FailedToProcessQueryException();
//        }
//        return result;
//    }
//
//
//    @Transactional
//    public void updateAvgScore(long gameId) {
//        String query = new StringBuilder(" UPDATE games SET avg_score = COALESCE(").
//                append("(SELECT AVG(CAST(score AS FLOAT))")
//                .append(" FROM game_scores")
//                .append(" WHERE game_scores.game_id = ?), 0)")
//                .append(" WHERE id = ?").toString();
//        getJdbcTemplate().update(query, gameId, gameId);
//    }
//
//    /**
//     * @param ids the collection of the ids
//     * @return a Map from gameId to a Game with the basic data of the game.
//     */
//    @Transactional
//    public Map<Long, Game> findBasicDataGamesFromArrayId(Collection<Long> ids) {
//        if (ids == null) throw new IllegalArgumentException();
//        Map<Long, Game> gameMap = new HashMap();
//        if (ids.isEmpty()) return gameMap;
//        StringBuilder queryBuilder = new StringBuilder().append("SELECT id, name, summary, release, avg_score,cover_picture_cloudinary_id FROM games WHERE");
//        Iterator<Long> iter = ids.iterator();
//        queryBuilder.append(" id = " + iter.next());
//        while (iter.hasNext()) { //TODO change to id IN (id1, id2,...)
//            queryBuilder.append(" OR ");
//            queryBuilder.append(" id = " + iter.next());
//        }
//        String query = queryBuilder.toString();
//
//        try {
//            getJdbcTemplate().query(query.toLowerCase(), new RowCallbackHandler() { //TODO usar object[] o al pedo porque es seguro?
//                        @Override
//                        public void processRow(ResultSet rs) throws SQLException {
//                            Game game = new Game.GameBuilder().setId(rs.getLong("id"))
//                                    .setName(rs.getString("name"))
//                                    .setSummary(rs.getString("summary"))
//                                    .setAvgScore(rs.getDouble("avg_score"))
//                                    .setReleaseDate(new LocalDate(rs.getString("release")))
//                                    .setCoverPictureUrl(rs.getString("cover_picture_cloudinary_id")).build();
//
//                            gameMap.put(game.getId(), game);
//                        }
//                    }
//            );
//        } catch (Exception e) {
//            throw new FailedToProcessQueryException();
//        }
//        return gameMap;
//    }
//
//
//    /**
//     * Gives a GameBuilder with completed fields.
//     *
//     * @param gb The Game Builder to be completed.
//     * @return The completed Game Builder.
//     */
//    private Game.GameBuilder completeGame(Game.GameBuilder gb) {
//        Map<String, LocalDate> platforms = platformDao.getGamePlatforms(gb.getBuildingGameId());
//        for (String platform : platforms.keySet()) {
//            gb.addPlatform(platform, platforms.get(platform));
//        }
//        genreDao.getGameGenres(gb.getBuildingGameId()).forEach(gb::addGenre);
//        publisherDao.getGamePublishers(gb.getBuildingGameId()).forEach(gb::addPublisher);
//        developerDao.getGameDevelopers(gb.getBuildingGameId()).forEach(gb::addDeveloper);
//        keywordDao.getGameKeywords(gb.getBuildingGameId()).forEach(gb::addKeyword);
//        picturesDao.getGamePictures(gb.getBuildingGameId()).forEach(gb::addPictureURL);
//        return gb;
//    }
//
//    /**
//     * Gives a Game with completed fields.
//     *
//     * @param game The Game to be completed
//     * @return The completed Game.
//     */
//    private Game completeGame(Game game) {
//        Map<String, LocalDate> platforms = platformDao.getGamePlatforms(game.getId());
//        for (String platform : platforms.keySet()) {
//            game.addPlatform(platform, platforms.get(platform));
//        }
//        genreDao.getGameGenres(game.getId()).forEach(game::addGenre);
//        publisherDao.getGamePublishers(game.getId()).forEach(game::addPublisher);
//        developerDao.getGameDevelopers(game.getId()).forEach(game::addDeveloper);
//        keywordDao.getGameKeywords(game.getId()).forEach(game::addKeyword);
//        picturesDao.getGamePictures(game.getId()).forEach(game::addPictureURL);
//        return game;
//    }
//
//
//    /**
//     * Makes the search games query to the database
//     *
//     * @param name          The game's name
//     * @param filters       A map containing filters (and respective values) for the query
//     * @param orderCategory Indicates how to order the resultant collection
//     * @param ascending     Indicates whether the resultant collection must be ordered in an ascending or descending way
//     * @param pageSize      Indicates how many elements should be in the resultant collection.
//     *                      If {@code 0}, and {@code pageNumber} is also {@code 0},
//     *                      then the entire collection is returned (i.e. pagination is turned off).
//     * @param pageNumber    Indicates which page should be returned in the resultant collection
//     * @return The resultant collection after making the query with the given parameters
//     * @throws IllegalArgumentException If {@code name} is null, {@code filters} is null,
//     *                                  {@code orderCategory} is null, if a list in the {@code filters} map is null,
//     *                                  if {@code pageSize} is negative, if {@code pageNumber} is negative
//     *                                  or if {@code pageSize} is {@code 0} and {@code pageNumber} not, or vice versa.
//     * @throws IllegalPageException     If there were problems creating a page with results
//     *                                  (i.e. {@code pageSize} smaller than the amount of elements in the result set,
//     *                                  {@code pageNumber} bigger than the total amount of pages available,
//     *                                  or Illegal arguments when creating the page).
//     */
//    private Page<Game> doSearchGames(String name, Map<FilterCategory, List<String>> filters,
//                                     OrderCategory orderCategory, boolean ascending, int pageSize, int pageNumber)
//            throws IllegalArgumentException {
//        if (name == null || filters == null || orderCategory == null || pageSize < 0 || pageNumber < 0
//                || (pageSize == 0 && pageNumber != 0) || (pageNumber == 0 && pageSize != 0)) {
//            throw new IllegalArgumentException();
//        }
//        String[] parameters = new String[countFilters(filters) + 1];
//        parameters[0] = name;
//        boolean paginationOn = pageSize > 0;
//
//        StringBuilder selectString = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY)
//                .append("SELECT games.id, games.name, avg_score, summary, " +
//                        "games.release, cover_picture_cloudinary_id");
//        StringBuilder fromString = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY)
//                .append("FROM games")
//                .append(" INNER JOIN game_platforms ON games.id = game_platforms.game_id")
//                .append(" INNER JOIN platforms ON game_platforms.platform_id = platforms.id");
//        StringBuilder nameString = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY)
//                .append("WHERE LOWER(games.name) like '%' || LOWER(?) || '%'");
//        StringBuilder filtersString = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY);
//        StringBuilder groupByString = new StringBuilder(STRING_BUILDER_SMALL_INITIAL_CAPACITY)
//                .append("GROUP BY games.id, games.name, avg_score, cover_picture_cloudinary_id, summary");
//
//        addDoSearchGamesFilters(filters, parameters, fromString, filtersString, 1);
//
//        StringBuilder queryBuilderWithoutSelectGroupByAndOrderBy = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY)
//                .append(" ")
//                .append(fromString)
//                .append(" ")
//                .append(nameString)
//                .append(filtersString);
//
//        StringBuilder dataFetchQuery = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY)
//                .append(selectString)
//                .append(queryBuilderWithoutSelectGroupByAndOrderBy)
//                .append(" ")
//                .append(groupByString)
//                .append(" ORDER BY games.").append(orderCategory.name())
//                .append(ascending ? " ASC" : " DESC");
//        ;
//        StringBuilder rowsCountQuery = new StringBuilder(STRING_BUILDER_INITIAL_CAPACITY)
//                .append("SELECT count(DISTINCT games.id) AS rows")
//                .append(queryBuilderWithoutSelectGroupByAndOrderBy);
//
//        Set<Game> gamesSet = new LinkedHashSet<>();
//        final Page<Game> page = new Page<>();
//        if (paginationOn) {
//            page.setPageSize(pageSize);
//            dataFetchQuery.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(pageSize * (pageNumber - 1));
//        } else {
//            page.setTotalPages(1);
//            page.setPageNumber(1);
//        }
//
//        try {
//            // Count rows
//            getJdbcTemplate().query(rowsCountQuery.toString(), parameters, new RowCallbackHandler() {
//                @Override
//                public void processRow(ResultSet rs) throws SQLException {
//                    int rowsCount = rs.getInt("rows");
//                    if (paginationOn) {
//                        int ratio = rowsCount / pageSize;
//                        int totalPages = (rowsCount % pageSize == 0) ? ratio : ratio + 1;
//                        page.setTotalPages(totalPages == 0 ? 1 : totalPages);   // With empty result set, one page is
//                        page.setPageNumber(pageNumber);                         // returned.
//                    } else {
//                        page.setPageSize(rowsCount);
//                    }
//                    page.setOverAllAmountOfElements(rowsCount);
//                }
//            });
//            // Fetch data
//            getJdbcTemplate().query(dataFetchQuery.toString(), parameters, new RowCallbackHandler() {
//                @Override
//                public void processRow(ResultSet rs) throws SQLException {
//                    Game.GameBuilder gb = new Game.GameBuilder()
//                            .setId(rs.getLong("id"))
//                            .setName(rs.getString("name"))
//                            .setSummary(rs.getString("summary"))
//                            .setReleaseDate(new LocalDate(rs.getString("release")))
//                            .setAvgScore(rs.getDouble("avg_score"))
//                            .setCoverPictureUrl(rs.getString("cover_picture_cloudinary_id"));
//
//                    gamesSet.add(gb.build());
//                }
//            });
//        } catch (IllegalPageException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new FailedToProcessQueryException();
//        }
//
//        page.setData(gamesSet);
//        return page;
//    }
//
//
//    /**
//     * Adds corresponding words to the given SQL sentences.
//     *
//     * @param filters        The map of filters
//     * @param parameters     The parameters array
//     * @param tablesString   The FROM sentence (joins are made on demand)
//     * @param filtersString  The WHERE sentence
//     * @param parameterCount Used for indexing the {@code parameters} array
//     * @throws IllegalArgumentException If the map contains a null value for a filterCategory
//     */
//    private void addDoSearchGamesFilters(Map<FilterCategory, List<String>> filters, String[] parameters,
//                                         StringBuilder tablesString, StringBuilder filtersString, int parameterCount)
//            throws IllegalArgumentException {
//        for (FilterCategory filter : filters.keySet()) {
//
//            List<String> values = filters.get(filter);
//            if (values == null) {
//                throw new IllegalArgumentException("A list must be specified for the filter" + filter.name());
//            }
//            int valuesSize = values.size();
//            if (valuesSize > 0) {
//
//                // Tables join string (Joins with specific table if a filter of that table is needed)
//                if (!filter.equals(FilterCategory.platform)) { // table "platforms" is already joined
//                    tablesString.append(" ").append(createJoinSentence(filter));
//                }
//                filtersString.append(" AND ( ");
//
//                boolean firstValue = true;      // Used to check if an 'OR' must be added to the filters string
//                for (String value : values) {
//                    if (!firstValue) {
//                        filtersString.append(" OR ");
//                    }
//                    filtersString.append(createFilterSentence(filter));
//                    parameters[parameterCount] = value;
//                    parameterCount++;
//                    firstValue = false;
//                }
//                filtersString.append(" )");
//            }
//        }
//    }
//
//    /**
//     * Creates a join sentence to be added into the FROM clause.
//     *
//     * @param filter The filter whose table (and the corresponding relation table) must be joined.
//     * @return The created sentence.
//     */
//    private String createJoinSentence(FilterCategory filter) {
//
//        String filterName = filter.name();
//        String pluralFilterName = English.plural(filterName);
//        String entityTable = getEntityTable(filter);
//        String relationTable = "game_" + pluralFilterName;
//
//        StringBuilder sentence = new StringBuilder(STRING_BUILDER_SMALL_INITIAL_CAPACITY)
//                .append("INNER JOIN ").append(relationTable).append(" ON games.id = ")
//                .append(relationTable).append(".game_id").append(" INNER JOIN ").append(entityTable).append(" ON ")
//                .append(relationTable).append(".").append(filterName).append("_id = ").append(pluralFilterName)
//                .append(".id");
//
//        return sentence.toString();
//    }
//
//    /**
//     * Creates a sentence to be added to the WHERE clause.
//     * <p>
//     * This sentence compares a given field (specified by the filter param)
//     * and a '?' param, that must be filled afterward in a parameters array.
//     * </p>
//     *
//     * @param filter The filter whose value must be checked
//     * @return The created sentence.
//     */
//    private String createFilterSentence(FilterCategory filter) {
//        return "LOWER(" + English.plural(filter.name()) + ".name) = LOWER(?)";
//    }
//
//    /**
//     * Counts how many filters will be applied (i.e. for each filter type, how many values there are).
//     *
//     * @param filters The map with the filters.
//     * @return How many filters will be applied.
//     */
//    private int countFilters(Map<FilterCategory, List<String>> filters) {
//        int count = 0;
//        for (List<String> list : filters.values()) {
//            count += list.size();
//        }
//        return count;
//    }
//
//    /**
//     * Creates a string with the filter's entity table name.
//     *
//     * @param filter The filter whose table name must be created.
//     * @return A string containing the filter's entity table name.
//     */
//    private String getEntityTable(FilterCategory filter) {
//        boolean useCompany = filter.equals(FilterCategory.developer) || filter.equals(FilterCategory.publisher);
//        String pluralFilter = English.plural(filter.name());
//        return "" + (useCompany ? "companies AS " + pluralFilter : pluralFilter);
//    }
//
//}
