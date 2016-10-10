package ar.edu.itba.paw.webapp.model;

public class User {
    private String username;
    private String email;
    //TODO track ranked games in model?

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public User(String email) {
        this(email, null);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
