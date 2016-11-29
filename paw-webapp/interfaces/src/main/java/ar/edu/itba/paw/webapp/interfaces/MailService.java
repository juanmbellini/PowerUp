package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.User;
import org.springframework.stereotype.Service;

/**
 * Created by Juan on 29-Nov-16.
 */
public interface MailService {

    public void sendEmailChangePassword(User user, String newPassword);
}
