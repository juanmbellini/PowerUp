package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;
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

    protected JdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }

    public Collection<Game> searchGame(String name, Collection<Filter> filters) {
        ArrayList<Game> gameList = new ArrayList();
        Object[] parameters = new Object[filters.size()+1];
        parameters[0] = name;
        int i = 1;
        StringBuilder searchString = new StringBuilder("SELECT power_up.games.name, avg_score, summary FROM power_up.games, power_up.game_platforms, power_up.platforms");
        StringBuilder whereSentence = new StringBuilder(" WHERE power_up.games.name LIKE ? AND power_up.game_platforms.game_id = power_up.games.id AND " +
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
                whereSentence.append(" AND power_up.games.id = power_up.game_" + filterType.name().toLowerCase() + ".game_id");
                //Join on entity table;
                if (filterType != Filter.FilterCategory.DEVELOPERS && filterType != Filter.FilterCategory.PUBLISHERS) {
                    searchString.append(", power_up." + filterType.name().toLowerCase());
                    whereSentence.append(" AND power_up.game_" + filterType.name().toLowerCase() + "." + filterType.name().substring(0, filterType.name().length() - 1) +
                            "_id = power_up." + filterType.name().toLowerCase() + ".id");
                } else {
                    if (!companyfilter) {
                        companyfilter = true;
                        searchString.append(", power_up.companies");
                        whereSentence.append(" AND power_up.game_" + filterType.name().toLowerCase() + "." + filterType.name().substring(0, filterType.name().length() - 1) +
                                "_id = power_up.companies.id");
                    }
                }
            }
        }
        searchString.append(whereSentence);
        for (Filter filter : filters) {
            //aca hay que meter el valor del filtro haciendo cosas raras
            searchString.append(" AND power_up." + filter.getType().name().toLowerCase() + ".name = ?");
            parameters[i] = filter.getName();
            i++;
        }
       System.out.print(searchString.toString());

       jdbcTemplate.query(searchString.toString(), parameters, new RowCallbackHandler(){

                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        Game newGame = new Game();
                        newGame.setName(rs.getString("name"));
                        newGame.setSummary(rs.getString("summary"));
                        gameList.add(newGame);
                    }
                }


        );

        return gameList;
    }
}

