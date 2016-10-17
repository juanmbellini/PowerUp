package ar.edu.itba.paw.webapp.model;

import org.joda.time.LocalDate;

import java.util.*;

/**
 * Stores basic information about a game as well as its reviews and ratings.
 * This class communicates with the database adding, removing and modifying information.
 */
public class Game {

    final static private int INITIAL_RATING = 7;
    final static private double INITIAL_AVG_SCORE = 0.0;
    final private static String CLOUDINARY_URL_FORMAT = "https://res.cloudinary.com/igdb/image/upload/t_%s_2x/%s.jpg";
    //TODO store this locally or get a more reliable URL
    final private static String DEFAULT_COVER_PICTURE_URL = "https://4.bp.blogspot.com/-9wibpF5Phs0/VubnbJfiprI/AAAAAAAABYg/TVSE7O7-yGYr_gCoBlObBc6DRve90LoIw/s1600/image06.png";

    private long id;
    private String name;
    private String summary;
    private Collection<String> genres;
    private Map<String, LocalDate> platforms;
    private Collection<String> publishers;
    private Collection<String> developers;
    private Collection<String> keywords;
    private Collection<Review> reviews;
    private double avgScore;
    private LocalDate releaseDate;
    private String coverPictureUrl;
    private Set<String> pictureUrls;

    public Game() {
        this(0, "", "");
    }

    public Game(long id, String name, String summary) {
        this(id, name, summary, INITIAL_AVG_SCORE);
    }

    public Game(long id, String name, String summary, double avgScore) {

        this.id = id;
        this.name = name;
        this.summary = summary;
        genres = new HashSet<>();
        platforms = new HashMap<>();
        publishers = new HashSet<>();
        developers = new HashSet<>();
        keywords = new HashSet<>();
        reviews = new HashSet<>();
        this.avgScore = avgScore;
        releaseDate = new LocalDate();
        coverPictureUrl = DEFAULT_COVER_PICTURE_URL;
        pictureUrls = new LinkedHashSet<>();


    }


    // Getters
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public Collection<String> getGenres() {
        return cloneCollection(genres);
    }

    public Map<String, LocalDate> getPlatforms() {
        HashMap<String, LocalDate> newPlatformMap = new HashMap<String, LocalDate>();

        for (String key : platforms.keySet()) {
            newPlatformMap.put(key, platforms.get(key)); //TODO check clone for LocalDate.
        }
        return newPlatformMap;
    }

    public Collection<String> getPublishers() {
        return cloneCollection(publishers);
    }

    public Collection<String> getDevelopers() {
        return cloneCollection(developers);
    }

    public Collection<String> getKeywords() {
        return cloneCollection(keywords);
    }

    public Collection<Review> getReviews() {
        return cloneCollection(reviews);
    }//TODO add review table in db

    public double getAvgScore() {
        return avgScore;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Set<String> getPictureUrls() {
        return new HashSet<>(pictureUrls);
    }

    /**
     * Returns the url set in {@link #coverPictureUrl}.
     * <p>
     * In case that url is the {@link #DEFAULT_COVER_PICTURE_URL} and {@link #pictureUrls} set is not empty,
     * the first url of this set is returned.
     *
     * @return The cover picture URL.
     */
    public String getCoverPictureUrl() {

        if (coverPictureUrl.equals(DEFAULT_COVER_PICTURE_URL) && !pictureUrls.isEmpty()) {
            return pictureUrls.iterator().next();
        }
        return coverPictureUrl;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setAvgScore(double avg_score) {
        this.avgScore = avg_score;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setCoverPictureUrl(String cloudinaryId) {
        if (cloudinaryId != null) {
            coverPictureUrl = getPictureURL(cloudinaryId);
            pictureUrls.remove(coverPictureUrl);
        }
    }

    // Adders
    public void addGenre(String genre) {
        genres.add(genre);
    }

    public void addPlatform(String platform, LocalDate date) {
        platforms.put(platform, date);
    }

    public void addPublisher(String publisher) {
        publishers.add(publisher);
    }

    public void addDeveloper(String developer) {
        developers.add(developer);
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addPictureURL(String cloudinaryId) {
        if (cloudinaryId != null) {
            String pictureUrl = getPictureURL(cloudinaryId);
            if (coverPictureUrl != pictureUrl) {
                pictureUrls.add(pictureUrl);
            }
        }
    }


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

    /**
     * Gets a full cloudinary picture URL given a cloudinary ID.
     *
     * @param cloudinaryId The cloudinary ID.
     * @return The picture URL.
     */
    private String getPictureURL(String cloudinaryId) {
        return String.format(CLOUDINARY_URL_FORMAT, "cover_big", cloudinaryId);
    }
}
