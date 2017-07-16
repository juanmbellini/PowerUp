package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.UserGameStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
<<<<<<<c51f3d6671d5baec2b81c69ca232d0b27e7b1834
        =======
import java.io.IOException;
>>>>>>>add date to playstatus
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Juan Marcos Bellini on 17/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gameStatus")
public class UserGameStatusDto extends EntityDto {

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
    @JsonDeserialize(using = DtoHelper.PlayStatusEnumDeserializer.class)
    private PlayStatus status;

    @XmlElement
    private String date;


    public UserGameStatusDto() {
        // For Jax-RS
    }

    public UserGameStatusDto(UserGameStatus gameStatus) {
        super(gameStatus.getUser().getId());
        this.gameId = gameStatus.getGame().getId();
        this.gameName = gameStatus.getGame().getName();
        this.status = gameStatus.getPlayStatus();
        this.date = LocalDateTime.ofInstant(gameStatus.getDate().toInstant(), ZoneId.systemDefault()).toString();

    }


    public Long getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public PlayStatus getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    /**
     * Returns a list of {@link UserGameStatusDto} based on the given collection of {@link UserGameStatus}.
     *
     * @param statuses The collection of {@link UserGameStatus}
     * @return A list of {@link UserGameStatusDto}.
     */
    public static List<UserGameStatusDto> createList(Collection<UserGameStatus> statuses) {
        return statuses.stream().map(UserGameStatusDto::new).collect(Collectors.toList());
    }
}
