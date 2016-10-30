package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="users_seq", sequenceName="users_userid_seq",allocationSize=1)
    private long id;

    @Column(length = 100, unique = true)
    private String username;

    //Max length accepted by the IETF
    @Column(length = 254, nullable = false, unique = true)
    private String email;

    //TODO Check if max length is 100
    @Column(name = "hashed_password" ,length = 100, nullable = false)
    private String hashedPassword;

    @ElementCollection
    @CollectionTable(
            name = "game_scores",
            joinColumns=@JoinColumn(name = "user_id")
    )
    @MapKeyColumn (name="game_id")
    @Column(name="score")
    private Map<Long, Integer> scoredGames = new HashMap<>();

    @ElementCollection
    @CollectionTable(
            name = "game_play_statuses",
            joinColumns=@JoinColumn(name = "user_id")
    )
    @MapKeyColumn (name="game_id")
    @Column(name="status")
    private Map<Long, PlayStatus> playedGames = new HashMap<>();

    @ElementCollection
    @CollectionTable(
            name = "user_authorities",
            joinColumns=@JoinColumn(name = "username", referencedColumnName = "username")
    )
    @Column(name="status")
    private Set<Authority> authorities = new HashSet<>();

    /*package*/  User() {
        //for hibernate
    }

    /**
     * Creates a new user.
     *
     * @param email The user's identifying email.
     * @param username The user's identifying username.
     * @param hashedPassword The user's hashed password.
     * @param authorities The user's authorities.
     */
    public User(String email, String username, String hashedPassword, Authority... authorities) {
        this.email = email;
        this.username = username;
        this.hashedPassword = hashedPassword;
        Collections.addAll(this.authorities, authorities);
    }

    /**
     * Creates a new user.
     *
     * @param email The user's identifying email.
     * @param username The user's identifying username.
     * @param hashedPassword The user's hashed password.
     * @param authorities The user's authorities.
     */
    public User(String email, String username, String hashedPassword, Collection<Authority> authorities) {
        this(email, username, hashedPassword, authorities.toArray(new Authority[0]));
    }

    /**
     * @return This user's username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return This user's email.
     */
    public String getEmail() {
        return email;
    }


    /**
     * @return This user's ID.
     */
    public long getId() {
        return id;
    }

    /**
     * @param gameId The ID of the game to check.
     * @return Whether this user has scored the game with ID {@code gameId}.
     */
    public boolean hasScoredGame(long gameId) {
        return scoredGames.containsKey(gameId);
    }

    /**
     * Gets the score that this user gave to a specified game.
     *
     * @param gameId The ID of the game for which to get score.
     * @return The score that this user gave to the game with ID {@code gameId}.
     * @throws IllegalArgumentException If this user hasn't scored the specified game.
     */
    public int getGameScore(long gameId) {
        if(!hasScoredGame(gameId)) {
            throw new IllegalArgumentException(username + " has not scored game with ID " + gameId);
        }
        return scoredGames.get(gameId);
    }

    /**
     * Sets or updates a score for a specified game, given by this user.
     *
     * @param gameId The ID of the game to score.
     * @param score The score, where 1 <= {@code score} <= 10.
     */
    public void scoreGame(long gameId, int score) {
        scoredGames.put(gameId, score);
    }

    /**
     * Sets the scored games for this user.
     * @param scoredGames The games that this user has scored, with their corresponding scores.
     */
    public void setScoredGames(Map<Long, Integer> scoredGames) {
        this.scoredGames.putAll(scoredGames);
    }

    /**
     * @param gameId The ID of the game to check.
     * @return Whether this user has registered a play status for the game with ID {@code gameId}.
     */
    public boolean hasPlayStatus(long gameId) {
        return playedGames.containsKey(gameId);
    }

    /**
     * Gets the play status that this user set for a specified game.
     *
     * @param gameId The ID of the game for which to get a play status.
     * @return The registered play status.
     * @throws IllegalArgumentException If this user hasn't set a play status for the game with ID {@code gameId}.
     */
    public PlayStatus getPlayStatus(long gameId) {
        if(!hasPlayStatus(gameId)) {
            throw new IllegalArgumentException(username + " has no play status for game with ID " + gameId);
        }
        return playedGames.get(gameId);
    }

    /**
     * Sets or updates a play status for a specified game.
     *
     * @param gameId The ID of the game for which to set or update status.
     * @param status The new status for the game.
     */
    public void setPlayStatus(long gameId, PlayStatus status) {
        playedGames.put(gameId, status);
    }

    /**
     * Sets or updates games' play status for this user.
     *
     * @param playedGames The games this user has registered a play status for, with their corresponding play status.
     */
    public void setPlayStatuses(Map<Long, PlayStatus> playedGames) {
        this.playedGames.putAll(playedGames);
    }

    /**
     * @return This user's hashed password.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Updates this user's hashed password.
     * @param hashedPassword The new hashed password. Can't be null.
     */
    public void setHashedPassword(String hashedPassword) {
        if(hashedPassword == null) {
            throw new IllegalArgumentException("Password can't be null");
        }
        this.hashedPassword = hashedPassword;
    }

    /**
     * @return This user's granted authorities.
     */
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    /**
     * Adds a new authority to this user, if not already present.
     * @param authority The new authority.
     */
    public void addAuthority(Authority authority) {
        authorities.add(authority);
    }

    /**
     * Adds new authorities to this user, if not already present.
     * @param authority The new authorities.
     */
    public void addAuthorities(Collection<Authority> authority) {
        authorities.addAll(authority);
    }

    /**
     * Sets this user's authorities, replacing any previously set authorities.
     * @param authorities The new authorities.
     */
    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities.clear();
        this.authorities.addAll(authorities);
    }

    /**
     * Get the map with all games status.
     *
     */
    public  Map<Long, PlayStatus> getPlayStatuses(){
        return playedGames;
    }

    public Map<Long, Integer> getScoredGames(){
        return scoredGames;
    }
}
