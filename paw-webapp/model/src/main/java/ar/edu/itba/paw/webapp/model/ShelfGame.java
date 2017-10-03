package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;

/**
 * Class representing a relationship between a {@link Shelf} and a {@link Game}.
 * <p>
 * Created by Juan Marcos Bellini on 14/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Entity
@Table(name = "shelf_games",
        indexes = {@Index(name = "shelf_games_shelf_id_game_id_key",
                columnList = "shelf_id, game_id", unique = true)})
public class ShelfGame {

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
     */
    public ShelfGame(Game game, Shelf shelf) {
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