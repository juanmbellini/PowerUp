package ar.edu.itba.paw.webapp.interfaces;

import ar.edu.itba.paw.webapp.model.User;

public interface MailService {

    /**
     * Sends an email to the specified user notifying them that their password was reset to the specified password.
     *
     * @param user The user whose password was reset.
     * @param newPassword The new password.
     */
    void sendPasswordResetEmail(User user, String newPassword);

    /**
     * Sends an email to the specified user confirming that their password was recently changed. Unlike {@link #sendPasswordResetEmail(User, String)},
     * this is used when a user manually changes their password, rather than when the password is reset because they
     * forgot their password.
     *
     * @param user The user who recently changed their password.
     */
    void sendPasswordChangedEmail(User user);

    /**
     * Sends an email to the specified user welcoming them to the app.
     *
     * @param user The user whose password was reset.
     */
    void sendWelcomeEmail(User user);
}