package ar.edu.itba.paw.webapp.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private long id;
    private String username;
    private String email;
    private Map<Long, Integer> scoredGames = new HashMap<>();
    private Map<Long, PlayStatus> playedGames = new HashMap<>();

    /**
     * Creates a new user.
     *
     * @param id The user's id.
     * @param email The user's identifying email.
     * @param username The user's identifying username.
     */

    public User(long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
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

    public Map getScoredGames(){
        return scoredGames;
    }
}
