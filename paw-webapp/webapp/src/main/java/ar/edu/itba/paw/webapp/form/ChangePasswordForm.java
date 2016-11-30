package ar.edu.itba.paw.webapp.form;

import de.malkusch.validation.constraints.EqualProperties;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualProperties({"newPassword","repeatNewPassword"})
public class ChangePasswordForm {

    @Size(min = 6, max = 100, message = "Your old password must be between 6 and 100 characters.")
    private String oldPassword;

    @Size(min = 6, max = 100, message = "Your new password must be between 6 and 100 characters.")
    private String newPassword;

    private String repeatNewPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }
}



