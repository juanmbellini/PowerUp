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

        PreparedStatement search = null;
        String searchString = "SELECT name, avg_score FROM power_up.games";

        //Joins with specific table if a filter of that table is needed
        Boolean companyfilter = false;
        for (Filter.FilterCategory filterType : Filter.FilterCategory.values()) {
            Boolean hasFilter = false;
            for (Filter filter : filters) {
                if (filter.getType() == filterType) {
                    hasFilter = true;
                }
            }
            if (hasFilter) {
                //Natural Join on relationship table
                searchString.concat(" NATURAL JOIN power_up.game_" + filterType.name().toLowerCase());
                //Natural Join on entity table;
                if (filterType != Filter.FilterCategory.DEVELOPERS && filterType != Filter.FilterCategory.PUBLISHERS) {
                    searchString.concat(" NATURAL JOIN power_up." + filterType.name().toLowerCase());
                } else {
                    if (!companyfilter) {
                        companyfilter = true;
                        searchString.concat(" NATURAL JOIN power_up.companies");
                    }
                }
            }
        }
        searchString.concat("WHERE power_up.games.name = ?");

        for (Filter.FilterCategory filterType : Filter.FilterCategory.values()) {
            Boolean flag;
            for (Filter filter : filters) {
                if (filter.getType() == filterType) {
                    //aca hay que meter el valor del filtro haciendo cosas raras
                    searchString.concat(" && power_up." + filterType.name().toLowerCase() + " = ?" );
                }
            }
        }


//        Game testGame = new Game();
//        testGame.setName(name);
//
//        gameList.add(testGame);
//
//        return gameList;

//        for (Game game : jdbcTemplate.query(
//                "SELECT name, avg_score FROM power_up.games WHERE name = ?", new Object[]{name},
//                new RowMapper<Game>() {
//                    public Game mapRow(ResultSet rs, int i) throws SQLException {
//
//                        Game newGame = new Game();
//                        newGame.setName(rs.getString(name));
//                        return newGame;
//                    }
//
//        ))
//
//                    {
//                        gameList.add(game);
//                    }
//
//                    ;
//                    //Old code: (rs, rowNum) -> new Game(rs.getString("first_name"), rs.getString("last_name"))
//                }
        return null;
    }
}

