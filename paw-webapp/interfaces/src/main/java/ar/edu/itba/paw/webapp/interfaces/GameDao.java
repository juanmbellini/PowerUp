package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;

public interface GameDao {

    List<Game> searchGames(String name, Collection<Filter> filters);
}
