package ar.edu.itba.paw.webapp.exceptions;

import ar.edu.itba.paw.webapp.model.Game;

/**
 * Created by juan_ on 06-Nov-16.
 */
public class NoSuchGameException extends NoSuchEntityException {
    public NoSuchGameException() {
        super("No such game");
    }

    public NoSuchGameException(long gameId) {
        super(Game.class, gameId);
    }

    public NoSuchGameException(String title) {
        super("No Game with title " + title);
    }
}
