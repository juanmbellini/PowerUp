package ar.edu.itba.paw.webapp.model;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game2 {

    final static public double INITIAL_AVG_SCORE = 0.0;
    final public static String CLOUDINARY_URL_FORMAT = "https://res.cloudinary.com/igdb/image/upload/t_%s_2x/%s.jpg";
    final public static String DEFAULT_COVER_PICTURE_URL = "http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="games_seq", sequenceName="games_gameid_seq",allocationSize=1)
    public long id;

    @Column(length = 100)
    public String name;

    public String summary;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="game_genres",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="genre_id", referencedColumnName="id"))
    public Collection<Genre> genres;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name="game_platforms", joinColumns=@JoinColumn(name="game_id"))
//    @MapKeyJoinColumn(name="platform_id")
//    public Map<Platform, GamePlatformRelationData> platforms;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="game_publishers",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="publisher_id", referencedColumnName="id"))
    public Collection<Publisher> publishers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="game_developers",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="developer_id", referencedColumnName="id"))
    public Collection<Developer> developers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="game_keywords",
            joinColumns=@JoinColumn(name="game_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="keyword_id", referencedColumnName="id"))
    public Collection<Keyword> keywords;

    @ElementCollection
    @CollectionTable(
            name = "game_pictures",
            joinColumns=@JoinColumn(name = "game_id", nullable = false))
    @Column(name="cloudinary_id", nullable = false)
    public Set<String> pictureUrls;

    @Column(name="avg_score")
    public double avgScore;

    @Column(name="release")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    public LocalDate releaseDate;

    @Column(name="cover_picture_cloudinary_id")
    public String coverPictureUrl;

    Game2() {}
}
