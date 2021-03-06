package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.GameJerseyController;
import ar.edu.itba.paw.webapp.controller.ReviewJerseyController;
import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.Review;
import ar.edu.itba.paw.webapp.model_wrappers.LikeableWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;

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
 * Created by Juan Marcos Bellini on 4/5/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "review")
public class ReviewDto extends EntityDto {


    @XmlElement
    private Long userId;

    @XmlElement
    private String username;

    @XmlElement
    private Long gameId;

    @XmlElement
    private String gameName;

    @XmlElement
    private String gameCoverPictureUrl;

    @XmlElement
    private String date;

    @XmlElement
    private String body;

    @XmlElement
    private String userUrl;

    @XmlElement
    private String gameUrl;

    @XmlElement
    private Long likeCount;

    @XmlElement
    private Boolean likedByCurrentUser;

    @XmlElement
    private String likesUrl;

    @XmlElement
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String userProfilePictureUrl;

    public ReviewDto() {
        // For Jax-RS
    }

    public ReviewDto(LikeableWrapper<Review> wrapper, UriBuilder baseUri) {
        super(wrapper.getEntity().getId());
        this.userId = wrapper.getEntity().getUser().getId();
        this.username = wrapper.getEntity().getUser().getUsername();
        this.gameId = wrapper.getEntity().getGame().getId();
        this.gameName = wrapper.getEntity().getGame().getName();
        this.gameCoverPictureUrl = wrapper.getEntity().getGame().getCoverPictureUrl();
        this.date = LocalDateTime.ofInstant(wrapper.getEntity().getDate().toInstant(), ZoneId.systemDefault()).toString();
        this.body = wrapper.getEntity().getReview();
        this.likeCount = wrapper.getLikeCount();
        this.likedByCurrentUser = wrapper.getLikedByCurrentUser();

        // Urls
        this.userUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(String.valueOf(wrapper.getEntity().getUser().getId()))
                .build().toString();
        this.gameUrl = baseUri.clone()
                .path(GameJerseyController.END_POINT)
                .path(String.valueOf(wrapper.getEntity().getGame().getId()))
                .build().toString();
        this.likesUrl = baseUri.clone()
                .path(ReviewJerseyController.END_POINT)
                .path(Long.toString(wrapper.getEntity().getId()))
                .path(ReviewJerseyController.LIKES_END_POINT)
                .build().toString();
        if (wrapper.getEntity().getUser().hasProfilePicture()) {
            this.userProfilePictureUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(this.userId))
                    .path(UserJerseyController.PICTURE_END_POINT)
                    .build().toString();
        } else {
            this.userProfilePictureUrl = null;
        }
    }

    private ReviewDto(Review review, UriBuilder baseUri) {
        super(review.getId());
        this.userId = review.getUser().getId();
        this.username = review.getUser().getUsername();
        this.gameId = review.getGame().getId();
        this.gameName = review.getGame().getName();
        this.gameCoverPictureUrl = review.getGame().getCoverPictureUrl();
        this.date = LocalDateTime.ofInstant(review.getDate().toInstant(), ZoneId.systemDefault()).toString();
        this.body = review.getReview();
        if (review.getUser().hasProfilePicture()) {
            this.userProfilePictureUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(this.userId))
                    .path(UserJerseyController.PICTURE_END_POINT)
                    .build().toString();
        } else {
            this.userProfilePictureUrl = null;
        }
    }


    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Long getGameId() {
        return gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public String getGameCoverPictureUrl() {
        return gameCoverPictureUrl;
    }

    public String getDate() {
        return date;
    }

    public String getBody() {
        return body;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    public Boolean getLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public String getUserProfilePictureUrl() {
        return userProfilePictureUrl;
    }

    /**
     * Returns a list of {@link ReviewDto} based on the given collection of {@link Review}.
     *
     * @param reviews The collection of {@link Review}
     * @return A list of {@link ReviewDto}.
     */
    public static List<ReviewDto> createList(Collection<LikeableWrapper<Review>> reviews, UriBuilder uriBuilder) {
        return reviews.stream().map(review -> new ReviewDto(review, uriBuilder.clone())).collect(Collectors.toList());
    }

    /**
     * Returns a list of {@link ReviewDto} based on the given collection of {@link Review}.
     *
     * @param reviews The collection of {@link Review}
     * @return A list of {@link ReviewDto}.
     */
    public static List<ReviewDto> createListWithoutCount(Collection<Review> reviews, UriBuilder uriBuilder) {
        return reviews.stream().map(review -> new ReviewDto(review, uriBuilder.clone())).collect(Collectors.toList());
    }
}
