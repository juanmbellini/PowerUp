package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a relationship between a {@link Shelf} and a {@link Game}.
 */
@Entity
@Table(name = "shelf_games",
        indexes = {@Index(name = "shelf_games_shelf_id_game_id_key",
                columnList = "shelf_id, game_id", unique = true)})
public class ShelfGame implements ValidationExceptionThrower {

    @Id
    @SequenceGenerator(name = "shelves_games_seq", sequenceName = "shelf_games_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shelves_games_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "game_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = true,
            updatable = false)
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "shelf_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = true,
            updatable = false)
    private Shelf shelf;


    /* package */ ShelfGame() {
        // For Hibernate
    }

    /**
     * Constructor
     *
     * @param game  The game.
     * @param shelf The shelf.
     * @throws ValidationException If there are validation errors with the given params.
     */
    public ShelfGame(Game game, Shelf shelf) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(game, errors, ValueErrorConstants.MISSING_GAME);
        ValidationHelper.objectNotNull(shelf, errors, ValueErrorConstants.MISSING_SHELF);
        throwValidationException(errors);

        this.game = game;
        this.shelf = shelf;
    }

    /**
     * Id getter.
     *
     * @return The id.
     */
    public long getId() {
        return id;
    }

    /**
     * Game getter.
     *
     * @return The game.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Shelf getter.
     *
     * @return The shelf.
     */
    public Shelf getShelf() {
        return shelf;
    }


    /**
     * Equals based on the game and the shelf.
     *
     * @param o The object to be compared with.
     * @return {@code true} if they are the same, or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShelfGame)) return false;

        ShelfGame shelfGame = (ShelfGame) o;

        if (game == null) {
            return shelfGame.game == null
                    && (shelf == null ? shelfGame.shelf == null : shelf.equals(shelfGame.shelf));
        }
        return game.equals(shelfGame.game)
                && (shelf == null ? shelfGame.shelf == null : shelf.equals(shelfGame.shelf));


    }

    /**
     * Hashcode based on the game and the shelf.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        int result = game == null ? 0 : game.hashCode();
        result = 31 * result + (shelf == null ? 0 : shelf.hashCode());
        return result;
    }
}
