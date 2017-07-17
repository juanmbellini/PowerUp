package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.controller.UserJerseyController;
import ar.edu.itba.paw.webapp.model.User;
import ar.edu.itba.paw.webapp.model_wrappers.UserWithFollowCountsWrapper;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.UriBuilder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user")
public class UserDto extends EntityDto {


    @XmlElement
    private String username;

    @XmlElement
    private String email;

    @XmlElement
    private SocialDto social;

    @XmlElement
    private String userUrl;

    @XmlElement
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;


    public UserDto() {
        // For Jax-RS
    }

    public UserDto(UserWithFollowCountsWrapper wrapper, UriBuilder baseUri) {
        this(wrapper.getUser(), baseUri);
        this.social = new SocialDto(wrapper, baseUri.clone());
    }

    public UserDto(User user, UriBuilder baseUri) {
        super(user.getId());
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.userUrl = baseUri.clone()
                .path(UserJerseyController.END_POINT)
                .path(Long.toString(user.getId()))
                .build().toString();
    }


    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public SocialDto getSocial() {
        return social;
    }

    public String getUserUrl() {
        return userUrl;
    }

    /**
     * Returns a list of {@link UserDto} based on the given collection of {@link UserWithFollowCountsWrapper}.
     *
     * @param users The collection of {@link User}
     * @return A list of {@link UserDto}.
     */
    public static List<UserDto> createList(Collection<UserWithFollowCountsWrapper> users, UriBuilder uriBuilder) {
        return users.stream().map(wrapper -> new UserDto(wrapper, uriBuilder.clone())).collect(Collectors.toList());
    }

    /**
     * Returns a list of {@link UserDto} based on the given collection of {@link User}.
     *
     * @param users The collection of {@link User}
     * @return A list of {@link UserDto}.
     */
    public static List<UserDto> createListWithoutFollowCount(Collection<User> users, UriBuilder uriBuilder) {
        return users.stream().map(user -> new UserDto(user, uriBuilder)).collect(Collectors.toList());
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "social")
    public static class SocialDto {


        @XmlElement
        private Long followingCount;

        @XmlElement
        private Long followersCount;

        @XmlElement
        private String followingUrl;

        @XmlElement
        private String followersUrl;

        @XmlElement
        private Boolean followedByCurrentUser;

        @XmlElement
        private Boolean followingCurrentUser;


        public SocialDto() {
            // For Jax-RS
        }


        public SocialDto(UserWithFollowCountsWrapper wrapper, UriBuilder baseUri) {
            this.followingCount = wrapper.getFollowingCount();
            this.followersCount = wrapper.getFollowersCount();
            this.followingUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(wrapper.getUser().getId()))
                    .path(UserJerseyController.FOLLOWING_END_POINT)
                    .build().toString();
            this.followersUrl = baseUri.clone()
                    .path(UserJerseyController.END_POINT)
                    .path(Long.toString(wrapper.getUser().getId()))
                    .path(UserJerseyController.FOLLOWERS_END_POINT)
                    .build().toString();
            this.followedByCurrentUser = wrapper.getFollowedByCurrentUser();
            this.followingCurrentUser= wrapper.getFollowingCurrentUser();
        }

        public Long getFollowingCount() {
            return followingCount;
        }

        public Long getFollowersCount() {
            return followersCount;
        }

        public String getFollowingUrl() {
            return followingUrl;
        }

        public String getFollowersUrl() {
            return followersUrl;
        }

        public Boolean getFollowingCurrentUser() {
            return followingCurrentUser;
        }

        public Boolean getFollowedByCurrentUser() {
            return followedByCurrentUser;
        }
    }
}
