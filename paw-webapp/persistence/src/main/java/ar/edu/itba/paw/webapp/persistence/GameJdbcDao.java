package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Repository
public class GameJdbcDao implements GameDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public GameJdbcDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected JdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }


    public Collection<Game> searchGame(String name, Map<FilterCategory, List<String>> filters) {

//        name.replace(' ', '%');
        String[] parameters = new String[countFilters(filters) + 1];
        parameters[0] = name;

        String tablesString = "SELECT power_up.games.name, avg_score, summary" +
                " FROM power_up.games" +
                " INNER JOIN power_up.game_platforms ON power_up.games.id = power_up.game_platforms.game_id" +
                " INNER JOIN power_up.platforms ON power_up.game_platforms.platform_id = power_up.platforms.id";
        String nameString = "WHERE LOWER(power_up.games.name) like '%' || LOWER(?) || '%'";
        String filtersString = "";
        String groupByString = "GROUP BY power_up.games.id, power_up.games.name, avg_score, summary HAVING ";



        int parameterCount = 1;         // Used for indexing parameters array
        boolean firstFilter = true;     // Used to check if an 'AND' must be added to the group by string
        for (FilterCategory filter : filters.keySet()) {

            List<String> values = filters.get(filter);
            if (values == null) {
                throw new IllegalArgumentException("A list must be specified for the filter" + filter.name());
            }

            int valuesSize = values.size();

            if (valuesSize > 0) {

                // Tables join string (Joins with specific table if a filter of that table is needed)
                if (!filter.equals(FilterCategory.platform)) { // table "platforms" is already joined
                    tablesString += " " + createJoinSentence(filter);
                }

                // Filters string
                filtersString += " AND ( ";

                boolean firstValue = true;      // Used to check if an 'OR' must be added to the filters string
                for (String value : values) {
                    if (!firstValue) {
                        filtersString += " OR ";
                    }
                    filtersString += createFilterSentence(filter);
                    parameters[parameterCount] = value;
                    parameterCount++;
                    firstValue = false;
                }
                filtersString += " )";

                // Group by string
                if (!firstFilter) {
                    groupByString += " AND ";
                }
                groupByString += createHavingSentence(filter, valuesSize);
                firstFilter = false;
            }
        }
        String query = tablesString + " " + nameString + filtersString;
        if (filters.size() > 0) {
            query += " " + groupByString;
        }
        query += ";";
        List<Game> gameList = new ArrayList();
        System.out.println(query);
        jdbcTemplate.query(query.toString().toLowerCase(), parameters, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Game newGame = new Game();
                newGame.setName(rs.getString("name"));
                newGame.setSummary(rs.getString("summary"));
                gameList.add(newGame);
            }
        });
        return gameList;
    }


    /**
     * Creates a join sentence to be added into the FROM clause.
     * @param filter The filter whose table (and the corresponding relation table) must be joined.
     * @return The created sentence.
     */
    private String createJoinSentence (FilterCategory filter) {

        String filterName = filter.name();
        String entityTable = getEntityTable(filter);
        String relationTable = "power_up.game_" + English.plural(filterName);

        String sentence = "INNER JOIN " + relationTable + " ON power_up.games.id = " + relationTable + ".game_id";
        sentence += " INNER JOIN " + entityTable
                + " ON " + relationTable + "." + filterName + "_id = " + entityTable + ".id";
        return sentence;
    }

    /**
     * Creates a sentence to be added to the WHERE clause.
     * <p>
     *     This sentence compares a given field (specified by the filter param)
     *     and a '?' param, that must be filled afterward in a parameters array.
     * </p>
     * @param filter The filter whose value must be checked
     * @return The created sentence.
     */
    private String createFilterSentence(FilterCategory filter) {
        return "LOWER(" + getEntityTable(filter) + ".name) = LOWER(?)";
    }

    /**
     * Creates a HAVING clause sentence to be added to the query.
     * <p>This sentence compares how many tuples there are that verify a given filter with a passed value</p>
     * @param filter The filter whose sum must be compared.
     * @param valuesCount The value to which the sum must be compared.
     * @return The created sentence.
     */
    private String createHavingSentence(FilterCategory filter, int valuesCount) {
        return "COUNT(DISTINCT " + getEntityTable(filter) + ".name) = " + valuesCount;
    }


    /**
     * Counts how many filters will be applied (i.e. for each filter type, how many values there are).
     * @param filters The map with the filters.
     * @return How many filters will be applied.
     */
    private int countFilters(Map<FilterCategory, List<String>> filters) {
        int count = 0;
        for (List<String> list : filters.values()) {
            count += list.size();
        }
        return count;
    }

    /**
     * Creates a string with the filter's entity table name.
     * @param filter The filter whose table name must be created.
     * @return A string containing the filter's entity table name.
     */
    private String getEntityTable(FilterCategory filter) {
        boolean useCompany = filter.equals(FilterCategory.developer) || filter.equals(FilterCategory.publisher);
        return "power_up." + (useCompany ? "companies": English.plural(filter.name()));
    }




}

