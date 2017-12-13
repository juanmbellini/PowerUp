package ar.edu.itba.paw.webapp.service;

import ar.edu.itba.paw.webapp.interfaces.MailService;
import ar.edu.itba.paw.webapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final Environment environment;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public MailServiceImpl(JavaMailSender mailSender, Environment environment) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    private InternetAddress from() {
        try {
            String name = environment.getRequiredProperty("email.name");
            String address = environment.getRequiredProperty("email.username");
            return new InternetAddress(name + " <" + address + ">");
        } catch (AddressException e) {
            LOGGER.error("Couldn't get PowerUp email", e);
            return null;
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, String url) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(from());
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear " + user.getUsername() + ", \n" +
                        "This email was sent to you because someone requested a password reset on your account. \n\n" +
                        "Visit the following link to set a new password: \n\n" +
                        url + "\n\n\n" +
                        "Yours sincerely, \n" +
                        "PowerUp team.");
                mimeMessage.setSubject("PowerUp password reset");
            }
        };

        try {
            mailSender.send(preparator);
            LOGGER.info("Sent reset password email to {} ({})", user.getUsername(), user.getEmail());
        } catch (MailException ex) {
            LOGGER.error("Couldn't send reset password email to {} ({}): {}", user.getUsername(), user.getEmail(), ex);
        }
    }

    @Override
    @Async
    public void sendPasswordChangedEmail(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(from());
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear " + user.getUsername() + ", \n" +
                        "Your password was changed.\n" +
                        "If it wasn't you, please reset it clicking on \"Forgot Password?\" in the log in page.\n\n" +
                        "Yours sincerely, \n" +
                        "PowerUp team.");
                mimeMessage.setSubject("PowerUp password change notice");
            }
        };

        try {
            mailSender.send(preparator);
            LOGGER.info("Sent password change confirmation email to {} ({})", user.getUsername(), user.getEmail());
        } catch (MailException ex) {
            LOGGER.error("Couldn't send password change confirmation email to {} ({}): {}", user.getUsername(), user.getEmail(), ex);
        }
    }

    @Override
    public void sendWelcomeEmail(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(from());
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear " + user.getUsername() + ", \n" +
                        "Welcome to PowerUp, your videogame database and discovery platform. " +
                        "We hope you enjoy our services.\n" +
                        "Feel free to send any feedback to powerappcontact@gmail.com.\n\n" +
                        "Yours sincerely,\n" +
                        "PowerUp team.");
                mimeMessage.setSubject(user.getUsername() + ", welcome to PowerUp!");
            }
        };

        try {
            mailSender.send(preparator);
            LOGGER.info("Sent welcome email to {} ({})", user.getUsername(), user.getEmail());
        } catch (MailException ex) {
            LOGGER.error("Couldn't send welcome email to {} ({}): {}", user.getUsername(), user.getEmail(), ex);
        }
    }
}