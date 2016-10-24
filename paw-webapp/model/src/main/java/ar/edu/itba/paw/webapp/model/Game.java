package ar.edu.itba.paw.webapp.model;

import org.joda.time.LocalDate;

import java.util.*;

/**
 * Stores basic information about a game as well as its reviews and ratings.
 * This class communicates with the database adding, removing and modifying information.
 */
public class Game {

    final static private double INITIAL_AVG_SCORE = 0.0;
    final private static String CLOUDINARY_URL_FORMAT = "https://res.cloudinary.com/igdb/image/upload/t_%s_2x/%s.jpg";
    final private static String DEFAULT_COVER_PICTURE_URL = "http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png";

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


    /**
     * Private default constructor used by Builder
     */
    private Game() {
        id = 0;
        name = "";
        summary = "";
        genres = new HashSet<>();
        platforms = new HashMap<>();
        publishers = new HashSet<>();
        developers = new HashSet<>();
        keywords = new HashSet<>();
        reviews = new HashSet<>();
        avgScore = INITIAL_AVG_SCORE;
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
    private void setId(long id) {
        this.id = id;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setSummary(String summary) {
        this.summary = summary;
    }

    private void setAvgScore(double avg_score) {
        this.avgScore = avg_score;
    }

    private void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    private void setCoverPictureUrl(String cloudinaryId) {
        if (cloudinaryId != null && !cloudinaryId.equals("")) {
            coverPictureUrl = getPictureURL(cloudinaryId);
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
            pictureUrls.add(getPictureURL(cloudinaryId));

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
    private static String getPictureURL(String cloudinaryId) {
        return String.format(CLOUDINARY_URL_FORMAT, "cover_big", cloudinaryId);
    }


    public static class GameBuilder {

        // TODO: Make a better Builder Implementation [JMB]

        final private Game game = new Game();
        private boolean built = false;
        private boolean startedBuilding = false;

        private boolean idSet = false;
        private boolean nameSet = false;
        private boolean summarySet = false;


        /**
         * Builds the game that was configured.
         * Note: Once built, this builder won't be able to build any more.
         *
         * @return The game built.
         */
        public Game build() {
            if (!idSet || !nameSet || !summarySet) {
                throw new IllegalStateException();
            }
            checkBuilt();
            built = true;
            startedBuilding = false;
            return game;
        }


        public boolean startedBuilding() {
            return startedBuilding;
        }

        public long getBuildingGameId() {
            checkBuilt();
            if (!startedBuilding) {
                throw new IllegalStateException();
            }
            return game.getId();
        }


        // Required things
        public GameBuilder setId(long id) {
            checkBuilt();
            game.setId(id);
            idSet = true;
            startedBuilding = true;
            return this;
        }

        public GameBuilder setName(String name) {
            checkBuilt();
            game.setName(name);
            nameSet = true;
            startedBuilding = true;
            return this;
        }

        public GameBuilder setSummary(String summary) {
            checkBuilt();
            game.setSummary(summary);
            summarySet = true;
            startedBuilding = true;
            return this;
        }


        // Optional things
        public GameBuilder setAvgScore(double avgScore) {
            checkBuilt();
            game.setAvgScore(avgScore);
            startedBuilding = true;
            return this;
        }

        public GameBuilder setCoverPictureUrl(String cloudinaryId) {
            checkBuilt();
            game.setCoverPictureUrl(cloudinaryId);
            startedBuilding = true;
            return this;
        }

        public GameBuilder setReleaseDate(LocalDate releaseDate) {
            checkBuilt();
            game.setReleaseDate(releaseDate);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addGenre(String genre) {
            checkBuilt();
            game.addGenre(genre);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addPlatform(String platform, LocalDate date) {
            checkBuilt();
            game.addPlatform(platform, date);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addPublisher(String publisher) {
            checkBuilt();
            game.addPublisher(publisher);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addDeveloper(String developer) {
            checkBuilt();
            game.addDeveloper(developer);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addKeyword(String keyword) {
            checkBuilt();
            game.addKeyword(keyword);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addReview(Review review) {
            checkBuilt();
            game.addReview(review);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addPictureURL(String cloudinaryId) {
            checkBuilt();
            game.addPictureURL(cloudinaryId);
            startedBuilding = true;
            return this;
        }


        private void checkBuilt() {
            if (built) {
                throw new IllegalStateException();
            }
        }

    }

}