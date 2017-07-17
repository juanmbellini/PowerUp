package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.webapp.model.validation.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements Serializable, ValidationExceptionThrower {

    /**
     * Contains a {@link PasswordEncoder} used for hashing the password when creating a new user.
     */
    private final static PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();


    // ==== User stuff ===

    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private long id;

    @Column(length = 100, unique = true)
    private String username;

    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @Column(name = "hashed_password", length = 100, nullable = false)
    private String hashedPassword;

    @Column(name = "profile_picture")
    private byte[] profilePicture;

    @Column(name = "mime_type")
    private String profilePictureMimeType;


    // ==== Collections ===

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<UserGameScore> scoredGames;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true, cascade = CascadeType.ALL)
    private Collection<UserGameStatus> playedGames;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "follower", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<UserFollow> following;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followed", orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<UserFollow> followers;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username")
    )
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;


    // ======== Constructors ========

    /* package */  User() {
        // For Hibernate
        this.playedGames = new HashSet<>();
        this.scoredGames = new HashSet<>();
        this.following = new HashSet<>();
        this.followers = new HashSet<>();
        this.authorities = new HashSet<>();

    }

    /**
     * Creates a new user.
     *
     * @param username The username.
     * @param email    The user's email.
     * @param password The raw password (will be encoded using BCrypt)
     * @throws ValidationException If any value is wrong.
     */
    public User(String username, String email, String password) throws ValidationException {
        this();
        List<ValueError> errorList = new LinkedList<>();
        checkValues(username, email, password, errorList);

        this.username = username;
        this.email = email;
        this.hashedPassword = PASSWORD_ENCODER.encode(password);
    }


    // ======== Updaters ========

    /**
     * Changes the password
     *
     * @param newPassword The new password.
     * @throws ValidationException If the password is not valid.
     */
    public void changePassword(String newPassword) throws ValidationException {
        checkPassword(newPassword);
        this.hashedPassword = PASSWORD_ENCODER.encode(newPassword);
    }

    /**
     * Changes the profile picture.
     *
     * @param picture  The picture (represented as a byte array).
     * @param mimeType The picture mime type.
     * @throws ValidationException if the values are not valid.
     */
    public void changeProfilePicture(byte[] picture, String mimeType) throws ValidationException {
        checkProfilePicture(picture, mimeType);
        this.profilePicture = picture;
        this.profilePictureMimeType = mimeType;
    }


    /**
     * Changes the play status of the given {@link Game}, returning the previous status for the game.
     *
     * @param game       The game whose status must be changed.
     * @param playStatus The new status.
     * @throws ValidationException If any value is not valid.
     */
    public void setPlayStatus(Game game, PlayStatus playStatus) throws ValidationException {
        checkPlayStatusValues(game, playStatus); //
        UserGameStatus ugs = findGameStatus(game);
        if (ugs != null) {
            ugs.setPlayStatus(playStatus);
        } else {
            playedGames.add(new UserGameStatus(game, this, playStatus));
        }
    }

    /**
     * Removes the play status for the given {@link Game}.
     *
     * @param game The game.
     * @throws ValidationException If the game is not valid.
     */
    public UserGameStatus removePlayStatus(Game game) throws ValidationException {
        checkGame(game);
        UserGameStatus gameStatus = findGameStatus(game);
        if (gameStatus == null) {
            return null; // Do nothing if not set before.
        }
        this.playedGames.remove(gameStatus);
        return gameStatus;
    }

    /**
     * Scores the given {@link Game}, returning the previous score for the game.
     *
     * @param game  The game to be scored.
     * @param score The score for the game.
     * @throws ValidationException If any value is not valid.
     */
    public UserGameScore scoreGame(Game game, Integer score) throws ValidationException {
        checkScoreGameValues(game, score);
        UserGameScore ugs = findGameScore(game);
        if (ugs != null) {
            ugs.setScore(score);
        } else {
            scoredGames.add(new UserGameScore(game, this, score));
        }
        return ugs;
    }

    /**
     * Removes the score for the given {@link Game}.
     *
     * @param game The game.
     * @throws ValidationException If the game is not valid.
     */
    public UserGameScore unscoreGame(Game game) throws ValidationException {
        checkGame(game);
        UserGameScore gameScore = findGameScore(game);
        if (gameScore == null) {
            return null;
        }
        this.scoredGames.remove(gameScore);
        return gameScore;
    }


    /**
     * Adds a new authority to this user, if not already present.
     *
     * @param authority The new authority.
     * @return this (for method chaining).
     * @throws ValidationException if the authority is missing.
     */
    public User addAuthority(Authority authority) throws ValidationException {
        checkAuthority(authority);
        authorities.add(authority);
        return this;
    }

    /**
     * Removes the given {@code authority} from the user.
     *
     * @param authority The authority to be removed.
     * @throws ValidationException if the authority is missing.
     */
    public void removeAuthority(Authority authority) {
        checkAuthority(authority);
        authorities.remove(authority);
    }


    // ======== Getters ========

    /**
     * Id getter.
     *
     * @return The id.
     */
    public long getId() {
        return id;
    }

    /**
     * Username getter.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Email getter.
     *
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Hashed password getter.
     *
     * @return The hashed password.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Profile picture getter
     *
     * @return The profile picture.
     */
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * Profile picture mime type getter.
     *
     * @return The profile picture mime type.
     */
    public String getProfilePictureMimeType() {
        return profilePictureMimeType;
    }


    /**
     * Played games getter.
     *
     * @return A map with all game statuses.
     */
    public Map<Long, PlayStatus> getPlayedGames() {
        return playedGames.stream()
                .collect(Collectors.toMap(gameStatus -> gameStatus.getGame().getId(), UserGameStatus::getPlayStatus));
    }

    /**
     * Scored games getter.
     *
     * @return The scored games.
     */
    public Map<Long, Integer> getScoredGames() {
        return scoredGames.stream()
                .collect(Collectors.toMap(gameScore -> gameScore.getGame().getId(), UserGameScore::getScore));
    }

    /**
     * @return This user's granted authorities.
     */
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * Following count getter.
     *
     * @return The amount of {@link User} being followed by {@code this} user.
     */
    public long getFollowingCount() {
        return following.size();
    }

    /**
     * Followers count getter.
     *
     * @return The amount of {@link User} following {@code this} user.
     */
    public long getFollowersCount() {
        return followers.size();
    }

    /**
     * Checks whether the user has a profile picture set.
     *
     * @return {@code true} if it has a profile picture, or {@code false} otherwise.
     */
    public boolean hasProfilePicture() {
        return profilePicture != null && profilePicture.length > 0;
    }

    /**
     * Checks if the given {@code rawPassword} is correct.
     *
     * @param rawPassword The password to be checked.
     * @return {@code true} if the given {@code rawPassword} is correct, or {@code false} otherwise.
     */
    public boolean matchesPassword(String rawPassword) {
        return PASSWORD_ENCODER.matches(rawPassword, hashedPassword);
    }


    // ======== Equals and hashcode ========

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

        User user = (User) o;

        return id == user.id;
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


    // ======== Helper methods ========

    // ==== Finders ====

    /**
     * Returns the association object for the given {@code game}, if this user has played it,
     * or {@code null} otherwise.
     *
     * @param game The {@link Game}
     * @return The association object for the given {@code game}, if this user has played it,
     * or {@code null} otherwise.
     */
    private UserGameStatus findGameStatus(Game game) {
        List<UserGameStatus> list = playedGames.stream()
                .filter(each -> each.getGame().equals(game) && each.getUser().equals(this))
                .collect(Collectors.toList());
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Returns the association object for the given {@code game}, if this user has scored it,
     * or {@code null} otherwise.
     *
     * @param game The {@link Game}
     * @return The association object for the given {@code game}, if this user has scored it,
     * or {@code null} otherwise.
     */
    private UserGameScore findGameScore(Game game) {
        List<UserGameScore> list = scoredGames.stream()
                .filter(each -> each.getGame().equals(game) && each.getUser().equals(this))
                .collect(Collectors.toList());
        return list.isEmpty() ? null : list.get(0);
    }


    // ==== Checkers ====


    /**
     * Checks the given values, throwing a {@link ValidationException} if any is wrong.
     *
     * @param username  The username be checked.
     * @param email     The email to be checked.
     * @param password  The password to be checked.
     * @param errorList A list containing possible detected errors before calling this method.
     * @throws ValidationException If any value is wrong.
     */
    private void checkValues(String username, String email, String password, List<ValueError> errorList) throws ValidationException {
        errorList = errorList == null ? new LinkedList<>() : errorList;
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(username, NumericConstants.USERNAME_MIN_LENGTH,
                NumericConstants.USERNAME_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_USERNAME,
                ValueErrorConstants.USERNAME_TOO_SHORT, ValueErrorConstants.USERNAME_TOO_LONG);
        ValidationHelper.checkEmailNotNullAndValid(email, NumericConstants.EMAIL_MIN_LENGTH,
                NumericConstants.EMAIL_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_E_MAIL,
                ValueErrorConstants.E_MAIL_TOO_SHORT, ValueErrorConstants.E_MAIL_TOO_LONG,
                ValueErrorConstants.INVALID_E_MAIL);
        checkPassword(password, errorList);
        throwValidationException(errorList);
    }

    /**
     * Checks the given {@code password}.
     *
     * @param password  The password to be checked.
     * @param errorList A list containing possible detected errors before calling this method.
     */
    private void checkPassword(String password, List<ValueError> errorList) {
        ValidationHelper.stringNotNullAndLengthBetweenTwoValues(password, NumericConstants.PASSWORD_MIN_LENGTH,
                NumericConstants.PASSWORD_MAX_LENGTH, errorList, ValueErrorConstants.MISSING_PASSWORD,
                ValueErrorConstants.PASSWORD_TOO_SHORT, ValueErrorConstants.PASSWORD_TOO_LONG);
    }

    /**
     * Checks the given {@code password}.
     *
     * @param password The password to be checked.
     * @throws ValidationException If it's not a valid password.
     */
    private void checkPassword(String password) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        checkPassword(password, errorList);
        throwValidationException(errorList);
    }

    /**
     * Checks the given {@link Authority}
     *
     * @param authority The authority to be checked.
     * @throws ValidationException If the {@link Authority} is not valid.
     */
    private void checkAuthority(Authority authority) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(authority, errorList, ValueErrorConstants.MISSING_AUTHORITY);
        throwValidationException(errorList);
    }


    /**
     * Checks the given game.
     *
     * @param game The game to be checked.
     * @throws ValidationException If the game is wrong.
     */
    private void checkGame(Game game) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(game, errorList, ValueErrorConstants.MISSING_GAME);
        throwValidationException(errorList);
    }


    /**
     * Checks the given values for play status.
     *
     * @param game       The game to be checked.
     * @param playStatus The play status to be checked.
     * @throws ValidationException If any value is wrong.
     */
    private void checkPlayStatusValues(Game game, PlayStatus playStatus) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(game, errorList, ValueErrorConstants.MISSING_GAME);
