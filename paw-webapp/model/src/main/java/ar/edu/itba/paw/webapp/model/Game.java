package ar.edu.itba.paw.webapp.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Stores basic information about the games as well as reviews and ratings.
 * This class communicates with the data base adding, removing and modifying information.
 */
public class Game {


    final static int INITIAL_RATING = 7;

    private long id;
    private String name;
    private String summary;
    private Collection<String> genres;
    private Collection<String> platforms;
    private Collection<String> publishers;
    private Collection<String> developers;
    private Collection<String> keywords;
    private Collection<Review> reviews;
    private int rating;






    public Game() {
        this(0, "", "");
    }

    public Game(long id, String name, String summary) {
        this(id, name, summary, INITIAL_RATING);
    }

    public Game(long id, String name, String summary, int rating) {

        if (!validRating(rating)) {
            throw new IllegalArgumentException("Rating must be a value between 0 and 10");
        }
        this.id = id;
        this.name = name;
        this.summary = summary;
        genres = new HashSet<>();
        platforms = new HashSet<>();
        publishers = new HashSet<>();
        developers = new HashSet<>();
        keywords = new HashSet<>();
        reviews = new HashSet<>();
        this.rating = rating;

    }


    // Getters
    public long getId() { return id; }
    public String getName() {
        return name;
    }
    public String getSummary() { return summary; }
    public Collection<String> getGenres() { return cloneCollection(genres); }
    public Collection<String> getPlatforms() { return cloneCollection(platforms); }
    public Collection<String> getPublishers() { return cloneCollection(publishers); }
    public Collection<String> getDevelopers() { return cloneCollection(developers); }
    public Collection<String> getKeywords() { return cloneCollection(keywords); }
    public Collection<Review> getReviews() { return cloneCollection(reviews); }
    public int getRating() { return rating; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setSummary(String summary) { this.summary = summary; }
    public void setRating(int rating) {
        if (!validRating(rating)) {
            throw new IllegalArgumentException("Rating must be a value between 0 and 10");
        }
        this.rating = rating;
    }

    // Adders
    public void addGenre(String genre) { genres.add(genre); }
    public void addPlatform(String platform) { platforms.add(platform); }
    public void addPublisher(String publisher) { publishers.add(publisher); }
    public void addDeveloper(String developer) { developers.add(developer); }
    public void addKeyword(String keyword) { keywords.add(keyword); }
    public void addReview(Review review) { reviews.add(review); }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return id == game.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


    
    private <T> List<T> cloneCollection(Collection<T> original) {
        List<T> list = new ArrayList<>();
        list.addAll(original);
        return list;
    }
    private boolean validRating(int rating) {
        return rating >= 0 && rating <= 10;
    }
}
