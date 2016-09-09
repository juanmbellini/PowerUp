package ar.edu.itba.paw.webapp.persistence;

import ar.edu.itba.paw.webapp.interfaces.GameDao;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;

/**
 * Created by julian on 07/09/16.
 */
public class GameJdbcDao implements GameDao {

    public Collection<Game> findByName(String name) {
        return null;
    }
}
