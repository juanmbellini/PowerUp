package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.Game;

import java.util.Collection;

public interface GameDao {

    Collection<Game> findByName(String name);
}
