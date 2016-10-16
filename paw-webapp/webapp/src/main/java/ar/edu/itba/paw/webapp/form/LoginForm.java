package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by julian on 13/10/16.
 */
public class LoginForm {
    @Size(min = 6, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String username;

    @Size(min = 6, max = 100)
    private String password;

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
}
