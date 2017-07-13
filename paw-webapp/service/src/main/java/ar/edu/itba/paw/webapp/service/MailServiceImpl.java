package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.MailService;
import ar.edu.itba.paw.webapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Juan on 29-Nov-16.
 */
@Service
public class MailServiceImpl implements MailService{
    @Autowired
    JavaMailSender mailSender;

    @Async
    public void sendEmailResetPassword(User user, String newPassword) {


        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(new InternetAddress("powerup.paw@gmail.com"));
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nYour new password is: "
                        + newPassword
                        + "\nPlease head to your userpage and change your password");
                mimeMessage.setSubject("PowerUp password reset");
            }
        };

        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void sendEmailChangePassword(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(new InternetAddress("powerup.paw@gmail.com"));
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nYour password was changed. If you haven't changed your password, please reset your password at"
                        + " the log in page clicking on forgot password.");
                mimeMessage.setSubject("PowerUp password change notice");
            }
        };

        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void sendEmailWelcome(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(new InternetAddress("powerup.paw@gmail.com"));
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nWelcome to PowerUp, your videogame database and discovery platform. "
                        + " We hope you enjoy our services. Feel free to send any feedback to powerup.paw@gmail.com. " +
                        "\n Your sincerely," +
                        "\n PowerUp team.");
                mimeMessage.setSubject(user.getUsername() + ", welcome to PowerUp!");
            }
        };

        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
