package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.anotationValidators.CheckTakenEmail;
import ar.edu.itba.paw.webapp.anotationValidators.CheckTakenUsername;
import de.malkusch.validation.constraints.EqualProperties;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualProperties({"password","repeatPassword"})
public class UserForm {

    @Size(min = 1, max = 25)
    @Pattern(regexp = "[a-zA-Z0-9]*")
    @CheckTakenUsername
    private String username;

    @Size(min = 6, max = 100)
    private String password;

    private String repeatPassword;

    @Email
    @CheckTakenEmail
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword)
    {
        this.repeatPassword = repeatPassword;
    }
}



