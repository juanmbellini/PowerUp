package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Stores basic information about a game as well as its reviews and ratings.
 * This class communicates with the database adding, removing and modifying information.
 */
@Entity
@Table(name = "games")
public class Game {

    final static private double INITIAL_AVG_SCORE = 0.0;
    final private static String CLOUDINARY_URL_FORMAT = "https://res.cloudinary.com/igdb/image/upload/t_%s_2x/%s.jpg";
    final private static String DEFAULT_COVER_PICTURE_URL = "http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "games_seq", sequenceName = "games_gameid_seq", allocationSize = 1)
    private long id;

    @Column(length = 100)
    private String name;

    private String summary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="game_genres",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="genre_id", referencedColumnName="id"))
    private Collection<Genre> genres;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name="game_platforms",
                    joinColumns=@JoinColumn(name="game_id"))
    @MapKeyJoinColumn(name="platform_id")
    private Map<Platform, GamePlatformReleaseDate> platforms;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="game_publishers",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="publisher_id", referencedColumnName="id"))
    private Collection<Publisher> publishers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="game_developers",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="developer_id", referencedColumnName="id"))
    private Collection<Developer> developers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="game_keywords",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="keyword_id", referencedColumnName="id"))
    private Collection<Keyword> keywords;

    @ElementCollection
    @CollectionTable(
            name = "reviews",
            joinColumns=@JoinColumn(name = "game_Id")
    )
    private Collection<Review> reviews;

    @ElementCollection
    @CollectionTable(
            name = "game_scores",
            joinColumns=@JoinColumn(name = "game_id")
    )
    @MapKeyColumn (name="user_id")
    @Column(name="score")
    private Map<Long, Integer> scores = new HashMap<>();

    @Column(name="avg_score")
    private double avgScore;

    @Column(name="release")
    private LocalDate releaseDate;

    @Column(name="cover_picture_cloudinary_id")
    private String coverPictureId;

    @ElementCollection
    @CollectionTable(
                    name = "game_pictures",
                    joinColumns=@JoinColumn(name = "game_id", nullable = false))
    @Column(name="cloudinary_id", nullable = false)
    private Set<String> pictureIds;

    @Transient
    private Set<String> pictureUrls;


    /**
     * Default constructor used by Builder and Hibernate.
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
//        reviews = new HashSet<>();
        avgScore = INITIAL_AVG_SCORE;
        releaseDate = LocalDate.now();
        coverPictureId = null;
        pictureIds = new LinkedHashSet<>();
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

    public Collection<Genre> getGenres() {
        return cloneCollection(genres);
    }

    public Map<Platform, GamePlatformReleaseDate> getPlatforms() {
        HashMap<Platform, GamePlatformReleaseDate> newPlatformMap = new HashMap<Platform, GamePlatformReleaseDate>();

        for (Platform key : platforms.keySet()) {
            newPlatformMap.put(key, platforms.get(key)); //TODO check clone for LocalDate.
        }
        return newPlatformMap;
    }

    public Collection<Publisher> getPublishers() {
        return cloneCollection(publishers);
    }

    public Collection<Developer> getDevelopers() {
        return cloneCollection(developers);
    }

    public Collection<Keyword> getKeywords() {
        return cloneCollection(keywords);
    }

//    public Collection<Review> getReviews() {
//        return cloneCollection(reviews);
//    }//TODO add review table in db

    public double getAvgScore() {
        return avgScore;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * Gets this game's picture URLs.
     *
     * @return An <b>unmodifiable</b> set containing this game's picture URLs.
     * @see #addPicture(String)
     */
    public Set<String> getPictureUrls() {
        return Collections.unmodifiableSet(pictureUrls);
    }

    /**
     * Returns the URL of this game's cover picture, or a default picture if none is set.
     *
     * @return The cover picture URL.
     */
    public String getCoverPictureUrl() {
        return coverPictureId == null ? DEFAULT_COVER_PICTURE_URL : buildCloudinaryURL(coverPictureId);
    }

    // Setters
    //TODO remove setters, the builder has access to these fields and these shouldn't be changed once built
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

    private void setCoverPictureId(String cloudinaryId) {
        if (cloudinaryId != null && !cloudinaryId.equals("")) {
            coverPictureId = cloudinaryId;
        }
    }

    // Adders
    //TODO also consider removing these, or at least those that we're sure won't change after the game is built
    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public void addPlatform(Platform platform, GamePlatformReleaseDate date) {
        platforms.put(platform, date);
    }

    public void addPublisher(Publisher publisher) {
        publishers.add(publisher);
    }

    public void addDeveloper(Developer developer) {
        developers.add(developer);
    }

    public void addKeyword(Keyword keyword) {
        keywords.add(keyword);
    }

//    public void addReview(Review review) {
//        reviews.add(review);
//    }

    /**
     * Adds a picture to this game, populating both its picture IDs collection and its picture URLs collection.
     *
     * @param cloudinaryId The Cloudinary ID of the picture to add.
     */
    public void addPicture(String cloudinaryId) {
        if (cloudinaryId != null) {
            pictureIds.add(cloudinaryId);
            pictureUrls.add(buildCloudinaryURL(cloudinaryId));
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

    /**
     * @deprecated There's no use for this method, and it's probably slower than its alternatives. Instead, use either
     * <p>{@code new MyCollection(originalCollection)} which already adds all elements from the original collection</p>
     * or <p>{@link Collections#unmodifiableCollection(Collection)}</p>
     * and its variants as appropriate.
     */
    private <T> List<T> cloneCollection(Collection<T> original) {
        List<T> list = new ArrayList<>();
        list.addAll(original);
        return list;
    }

    /**
     * Gets a full Cloudinary picture URL given a cloudinary ID.
     *
     * @param cloudinaryId The cloudinary ID.
     * @return The picture URL.
     */
    private static String buildCloudinaryURL(String cloudinaryId) {
        return String.format(CLOUDINARY_URL_FORMAT, "cover_big", cloudinaryId);
    }

    /**
     * Gets an inverse mapping of {@link User#getScoredGames()}, where each user ID
     * is mapped to the score that said user gave this game.
     *
     * @return The scores map.
     */
    public Map<Long, Integer> getScores() {
        return scores;
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
            game.setCoverPictureId(cloudinaryId);
            startedBuilding = true;
            return this;
        }

        public GameBuilder setReleaseDate(LocalDate releaseDate) {
            checkBuilt();
            game.setReleaseDate(releaseDate);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addGenre(Genre genre) {
            checkBuilt();
            game.addGenre(genre);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addPlatform(Platform platform, GamePlatformReleaseDate date) {
            checkBuilt();
            game.addPlatform(platform, date);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addPublisher(Publisher publisher) {
            checkBuilt();
            game.addPublisher(publisher);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addDeveloper(Developer developer) {
            checkBuilt();
            game.addDeveloper(developer);
            startedBuilding = true;
            return this;
        }

        public GameBuilder addKeyword(Keyword keyword) {
            checkBuilt();
            game.addKeyword(keyword);
            startedBuilding = true;
            return this;
        }

//        public GameBuilder addReview(Review review) {
//            checkBuilt();
//            game.addReview(review);
//            startedBuilding = true;
//            return this;
//        }

        public GameBuilder addPictureURL(String cloudinaryId) {
            checkBuilt();
            game.addPicture(cloudinaryId);
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
