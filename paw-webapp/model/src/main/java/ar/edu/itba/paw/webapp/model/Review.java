package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Models a review made by a specific user for a specific game.
 */
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @SequenceGenerator(name = "reviews_seq", sequenceName = "reviews_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id",referencedColumnName = "id",nullable = false)
    private Game game;

    @Column(nullable = false)
    private String review;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, name = "story_score")
    private int storyScore;

    @Column(nullable = false, name = "graphics_score")
    private int graphicsScore;

    @Column(nullable = false, name = "audio_score")
    private int audioScore;

    @Column(nullable = false, name = "controls_score")
    private int controlsScore;

    @Column(nullable = false, name = "fun_score")
    private int funScore;

    /*package*/  Review() {
        //for hibernate
    }

    public Review(User user, Game game, String review, LocalDate date, int storyScore, int graphicsScore, int audioScore, int controlsScore, int funScore) {
        this.user = user;
        this.game = game;
        this.review = review;
        this.date = date;
        this.storyScore = storyScore;
        this.graphicsScore = graphicsScore;
        this.audioScore = audioScore;
        this.controlsScore = controlsScore;
        this.funScore = funScore;
    }

    public Review(User user, Game game, String review, int storyScore, int graphicsScore, int audioScore, int controlsScore, int funScore) {
        this(user, game, review, LocalDate.now(), storyScore, graphicsScore, audioScore, controlsScore, funScore);
    }

    public Review(User user, String review, LocalDate date) {
        this.user = user;
        this.review = review;
        this.date = date;
    }


    public double getRating() {
        return (double)(this.audioScore + this.controlsScore + this.funScore + this.graphicsScore + this.storyScore)/5;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getStoryScore() {
        return storyScore;
    }

    public void setStoryScore(int storyScore) {
        this.storyScore = storyScore;
    }

    public int getGraphicsScore() {
        return graphicsScore;
    }

    public void setGraphicsScore(int graphicsScore) {
        this.graphicsScore = graphicsScore;
    }

    public int getAudioScore() {
        return audioScore;
    }

    public void setAudioScore(int audioScore) {
        this.audioScore = audioScore;
    }

    public int getControlsScore() {
        return controlsScore;
    }

    public void setControlsScore(int controlsScore) {
        this.controlsScore = controlsScore;
    }

    public int getFunScore() {
        return funScore;
    }

    public void setFunScore(int funScore) {
        this.funScore = funScore;
    }

    public String getReview() {
        return review;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Transient
    public double getOverallScore() {
        return (storyScore + graphicsScore + audioScore + controlsScore + funScore) / 5.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        return id == review.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
