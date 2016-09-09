package ar.edu.itba.paw.webapp.model;

import java.util.ArrayList;

/**
 * Stores basic information about the games as well as reviews and ratings.
 * This class communicates with the data base adding, removing and modifying information.
 */
public class Game {

    private ArrayList<Review> reviews = new ArrayList<>();  
    private int[] ratings = new int[10];
    private ArrayList<String> companies;
    private ArrayList<String> genres;
    private ArrayList<String> keywords;
    private ArrayList<String> platforms;
    private ArrayList<String> series;

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public int[] getRatings() {
        return ratings;
    }

    public void setRatings(int[] ratings) {
        this.ratings = ratings;
    }

    public ArrayList<String> getCompanies() {
        return companies;
    }

    public void setCompanies(ArrayList<String> companies) {
        this.companies = companies;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = platforms;
    }

    public ArrayList<String> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<String> series) {
        this.series = series;
    }

}
