package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
//import org.springframework.jdbc.core.JdbcTemplate;


import javax.sql.DataSource;
import javax.swing.tree.RowMapper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public class GameJdbcDao implements GameDao {

    private JdbcTemplate jdbcTemplate;

    public GameJdbcDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Collection<Game> searchGame(String name, Collection<Filter> filters) {
        ArrayList<Game> gameList = new ArrayList();

        StringBuilder searchString = new StringBuilder("SELECT name, avg_score, summary FROM power_up.games, power_up.game_platforms, power_up.platforms");
        StringBuilder whereSentence = new StringBuilder(" WHERE power_up.games.name LIKE ? && power_up.game_platform.game_id = power_up.games.id &&" +
                                                        "power_up.game_platforms.platform_id = power_up.platforms.id");

        //Joins with specific table if a filter of that table is needed
        Boolean companyfilter = false;
        for (Filter.FilterCategory filterType : Filter.FilterCategory.values()) {
            Boolean hasFilter = false;
            for (Filter filter : filters) {
                if (filter.getType() == filterType && filterType != Filter.FilterCategory.PLATFORMS) {
                    hasFilter = true;
                }
            }
            if (hasFilter) {
                //Join on relationship table
                searchString.append(", power_up.game_" + filterType.name().toLowerCase());
                whereSentence.append("&& power_up.games.id = power_up.game_" + filterType.name().toLowerCase() + ".game_id");
                //Join on entity table;
                if (filterType != Filter.FilterCategory.DEVELOPERS && filterType != Filter.FilterCategory.PUBLISHERS) {
                    searchString.append(", power_up." + filterType.name().toLowerCase());
                    whereSentence.append("&& power_up.game_" + filterType.name().toLowerCase() + "." + filterType.name().substring(0,filterType.name().length()-2) +
                            "_id = power_up." + filterType.name().toLowerCase() + ".id");
                } else {
                    if (!companyfilter) {
                        companyfilter = true;
                        searchString.append(", power_up.companies");
                        whereSentence.append("&& power_up.game_" + filterType.name().toLowerCase() + "." + filterType.name().substring(0,filterType.name().length()-2) +
                                "_id = power_up.companies.id");
                    }
                }
            }
        }
        searchString.append(whereSentence);
            for (Filter filter : filters) {
                    //aca hay que meter el valor del filtro haciendo cosas raras
                    searchString.append(" && power_up." + filter.getType().name().toLowerCase() + ".name = ?" );
            }

        return null;
    }
}

