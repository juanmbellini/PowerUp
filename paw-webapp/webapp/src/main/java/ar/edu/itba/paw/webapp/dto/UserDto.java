package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    private String userName;

    @XmlElement
    private String email;

    @XmlElement
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UserDto() {
        // For Jax-RS
    }

    public UserDto(User user) {
        super(user.getId());
        userName = user.getUsername();
        email = user.getEmail();
    }


    public String getUserName() {
        return userName;
    }


    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    /**
     * Returns a list of {@link UserDto} based on the given collection of {@link User}.
     *
     * @param users The collection of {@link User}
     * @return A list of {@link UserDto}.
     */
    public static List<UserDto> createList(Collection<User> users) {
        return users.stream().map(UserDto::new).collect(Collectors.toList());
    }
}
