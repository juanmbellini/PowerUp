package ar.edu.itba.paw.webapp.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * Stores basic information about a game as well as its reviews and ratings.
 * This class communicates with the database adding, removing and modifying information.
 */
public class Game {

    private ArrayList<Review> reviews = new ArrayList<>();
    private int[] ratings = new int[10];
    private ArrayList<String> publishers = new ArrayList<>();
    private ArrayList<String> developers = new ArrayList<>();
    private ArrayList<String> genres = new ArrayList<>();
    private ArrayList<String> keywords;
    private ArrayList<String> platforms = new ArrayList<>();
    private LocalDate release = new LocalDate();
    private String summary;
    private double avg_score;
    private int id;
    private String name;

    public String getSummary() {
        return summary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAvg_score() {
        return avg_score;
    }

    public void setAvg_score(double avg_score) {
        this.avg_score = avg_score;
    }

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

    public ArrayList<String> getPublishers() {
        return publishers;
    }

    public void setPublishers(ArrayList<String> publishers) {
        this.publishers = publishers;
    }

    public ArrayList<String> getDevelopers() {
        return developers;
    }

    public void setDevelopers(ArrayList<String> developers) {
        this.developers = developers;
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

    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        if (((Game) obj).getName().compareTo(this.getName()) != 0) {
            return false;
        }
        return true;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void addPlatform(String platformName) {
        if (!platforms.contains(platformName)) {
            this.platforms.add(platformName);
        }
    }

    public void addGenre(String genreName) {
        if (!genres.contains(genreName)) {
            this.genres.add(genreName);
        }
    }

    public void addDeveloper(String developerName) {
        if (!developers.contains(developerName)) {
            this.developers.add(developerName);
        }
    }

    public void addPublisher(String publisherName) {
        if (!publishers.contains(publisherName)) {
            this.publishers.add(publisherName);
        }
    }

    public LocalDate getRelease() {
        return release;
    }

    public void setRelease(LocalDate release) {
        this.release = release;
    }
}
