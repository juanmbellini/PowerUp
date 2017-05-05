package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Shelves are custom-named game lists created by users to organize their games. Users might create a Shelf for the
 * weekends, another one with their favorite games, and yet another one for the games they want to buy, etc.
 */
@Entity
@Table(name = "shelves")
public class Shelf implements ValidationExceptionThrower {

    @Id
    @SequenceGenerator(name = "shelves_seq", sequenceName = "shelves_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shelves_seq")
    private long id;

    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shelf", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<ShelfGame> games = new LinkedHashSet<>();

    @Column(name = "created_at")
    @CreationTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Access(value = AccessType.FIELD)
    private Calendar updatedAt;


    /* package */  Shelf() {
        // For Hibernate
        this.games = new HashSet<>();
    }

    public Shelf(String name, User creator) {
        this();
        final List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(creator, errorList, ValueErrorConstants.MISSING_USER);

        update(name, errorList);
        this.user = creator;
    }

    /**
     * Updates the shelf.
     *
     * @param name The new name of the shelf.
     * @throws ValidationException If any value is wrong.
     */
    public void update(String name) throws ValidationException {
        update(name, new LinkedList<>());
    }

    /**
     * Updates the shelf, receiving a list of detected errors before executing this method.
     *
     * @param name      The new name of the shelf.
     * @param errorList The list containing possible errors detected before executing the method.
     * @throws ValidationException If any value is wrong.
     */
    private void update(String name, List<ValueError> errorList) throws ValidationException {
        checkValues(name, errorList);
        this.name = name;
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
     * User getter.
     *
     * @return The user to which this shelf belongs to.
     */
    public User getUser() {
        return user;
    }

    /**
     * Name getter.
     *
     * @return The name of the shelf.
     */
    public String getName() {
        return name;
    }

    /**
     * Games getter.
     *
     * @return The list of games.
     */
    public Set<Game> getGames() {
        return games.stream().map(ShelfGame::getGame).collect(Collectors.toSet());
    }


    public Set<ShelfGame> getShelfGames() {
        return games;
    }


    /**
     * Created at getter.
     *
     * @return The moment this shelf was created.
     */
    public LocalDateTime getCreatedAt() {
        return LocalDateTime.ofInstant(createdAt.toInstant(), createdAt.getTimeZone().toZoneId());
    }

    /**
     * Updated at getter.
     *
     * @return The moment this shelf was updated.
     */
    public LocalDateTime getUpdatedAt() {
        return LocalDateTime.ofInstant(updatedAt.toInstant(), updatedAt.getTimeZone().toZoneId());
    }

    /**
     * Adds the given {@link Game} into this shelf.
     *
     * @param game The game to be added.
     */
    public void addGame(Game game) {
        checkGame(game);
        games.add(new ShelfGame(game, this));
    }

    /**
     * Removes the given {@link Game} from this shelf.
     *
     * @param game The game to be removed.
     */
    public ShelfGame removeGame(Game game) {
        checkGame(game);
        ShelfGame shelfGame = findShelfGame(game);
        if (shelfGame == null) {
            return null;
        }
        this.games.remove(shelfGame);
        return shelfGame;
    }


    /**
     * Removes all {@link Game}s from this shelf.
     */
    public void clear() {
        games.clear();
    }


    /**
     * Equals based on the id.
     *
     * @param o The object to be compared with.
     * @return {@code true} if they are the same, or {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        return id == shelf.id;
    }

    /**
     * Hashcode based on the id.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


    /**
     * Returns the association object for the given {@code game}, if this shelf contains it,
     * or {@code null} otherwise.
     *
     * @param game The {@link Game}
     * @return The association object for the given {@code game}, if this shelf contains,
     * or {@code null} otherwise.
     */
    private ShelfGame findShelfGame(Game game) {
        List<ShelfGame> list = games.stream()
                .filter(each -> each.getGame().equals(game) && each.getShelf().equals(this))
                .collect(Collectors.toList());
        return list.isEmpty() ? null : list.get(0);
    }


    /**
     * Checks the given values, throwing a {@link ValidationException} if any is wrong.
     *
     * @param name      The name to be checked.
     * @param errorList A list containing possible detected errors before calling this method.
     * @throws ValidationException If any value is wrong.
     */
    private void checkValues(String name, List<ValueError> errorList) throws ValidationException {
        errorList = errorList == null ? new LinkedList<>() : errorList;
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(name, NumericConstants.NAME_MIN_LENGTH,
                NumericConstants.NAME_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_NAME,
                ValueErrorConstants.NAME_TOO_SHORT, ValueErrorConstants.NAME_TOO_LONG);

        throwValidationException(errorList);
    }

    /**
     * Checks that the given {@code game} is valid.
     *
     * @param game The game to be checked.
     * @throws ValidationException If the game is not valid.
     */
    private void checkGame(Game game) throws ValidationException {
        if (game == null) {
            throwValidationException(Stream.of(ValueErrorConstants.MISSING_GAME).collect(Collectors.toList()));
        }
    }
}
