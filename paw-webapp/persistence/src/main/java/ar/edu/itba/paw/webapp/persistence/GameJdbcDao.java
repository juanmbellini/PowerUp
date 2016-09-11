package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
//import org.springframework.jdbc.core.JdbcTemplate;


import javax.swing.tree.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * Created by julian on 07/09/16.
 */

@Repository
public class GameJdbcDao implements GameDao {

//    @Autowired
//    JdbcTemplate jdbcTemplate;

    public Collection<Game> findByName(String name) {
        ArrayList<Game> gameList = new ArrayList();
        Game testGame = new Game();
        testGame.setName(name);

        gameList.add(testGame);

        return gameList;

/*
        for(Game game: jdbcTemplate.query(
                "SELECT name, avg_score FROM power_up.games WHERE name = ?", new Object[] { name },
                new RowMapper<Game>(){
                    public Game mapRow(ResultSet rs, int i) throws SQLException {

                        Game newGame = new Game();
                        newGame.setName(rs.getString(name));
                        return newGame;
                    }

        )){
            gameList.add(game);
        };
        //Old code: (rs, rowNum) -> new Game(rs.getString("first_name"), rs.getString("last_name"))
*/
    }


}

