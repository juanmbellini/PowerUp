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

    @Column(nullable = false)
    private int story_score;

    @Column(nullable = false)
    private int graphics_score;

    @Column(nullable = false)
    private int audio_score;

    @Column(nullable = false)
    private int controls_score;

    @Column(nullable = false)
    private int fun_score;

    /*package*/  Review() {
        //for hibernate
    }
    public Review(User user, String review, LocalDate date) {
        this.user = user;
        this.review = review;
        this.date = date;
    }


    public double getRating() {
        return (double)(this.audio_score + this.controls_score + this.fun_score + this.graphics_score + this.story_score)/5;
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

    public int getStory_score() {
        return story_score;
    }

    public void setStory_score(int story_score) {
        this.story_score = story_score;
    }

    public int getGraphics_score() {
        return graphics_score;
    }

    public void setGraphics_score(int graphics_score) {
        this.graphics_score = graphics_score;
    }

    public int getAudio_score() {
        return audio_score;
    }

    public void setAudio_score(int audio_score) {
        this.audio_score = audio_score;
    }

    public int getControls_score() {
        return controls_score;
    }

    public void setControls_score(int controls_score) {
        this.controls_score = controls_score;
    }

    public int getFun_score() {
        return fun_score;
    }

    public void setFun_score(int fun_score) {
        this.fun_score = fun_score;
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
