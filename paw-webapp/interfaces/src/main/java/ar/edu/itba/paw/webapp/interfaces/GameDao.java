package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.FilterCategory;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GameDao {

    Collection<Game> searchGame(String name, Map<FilterCategory,List<String>> filters);
}
