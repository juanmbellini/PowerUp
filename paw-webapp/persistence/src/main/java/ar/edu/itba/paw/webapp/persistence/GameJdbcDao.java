package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Game;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by julian on 07/09/16.
 */

@Repository
public class GameJdbcDao implements GameDao {

    public Collection<Game> findByName(String name) {
        ArrayList<Game> gameList = new ArrayList<Game>();
        Game testGame = new Game();
        testGame.setName("test1");

        gameList.add(testGame);
        return gameList;
    }
}

