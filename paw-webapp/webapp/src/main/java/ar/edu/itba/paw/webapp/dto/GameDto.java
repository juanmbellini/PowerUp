package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import ar.edu.itba.paw.webapp.model.Keyword;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 8/1/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlRootElement(name = "game")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameDto {

    @XmlElement(required = true)
    private Long id;

    @XmlElement
    private String name;

    @XmlElement
    private String summary;

    @XmlElement
    private List<String> genres;

    @XmlElement
    private List<PlatformWrapper> platforms;

    @XmlElement
    private List<String> publishers;

    @XmlElement
    private List<String> developers;

    @XmlElement
    private List<String> keywords;


//    private Map<Long, Integer> scores = new HashMap<>();

    @XmlElement
    private Double avgScore;

    @XmlElement
    private LocalDate releaseDate;

    @XmlElement
    private String coverPictureUrl;

    @XmlElement
    private List<String> pictureUrls;

    @XmlElement
    private List<String> videoUrls;




    public GameDto() {
        // Default constructor
    }

    public GameDto(Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.summary = game.getSummary();
        this.avgScore = game.getAvgScore();
        this.releaseDate = game.getReleaseDate();
        this.releaseDate = game.getReleaseDate();
        this.coverPictureUrl = game.getCoverPictureUrl();

        this.genres = game.getGenres().stream().map(Genre::getName).collect(Collectors.toList());
        this.platforms = game.getPlatforms().entrySet().stream()
                .map(each -> new PlatformWrapper(each.getKey().getName(), each.getValue().getReleaseDate()))
                .collect(Collectors.toList());
        this.publishers = game.getPublishers().stream().map(Company::getName).collect(Collectors.toList());
        this.developers = game.getDevelopers().stream().map(Company::getName).collect(Collectors.toList());
        this.keywords = game.getKeywords().stream().map(Keyword::getName).collect(Collectors.toList());

        this.pictureUrls = game.getPictureUrls().stream().collect(Collectors.toList());
        this.videoUrls = game.getVideos().keySet().stream()
                .map(each -> "https://www.youtube.com/embed/" + each)
                .collect(Collectors.toList());
    }

    
    /**
     * Returns a list of {@link GameDto} based on the given collection of {@link Game}.
     *
     * @param games The collection of {@link Game}
     * @return A list of {@link GameDto}.
     */
    public static List<GameDto> createList(Collection<Game> games) {
        return games.stream().map(GameDto::new).collect(Collectors.toList());
    }


    // Getters

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getSummary() {
        return summary;
    }
    public Double getAvgScore() {
        return avgScore;
    }
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    public String getCoverPictureUrl() {
        return coverPictureUrl;
    }
    public List<String> getGenres() {
        return genres;
    }
    public List<String> getPublishers() {
        return publishers;
    }
    public List<String> getDevelopers() {
        return developers;
    }
    public List<String> getKeywords() {
        return keywords;
    }
    public List<PlatformWrapper> getPlatforms() {
        return platforms;
    }
    public List<String> getPictureUrls() {
        return pictureUrls;
    }
    public List<String> getVideoUrls() {
        return videoUrls;
    }





    /**
     * This class wraps platform data (name and release date)
     */
    @XmlRootElement(name = "")
    @XmlAccessorType(XmlAccessType.FIELD)
    /* package */ static class PlatformWrapper {

        @XmlElement
        private String platform;

        @XmlElement
        private LocalDate releaseDate;

        public PlatformWrapper() {
            // Default constructor
        }

        public PlatformWrapper(String name, LocalDate releaseDate) {
            this.platform = name;
            this.releaseDate = releaseDate;
        }


        public String getPlatform() {
            return platform;
        }

        public LocalDate getReleaseDate() {
            return releaseDate;
        }
    }
}
