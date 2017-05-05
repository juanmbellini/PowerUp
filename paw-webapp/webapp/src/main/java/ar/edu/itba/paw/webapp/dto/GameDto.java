package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.Company;
import ar.edu.itba.paw.webapp.model.Game;
import ar.edu.itba.paw.webapp.model.Genre;
import ar.edu.itba.paw.webapp.model.Keyword;
import org.hibernate.Hibernate;

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

    public static final int MAX_LENGTH_OF_SUMMARY_IN_LIST = 200;
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

    @XmlElement
    private Double avgScore;

    @XmlElement
    private String releaseDate; // TODO: check how to format output using LocalDate

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
        this.releaseDate = game.getReleaseDate() == null ? null : game.getReleaseDate().toString();
        this.coverPictureUrl = game.getCoverPictureUrl();

        this.genres = !Hibernate.isInitialized(game.getGenres()) ? null :
                game.getGenres().stream().map(Genre::getName).collect(Collectors.toList());
        this.platforms = !Hibernate.isInitialized(game.getPlatforms()) ? null :
                game.getPlatforms().entrySet().stream()
                        .map(each -> new PlatformWrapper(each.getKey().getName(), each.getValue().getReleaseDate()))
                        .collect(Collectors.toList());
        this.publishers = !Hibernate.isInitialized(game.getPublishers()) ? null :
                game.getPublishers().stream().map(Company::getName).collect(Collectors.toList());
        this.developers = !Hibernate.isInitialized(game.getDevelopers()) ? null :
                game.getDevelopers().stream().map(Company::getName).collect(Collectors.toList());
        this.keywords = !Hibernate.isInitialized(game.getKeywords()) ? null :
                game.getKeywords().stream().map(Keyword::getName).collect(Collectors.toList());

        this.pictureUrls = !Hibernate.isInitialized(game.getPictureIds()) ? null :
                game.getPictureUrls().stream().collect(Collectors.toList());
        this.videoUrls = !Hibernate.isInitialized(game.getVideos()) ? null :
                game.getVideos().keySet().stream().map(each -> "https://www.youtube.com/embed/" + each) // TODO: move url to model?
                        .collect(Collectors.toList());
    }


    /**
     * Returns a list of {@link GameDto} based on the given collection of {@link Game}.
     *
     * @param games The collection of {@link Game}
     * @return A list of {@link GameDto}.
     */
    public static List<GameDto> createList(Collection<Game> games) {
        return games.stream().map(game -> {
            GameDto gameDto = new GameDto(game);
            gameDto.summary = gameDto.summary == null ? null : gameDto.summary
                    .substring(0, Math.min(gameDto.getSummary().length(), MAX_LENGTH_OF_SUMMARY_IN_LIST)); // TODO: make it a bit more pro, cutting the string at the end of a word
            return gameDto;
        }).collect(Collectors.toList());
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

    public String getReleaseDate() {
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
    public static class PlatformWrapper {

        @XmlElement
        private String name;

        @XmlElement
        private String releaseDate; // TODO: check how to format output using LocalDate

        public PlatformWrapper() {
            // Default constructor
        }

        public PlatformWrapper(String name, LocalDate releaseDate) {
            this.name = name;
            this.releaseDate = releaseDate.toString();
        }


        public String getName() {
            return name;
        }

        public String getReleaseDate() {
            return releaseDate;
        }
    }
}
