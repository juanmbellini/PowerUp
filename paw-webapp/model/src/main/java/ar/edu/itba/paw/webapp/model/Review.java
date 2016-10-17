package ar.edu.itba.paw.webapp.model;

import org.joda.time.DateTime;

/**
 * Models a review made by a specific user for a specific game.
 */
public class Review {

    private long id; //TODO set id
    private int rating;
    private User user;
    private String review;
    private DateTime date;

    public Review(int rating, User user, String review, DateTime date) {
        this.rating = rating;
        this.user = user;
        this.review = review;
        this.date = date;
    }

    public int getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }

    public User getUser() {
        return user;
    }

    public DateTime getDate() {
        return date;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDate(DateTime date) {
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
