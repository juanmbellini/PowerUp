package ar.edu.itba.paw.webapp.model;

import java.util.ArrayList;

/**
 * Stores basic information about the games as well as reviews and ratings.
 * This class communicates with the data base adding, removing and modifying information.
 */
public class Game {

    private ArrayList<Review> reviews = new ArrayList<>();
    private int[] ratings = new int[10];
    private ArrayList<String> publishers;
    private ArrayList<String> developers;
    private ArrayList<String> genres;
    private ArrayList<String> keywords;
    private ArrayList<String> platforms;
    private String summary;
    private String name;
    public String getSummary() {return summary;}


    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public String getName() {
        return name;
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

    public ArrayList<String> getPublishers() { return publishers; }

    public void setPublishers(ArrayList<String> publishers) { this.publishers = publishers; }

    public ArrayList<String> getDevelopers() { return developers; }

    public void setDevelopers(ArrayList<String> developers) { this.developers = developers; }

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


    public void setSummary(String summary) {
        this.summary = summary;
    }
}
