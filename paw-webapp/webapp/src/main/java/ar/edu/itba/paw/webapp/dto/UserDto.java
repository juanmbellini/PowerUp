package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.webapp.model.User;

public class UserDto {

    private long id;

    private String username;

    private String email;

    public UserDto() {}

    public UserDto(User user, boolean complete) {
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
    }

    public UserDto(User user) {
        this(user, false);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
