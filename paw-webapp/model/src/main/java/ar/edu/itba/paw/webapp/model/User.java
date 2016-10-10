package ar.edu.itba.paw.webapp.model;

public class User {
    private long id;
    private String username;
    private String email;
    //TODO track ranked games in model?

    public User(long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public User(long id, String email) {
        this(id, email, null);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }
}
