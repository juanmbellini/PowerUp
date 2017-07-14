package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;

/**
 * Class representing a relationship between a {@link User} and a {@link Game}, including a {@link PlayStatus}.
 * <p>
 * Created by Juan Marcos Bellini on 16/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Entity
@Table(name = "game_scores",
        indexes = {@Index(name = "game_scores_user_id_game_id_key",
                columnList = "user_id, game_id", unique = true)})
public class UserGameScore {

    /**
     * The id.
     */
    @Id
    @SequenceGenerator(name = "game_scores_seq", sequenceName = "game_scores_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_scores_seq")
    private long id;


    /**
     * The game.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "game_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = true,
            updatable = false)
    private Game game;

    /**
     * The user.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            columnDefinition = "integer",
            name = "user_id",
            referencedColumnName = "id",
            nullable = false,
            insertable = true,
            updatable = false)
    private User user;

    /**
     * The score.
     */
    @Column(name = "score")
    private int score;


    /* package */ UserGameScore() {
        // For Hibernate
    }

    /**
     * Constructor
     *
     * @param game  The game.
     * @param user  The user.
     * @param score The score.
     */
    public UserGameScore(Game game, User user, int score) {
        this.game = game;
        this.user = user;
        this.score = score;
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
     * User getter.
     *
     * @return The user.
     */
    public User getUser() {
        return user;
    }

    /**
     * Score getter.
     *
     * @return The score.
     */
    public int getScore() {
        return score;
    }


    /**
     * Equals based on the game and the user.
     *
     * @param o The object to be compared with.
     * @return {@code true} if they are the same, or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGameScore)) return false;

        UserGameScore gameStatus = (UserGameScore) o;

        if (game == null) {
            return gameStatus.game == null
                    && (user == null ? gameStatus.user == null : user.equals(gameStatus.user));
        }
        return game.equals(gameStatus.game)
                && (user == null ? gameStatus.user == null : user.equals(gameStatus.user));


    }

    /**
     * Hashcode based on the game and the user.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        int result = game == null ? 0 : game.hashCode();
        result = 31 * result + (user == null ? 0 : user.hashCode());
        return result;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
