package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;

public interface GameDao {

    Collection<Game> searchGame(String name, Collection<Filter> filters);
}
