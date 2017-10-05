package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

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
public class UserGameScore implements ValidationExceptionThrower, ScoreChecker {

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
     * @throws ValidationException If the score is not valid.
     */
    public UserGameScore(Game game, User user, int score) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(game, errors, ValueErrorConstants.MISSING_GAME);
        ValidationHelper.objectNotNull(user, errors, ValueErrorConstants.MISSING_USER);
        checkScore(score, ValueErrorConstants.MISSING_GAME_SCORE,
                ValueErrorConstants.GAME_SCORE_BELOW_MIN, ValueErrorConstants.GAME_SCORE_ABOVE_MAX, errors);
        throwValidationException(errors);

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
     * Sets a new score.
     *
     * @param score The new score.
     * @throws ValidationException If the score is not valid.
     */
    public void setScore(int score) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        checkScore(score, ValueErrorConstants.MISSING_GAME_SCORE,
                ValueErrorConstants.GAME_SCORE_BELOW_MIN, ValueErrorConstants.GAME_SCORE_ABOVE_MAX, errors);
        throwValidationException(errors);

        this.score = score;
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
}
