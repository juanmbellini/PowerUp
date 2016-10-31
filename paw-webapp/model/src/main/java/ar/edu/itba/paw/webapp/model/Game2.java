package ar.edu.itba.paw.webapp.model;

import javax.persistence.*;

@Entity
@Table(name = "games")
public class Game2 {

    final static private double INITIAL_AVG_SCORE = 0.0;
    final private static String CLOUDINARY_URL_FORMAT = "https://res.cloudinary.com/igdb/image/upload/t_%s_2x/%s.jpg";
    final private static String DEFAULT_COVER_PICTURE_URL = "http://res.cloudinary.com/dtbyr26w9/image/upload/v1476797451/default-cover-picture.png";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="games_seq", sequenceName="games_gameid_seq",allocationSize=1)
    private long id;

    @Column(length = 100)
    private String name;

    private String summary;

    Game2() {}
}
