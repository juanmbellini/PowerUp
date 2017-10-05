package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing a relationship between a {@link User} and a {@link Game}, including a {@link PlayStatus}.
 * <p>
 * Created by Juan Marcos Bellini on 16/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@Entity
@Table(name = "game_play_statuses",
        indexes = {@Index(name = "game_play_statuses_user_id_game_id_key",
                columnList = "user_id, game_id", unique = true)})
public class UserGameStatus implements ValidationExceptionThrower {

    /**
     * The id.
     */
    @Id
    @SequenceGenerator(name = "game_play_statuses_seq", sequenceName = "game_play_statuses_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_play_statuses_seq")
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
     * The play status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PlayStatus playStatus;

    @UpdateTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar date;


    /* package */ UserGameStatus() {
        // For Hibernate
    }

    /**
     * Constructor
     *
     * @param game       The game.
     * @param user       The user.
     * @param playStatus The play status.
     * @throws ValidationException If the given {@code playStatus} is not valid.
     */
    public UserGameStatus(Game game, User user, PlayStatus playStatus) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(game, errors, ValueErrorConstants.MISSING_GAME);
        ValidationHelper.objectNotNull(user, errors, ValueErrorConstants.MISSING_USER);
        ValidationHelper.objectNotNull(playStatus, errors, ValueErrorConstants.MISSING_PLAY_STATUS);
        throwValidationException(errors);

        this.game = game;
        this.user = user;
        this.playStatus = playStatus;
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
     * Play status getter.
     *
     * @return The play status.
     */
    public PlayStatus getPlayStatus() {
        return playStatus;
    }

    /**
     * Date getter.
     *
     * @return The date.
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Sets a new play status.
     *
     * @param playStatus The new play status.
     * @throws ValidationException If the given {@code playStatus} is not valid.
     */
    public void setPlayStatus(PlayStatus playStatus) throws ValidationException {
        List<ValueError> errors = new LinkedList<>();
        ValidationHelper.objectNotNull(playStatus, errors, ValueErrorConstants.MISSING_PLAY_STATUS);
        throwValidationException(errors);

        this.playStatus = playStatus;
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
        if (!(o instanceof UserGameStatus)) return false;

        UserGameStatus gameStatus = (UserGameStatus) o;

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
