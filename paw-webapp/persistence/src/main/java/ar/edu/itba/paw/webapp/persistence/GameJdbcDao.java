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
        Object[] parameters = new Object[filters.size() + 1];
        parameters[0] = name;
        int i = 1;
//        StringBuilder searchString = new StringBuilder("SELECT power_up.games.name, avg_score, summary, power_up.platforms.name FROM power_up.games, power_up.game_platforms, power_up.platforms");
//        StringBuilder whereSentence = new StringBuilder(" WHERE power_up.games.name LIKE ? AND power_up.game_platforms.game_id = power_up.games.id AND " +
//                "power_up.game_platforms.platform_id = power_up.platforms.id");

        StringBuilder searchString = new StringBuilder("SELECT power_up.games.name, avg_score, summary, power_up.consoles.name FROM power_up.games, power_up.game_consoles, power_up.consoles");
        StringBuilder whereSentence = new StringBuilder(" WHERE power_up.games.name LIKE ? AND power_up.game_consoles.game_id = power_up.games.id AND " +
                "power_up.game_consoles.platform_id = power_up.consoles.id");

        //Joins with specific table if a filter of that table is needed
        for (Filter filter : filters) {
            //Join on relationship table
            searchString.append(", power_up.game_").append(filter.getType().name()).append(" AS  game_").append(filter.getType().name()).append("_").append(filter.getName());
            whereSentence.append(" AND power_up.games.id = game_").append(filter.getType().name()).append("_").append(filter.getName()).append(".game_id");
            //Join on entity table;
            if ((filter.getType() != Filter.FilterCategory.DEVELOPERS) && (filter.getType() != Filter.FilterCategory.PUBLISHERS)) {
                searchString.append(", power_up.").append(filter.getType().name()).append(" AS ").append(filter.getType().name()).append("_").append(filter.getName());
                whereSentence.append(" AND game_").append(filter.getType().name()).append("_").append(filter.getName()).append(".").append(filter.getType().name().substring(0, filter.getType().name().length() - 1)).append("_id = ").append(filter.getType().name()).append("_").append(filter.getName()).append(".id");
                whereSentence.append(" AND ").append(filter.getType().name()).append("_").append(filter.getName()).append(".name = ?");
                parameters[i] = filter.getName();
                i++;
            } else {
                searchString.append(", power_up.companies AS companies_").append(filter.getType()).append("_").append(filter.getName());
                whereSentence.append(" AND power_up.game_").append(filter.getType()).append("_").append(filter.getName()).append(".").append(filter.getType().name().substring(0, filter.getType().name().length() - 1)).append("_id = companies_").append(filter.getName()).append(".id");
                whereSentence.append(" AND companies_").append(filter.getName()).append(".name = ?");
                parameters[i] = filter.getName();
                i++;
            }


        }

        searchString.append(whereSentence);

        System.out.print(searchString.toString());

        jdbcTemplate.query(searchString.toString(), parameters, new RowCallbackHandler() {

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

