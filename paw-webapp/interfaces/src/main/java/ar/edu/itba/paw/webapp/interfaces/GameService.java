package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;

public interface GameService {

    List<Game> searchGames(String name, Collection<Filter> filters);
}
