package ar.edu.itba.paw.webapp.model_wrappers;

import ar.edu.itba.paw.webapp.model.*;

import java.util.List;

/**
 * This class wraps a {@link Game} with a {@link List} of {@link Shelf} in which a given {@link User}
 * has saved the given {@link Game},
 * together with the {@link UserGameScore} and {@link PlayStatus} that might be set by the given {@link User}.
 */
public class GameWithUserShelvesWrapper {

    /**
     * The {@link User} being wrapped.
     */
    private final User user;

    /**
     * The {@link Game} being wrapped.
     */
    private final Game game;

    /**
     * The {@link List} of {@link Shelf} being wrapped.
     */
    private final List<Shelf> shelves;

    /**
     * The {@link UserGameScore} being wrapped.
     */
    private final Integer score;

    /**
     * The {@link PlayStatus} being wrapped.
     */
    private final PlayStatus status;

    /**
     * Constructor.
     *
     * @param user    The {@link User} to wrap.
     * @param game    The {@link Game} to wrap.
     * @param shelves The {@link List} to wrap.
     * @param score   The {@link UserGameScore} to wrap.
     * @param status  The {@link PlayStatus} to wrap.
     */
    public GameWithUserShelvesWrapper(User user, Game game, List<Shelf> shelves,
                                      Integer score, PlayStatus status) {
        this.user = user;
        this.game = game;
        this.shelves = shelves;
        this.score = score;
        this.status = status;
    }

    /**
     * @return The {@link User} being wrapped.
     */
    public User getUser() {
        return user;
    }

    /**
     * @return The {@link Game} being wrapped.
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return The {@link List} of {@link Shelf} being wrapped.
     */
    public List<Shelf> getShelves() {
        return shelves;
    }

    /**
     * @return The {@link UserGameScore} being wrapped.
     */
    public Integer getScore() {
        return score;
    }

    /**
     * @return The {@link PlayStatus} being wrapped.
     */
    public PlayStatus getStatus() {
        return status;
    }
}
