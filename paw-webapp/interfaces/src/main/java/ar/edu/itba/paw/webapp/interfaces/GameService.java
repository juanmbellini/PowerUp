package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Filter;
import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;

public interface GameService {

    //TODO change Object to Game, rebase with master branch
    Collection<Game> searchGame(String name, Collection<Filter> filters);
}
