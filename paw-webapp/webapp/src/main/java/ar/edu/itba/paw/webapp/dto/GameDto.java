package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.util.*;
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

//    private Collection<Genre> genres;
//
//    private Map<Platform, GamePlatformReleaseDate> platforms;
//
//    private Collection<Company> publishers;

//    private Collection<Company> developers;

//    private Collection<Keyword> keywords;

//    private Map<Long, Integer> scores = new HashMap<>();

    @XmlElement
    private Double avgScore;

    @XmlElement
    private LocalDate releaseDate;

    @XmlElement
    private String coverPictureId;

//    private Set<String> pictureIds;

//    private Set<String> pictureUrls;

//    private Map<String, String> videos;

    /* package */ GameDto() {
        // Default constructor
    }

    public GameDto(Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.summary = game.getSummary();
        this.avgScore = game.getAvgScore();
        this.releaseDate = game.getReleaseDate();
        this.releaseDate = game.getReleaseDate();
        this.coverPictureId = game.getCoverPictureUrl();
    }



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

    public String getCoverPictureId() {
        return coverPictureId;
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
}
