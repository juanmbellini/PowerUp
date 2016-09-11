package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
//import org.springframework.jdbc.core.JdbcTemplate;


import javax.sql.DataSource;
import javax.swing.tree.RowMapper;
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

    public Collection<Game> findByName(String name) {
        ArrayList<Game> gameList = new ArrayList();

       jdbcTemplate.query(
                "SELECT name, avg_score, summary FROM power_up.games WHERE name = ?", new Object[] { name }, new RowCallbackHandler(){

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