//        if (errorList.size() == 0 && findGameStatus(game) != null) {
//            // Checks if the game is not null (errorList.size() == 0) and that status is not set yet for the game.
//            errorList.add(ValueErrorConstants.PLAY_STATUS_ALREADY_SET);
//        }
        ValidationHelper.objectNotNull(playStatus, errorList, ValueErrorConstants.MISSING_PLAY_STATUS);
        throwValidationException(errorList);
    }

    /**
     * Checks the given values for score game.
     *
     * @param game  The game to be checked.
     * @param score The score to be checked
     * @throws ValidationException If any value is wrong.
     */
    private void checkScoreGameValues(Game game, Integer score) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        ValidationHelper.objectNotNull(game, errorList, ValueErrorConstants.MISSING_GAME);
        ValidationHelper.intNotNullAndLengthBetweenTwoValues(score, NumericConstants.MIN_SCORE,
                NumericConstants.MAX_SCORE, errorList, ValueErrorConstants.MISSING_SCORE,
                ValueErrorConstants.SCORE_BELOW_MIN, ValueErrorConstants.SCORE_ABOVE_MAX);
        throwValidationException(errorList);
    }

    /**
     * Checks the given values for profile picture.
     *
     * @param profilePicture The profile picture to be checked.
     * @param mimeType       The mime type to be checked.
     * @throws ValidationException If values are not valid.
     */
    private void checkProfilePicture(byte[] profilePicture, String mimeType) throws ValidationException {
        List<ValueError> errorList = new LinkedList<>();
        if (profilePicture == null) {
            // If mimeType is not null, then what is missing is the profile picture.
            ValidationHelper.objectNull(mimeType, errorList, ValueErrorConstants.MISSING_PICTURE);
        } else {
            ValidationHelper.objectNotNull(mimeType, errorList, ValueErrorConstants.MISSING_MIME_TYPE);
        }

        ValidationHelper.arrayNullOrLengthBetweenTwoNumbers(profilePicture,
                NumericConstants.PROFILE_PICTURE_MIN_SIZE, NumericConstants.PROFILE_PICTURE_MAX_SIZE,
                errorList, ValueErrorConstants.PICTURE_TOO_SMALL, ValueErrorConstants.PICTURE_TOO_BIG);

        ValidationHelper.stringNullOrLengthBetweenTwoValues(mimeType, NumericConstants.MIME_TYPE_MIN_LENGTH,
                NumericConstants.MIME_TYPE_MAX_LENGTH, errorList, ValueErrorConstants.MIME_TYPE_TOO_SHORT,
                ValueErrorConstants.MIME_TYPE_TOO_LONG);

        throwValidationException(errorList);
    }


}
