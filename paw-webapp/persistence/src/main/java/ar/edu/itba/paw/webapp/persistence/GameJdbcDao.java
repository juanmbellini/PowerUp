package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;
import org.atteo.evo.inflector.English;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
//import org.springframework.jdbc.core.JdbcTemplate;


import javax.sql.DataSource;
import javax.swing.tree.RowMapper;
import java.sql.PreparedStatement;
import javax.swing.tree.TreePath;
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



    public Collection<Game> searchGame(String name, Map<FilterCategory, List<String>> filters) {

        int filtersAmount = countFilters(filters);
        String[] parameters = new String[(filtersAmount * 2) + 1];
        parameters[0] = name;

        String tablesString = "SELECT power_up.games.name, avg_score, summary, power_up.platforms.name" +
                " FROM power_up.games" +
                " INNER JOIN power_up.game_platforms ON power_up.games.id = power_up.game_platforms.game_id" +
                " INNER JOIN power_up.platforms ON power_up.game_platforms.platform_id = power_up.platforms.id";
        String nameString = "WHERE LOWER(power_up.games.name) like %LOWER(?)%";
        String filtersString = "";


        //Joins with specific table if a filter of that table is needed
        int parameterCount = 1;
        for (FilterCategory filter : filters.keySet()) {
            // table "platforms" is already joined
            if (!filter.equals(FilterCategory.platform)) {
                tablesString += " " + createJoinSentence(filter);
            }
            filtersString += " AND ( ";
            List<String> values = filters.get(filter);
            int valuesCount = 0;
            for (String value : values) {
                if (valuesCount > 0) {
                    filtersString += " OR ";
                }
                filtersString += createFilterSentence(filter);
                parameters[parameterCount] = value;
                parameters[parameterCount + filtersAmount] = Integer.toString(values.size());
                parameterCount++;
                valuesCount++;
            }
            filtersString += " )";

        }
        String query = tablesString + " " + nameString + filtersString;
        List<Game> gameList = new ArrayList();
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



    private String createJoinSentence (FilterCategory filter) {

        boolean useCompany = filter.equals(FilterCategory.developer) || filter.equals(FilterCategory.platform);
        String filterName = filter.name();
        String entityTable = "power_up." + (useCompany ? "companies": English.plural(filterName));
        String relationTable = "power_up.game_" + English.plural(filterName);

        String sentence = "INNER JOIN " + relationTable + " ON power_up.games.id = " + relationTable + ".game_id";
        sentence += " INNER JOIN " + entityTable
                + " ON " + relationTable + "." + filterName + "_id = " + entityTable + ".id";
        return sentence;
    }

    private String createFilterSentence(FilterCategory filter) {
        boolean useCompany = filter.equals(FilterCategory.developer) || filter.equals(FilterCategory.platform);
        String entityTable = "power_up." + (useCompany ? "companies": English.plural(filter.name()));

        return "LOWER(" + entityTable + ".name) = LOWER(?)";
    }




    private int countFilters(Map<FilterCategory, List<String>> filters) {
        int count = 0;
        for (List<String> list : filters.values()) {
            count += list.size();
        }
        return count;
    }




}

