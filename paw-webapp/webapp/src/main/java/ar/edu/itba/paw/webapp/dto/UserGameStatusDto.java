package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.PlayStatus;
import ar.edu.itba.paw.webapp.model.UserGameStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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
    private String username;

    @XmlElement
    private String profilePictureUrl;

    @XmlElement
    private String userUrl;

    @XmlElement
    private String gameCoverPictureUrl;

    @XmlElement
    @JsonDeserialize(using = DtoHelper.PlayStatusEnumDeserializer.class)
    private PlayStatus status;

    @XmlElement
    private String date;


    public UserGameStatusDto() {
        // For Jax-RS
    }

    public UserGameStatusDto(UserGameStatus gameStatus, UriBuilder baseUri) {
        super(gameStatus.getUser().getId());
        this.gameId = gameStatus.getGame().getId();
        this.gameName = gameStatus.getGame().getName();
        this.status = gameStatus.getPlayStatus();
        this.date = LocalDateTime.ofInstant(gameStatus.getDate().toInstant(), ZoneId.systemDefault()).toString();
        this.username = gameStatus.getUser().getUsername();
        if (gameStatus.getUser().hasProfilePicture()) {
            this.profilePictureUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(gameStatus.getUser().getId()))
                    .path(UserJerseyController.PICTURE_END_POINT)
                    .build().toString();
        } else {
            this.profilePictureUrl = null;
        }
        this.userUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(Long.toString(gameStatus.getUser().getId()))
                .build().toString();
        this.gameCoverPictureUrl = gameStatus.getGame().getCoverPictureUrl();

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

    public String getUsername() {
        return username;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public String getGameCoverPictureUrl() {
        return gameCoverPictureUrl;
    }

    /**
     * Returns a list of {@link UserGameStatusDto} based on the given collection of {@link UserGameStatus}.
     *
     * @param statuses The collection of {@link UserGameStatus}
     * @return A list of {@link UserGameStatusDto}.
     */
    public static List<UserGameStatusDto> createList(Collection<UserGameStatus> statuses, UriBuilder uriBuilder) {
        return statuses.stream().map(status -> new UserGameStatusDto(status, uriBuilder)).collect(Collectors.toList());
    }
}
