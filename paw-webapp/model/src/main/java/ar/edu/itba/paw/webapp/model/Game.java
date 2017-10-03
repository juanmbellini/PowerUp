package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a game.
 */
@Entity
@Table(name = "games")
public class Game {

    final static private double INITIAL_AVG_SCORE = 0.0;
    final private static String CLOUDINARY_URL_FORMAT = "https://res.cloudinary.com/igdb/image/upload/t_%s_2x/%s.jpg";
    final public static String DEFAULT_COVER_PICTURE_URL = "http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "games_seq", sequenceName = "games_gameid_seq", allocationSize = 1)
    private long id;

    @Column(length = 100)
    private String name;

    private String summary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_genres",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Collection<Genre> genres;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "game_platforms",
            joinColumns = @JoinColumn(name = "game_id"))
    @MapKeyJoinColumn(name = "platform_id")
    private Map<Platform, GamePlatformReleaseDate> platforms;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_publishers",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id", referencedColumnName = "id"))
    private Collection<Company> publishers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_developers",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "developer_id", referencedColumnName = "id"))
    private Collection<Company> developers;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "game_keywords",
            joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id", referencedColumnName = "id"))
    private Collection<Keyword> keywords;

    @ElementCollection
    @CollectionTable(
            name = "game_scores",
            joinColumns = @JoinColumn(name = "game_id")
    )
    @MapKeyColumn(name = "user_id")
    @Column(name = "score")
    private Map<Long, Integer> scores = new HashMap<>();

    @Column(name = "avg_score")
    private double avgScore;

    @Column(name = "release")
    private LocalDate releaseDate;

    @Column(name = "cover_picture_cloudinary_id")
    private String coverPictureId;

    @ElementCollection
    @CollectionTable(
            name = "game_pictures",
            joinColumns = @JoinColumn(name = "game_id", nullable = false))
    @Column(name = "cloudinary_id", nullable = false)
    private Set<String> pictureIds;

    @Transient
    private Set<String> pictureUrls;

    @ElementCollection
    @CollectionTable(
            name = "game_videos",
            joinColumns = @JoinColumn(name = "game_id", nullable = false))
    @MapKeyColumn(name = "video_id")
    @Column(name = "name")
    private Map<String, String> videos;


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
        avgScore = INITIAL_AVG_SCORE;
        releaseDate = LocalDate.now();
        coverPictureId = null;
        pictureIds = new LinkedHashSet<>();
        pictureUrls = new LinkedHashSet<>();
        videos = new HashMap<>();

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
        return genres;
    }

    public Map<Platform, GamePlatformReleaseDate> getPlatforms() {
        return platforms;
    }

    public Collection<Company> getPublishers() {
        return publishers;
    }

    public Collection<Company> getDevelopers() {
        return developers;
    }

    public Collection<Keyword> getKeywords() {
        return keywords;
    }

    public double getAvgScore() {
        return avgScore;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    /**
     * Gets this game's picture URLs.
     *
     * @return An set containing this game's picture URLs.
     * @see #addPicture(String)
     */
    public Set<String> getPictureUrls() {
        return pictureIds.stream()
                .filter(id -> !id.equals(coverPictureId))
                .map(Game::buildCloudinaryURL)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // TODO: why linked hash set?
    }

    public Set<String> getPictureIds() {
        return pictureIds;
    }

    /**
     * Returns the URL of this game's cover picture, or a default picture if none is set.
     *
     * @return The cover picture URL.
     */
    public String getCoverPictureUrl() {
        return coverPictureId == null ? DEFAULT_COVER_PICTURE_URL : buildCloudinaryURL(coverPictureId);
    }

    public Map<String, String> getVideos() {
        return videos;
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

    public void setAvgScore(double avg_score) {
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

    public void addPublisher(Company publisher) {
        publishers.add(publisher);
    }

    public void addDeveloper(Company developer) {
        developers.add(developer);
    }

    public void addKeyword(Keyword keyword) {
        keywords.add(keyword);
    }


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

    public void addVideo(String videoId, String videoName) {
        if (videoId != null && !videoId.isEmpty() && videoName != null && !videoName.isEmpty()) {
            videos.put(videoId, videoName);
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

}
