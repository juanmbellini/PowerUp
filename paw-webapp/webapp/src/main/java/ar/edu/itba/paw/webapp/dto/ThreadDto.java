package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.ThreadJerseyController;
import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.Thread;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model_wrappers.ThreadWithLikesCount;

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
 * Created by Juan Marcos Bellini on 19/4/17.
 * Questions at jbellini@itba.edu.ar or juanmbellini@gmail.com
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "thread")
public class ThreadDto extends EntityDto {


    @XmlElement
    private String title;

    @XmlElement
    private String body;

    @XmlElement
    private String createdAt;

    @XmlElement
    private Long likesCount;

    @XmlElement
    private CreatorDto creator;

    @XmlElement
    private String commentsUrl;

    @XmlElement
    private String likesUrl;


    public ThreadDto() {
        // For Jax-RS
    }


    public ThreadDto(ThreadWithLikesCount threadWithLikesCount, UriBuilder baseUri) {
        super(threadWithLikesCount.getThread().getId());
        this.title = threadWithLikesCount.getThread().getTitle();
        this.body = threadWithLikesCount.getThread().getBody();
        this.createdAt = LocalDateTime.ofInstant(threadWithLikesCount.getThread().getCreatedAt().toInstant(),
                ZoneId.systemDefault()).toString();
        this.creator = new CreatorDto(threadWithLikesCount.getThread().getCreator(), baseUri.clone());
        this.likesCount = threadWithLikesCount.getLikesCount();
        this.commentsUrl = baseUri.clone()
                .path(ThreadJerseyController.END_POINT)
                .path(Long.toString(threadWithLikesCount.getThread().getId()))
                .path(ThreadJerseyController.COMMENTS_END_POINT)
                .build().toString();
        this.likesUrl = baseUri.clone()
                .path(ThreadJerseyController.END_POINT)
                .path(Long.toString(threadWithLikesCount.getThread().getId()))
                .path(ThreadJerseyController.LIKES_END_POINT)
                .build().toString();
    }


    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public CreatorDto getCreator() {
        return creator;
    }

    public Long getLikesCount() {
        return likesCount;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public String getLikesUrl() {
        return likesUrl;
    }

    /**
     * Returns a list of {@link ThreadDto} based on the given collection of {@link Thread}.
     *
     * @param threads The collection of {@link Thread}
     * @return A list of {@link ThreadDto}.
     */
    public static List<ThreadDto> createList(Collection<ThreadWithLikesCount> threads, UriBuilder uriBuilder) {
        return threads.stream().map(thread -> new ThreadDto(thread, uriBuilder.clone())).collect(Collectors.toList());
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "creator")
    public static class CreatorDto extends EntityDto {


        @XmlElement
        private String userName;

        @XmlElement
        private String profilePictureUrl;

        @XmlElement
        private String creatorUrl;


        public CreatorDto() {
            // For Jax-RS
        }


        public CreatorDto(User user, UriBuilder baseUri) {
            super(user.getId());
            this.userName = user.getUsername();
            this.creatorUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(user.getId()))
                    .build().toString();
            this.profilePictureUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(user.getId()))
                    .path("picture")
                    .build().toString();
        }


        public String getUserName() {
            return userName;
        }

        public String getProfilePictureUrl() {
            return profilePictureUrl;
        }

        public String getCreatorUrl() {
            return creatorUrl;
        }
    }

}