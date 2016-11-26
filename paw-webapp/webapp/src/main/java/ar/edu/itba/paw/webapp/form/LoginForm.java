package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Pattern;

/**
 * Created by julian on 13/10/16.
 */
public class
LoginForm {

    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    private String password;

    private Boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
