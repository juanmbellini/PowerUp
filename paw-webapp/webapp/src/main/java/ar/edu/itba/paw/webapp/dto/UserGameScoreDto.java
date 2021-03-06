package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.UserGameScore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gameStatus")
public class UserGameScoreDto extends EntityDto {

    // Will consider super class field id as the user id.
    @Override
    @JsonProperty(value = "userId")
    public Long getId() {
        return super.getId();
    }

    @XmlElement(required = true)
    private Long gameId;

    @XmlElement
    private String gameName;

    @XmlElement
    private String gameCoverPictureUrl;

    @XmlElement
    private String gameSummary;

    @XmlElement
    private String gameReleaseDate;

    @XmlElement
    private Integer score;


    public UserGameScoreDto() {
        // For Jax-RS
    }

    public UserGameScoreDto(UserGameScore gameStatus) {
        super(gameStatus.getUser().getId());
        this.gameId = gameStatus.getGame().getId();
        this.gameName = gameStatus.getGame().getName();
        this.score = gameStatus.getScore();
        this.gameCoverPictureUrl = gameStatus.getGame().getCoverPictureUrl();
        this.gameSummary = gameStatus.getGame().getSummary();
        this.gameReleaseDate = Optional.ofNullable(gameStatus.getGame().getReleaseDate())
                .map(LocalDate::toString)
                .orElse(null);

    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public Integer getScore() {
        return score;
    }

    public String getGameCoverPictureUrl() {
        return gameCoverPictureUrl;
    }

    public String getGameSummary() {
        return gameSummary;
    }

    public String getGameReleaseDate() {
        return gameReleaseDate;
    }

    /**
     * Returns a list of {@link UserGameScoreDto} based on the given collection of {@link UserGameScoreDto}.
     *
     * @param scores The collection of {@link UserGameScore}
     * @return A list of {@link UserGameScoreDto}.
     */
    public static List<UserGameScoreDto> createList(Collection<UserGameScore> scores) {
        return scores.stream().map(UserGameScoreDto::new).collect(Collectors.toList());
    }


}
