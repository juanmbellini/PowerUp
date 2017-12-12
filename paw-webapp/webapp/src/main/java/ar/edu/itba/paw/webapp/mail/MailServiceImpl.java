package ar.edu.itba.paw.webapp.mail;

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

    final JavaMailSender mailSender;
    private final Environment environment;

    private Logger logger = LoggerFactory.getLogger(getClass());

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
            logger.error("Couldn't get PowerUp email", e);
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
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nThis email was sent to you because someone requested a password reset on your account. \n\n Visit the following URL to set a new password: "
                        + url +
                        "\n Your sincerely, \n PowerUp team.");
                mimeMessage.setSubject("PowerUp password reset");
            }
        };

        try {
            mailSender.send(preparator);
            logger.info("Sent new password email to {} ({})", user.getUsername(), user.getEmail());
        } catch (MailException ex) {
            logger.error("Couldn't send new password email to {} ({}): {}", user.getUsername(), user.getEmail(), ex);
        }
    }

    @Override
    public void sendPasswordChangedEmail(User user) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setFrom(from());
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(user.getEmail()));
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nYour password was changed. If you haven't changed your password, please reset your password at"
                        + " the Log In page clicking on \"Forgot Password?\"");
                mimeMessage.setSubject("PowerUp password change notice");
            }
        };

        try {
            mailSender.send(preparator);
            logger.info("Sent password change confirmation email to {} ({})", user.getUsername(), user.getEmail());
        } catch (MailException ex) {
            logger.error("Couldn't send password change confirmation email to {} ({}): {}", user.getUsername(), user.getEmail(), ex);
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
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nWelcome to PowerUp, your videogame database and discovery platform. "
                        + " We hope you enjoy our services. Feel free to send any feedback to powerappcontact@gmail.com. " +
                        "\n Your sincerely," +
                        "\n PowerUp team.");
                mimeMessage.setSubject(user.getUsername() + ", welcome to PowerUp!");
            }
        };

        try {
            mailSender.send(preparator);
            logger.info("Sent welcome email to {} ({})", user.getUsername(), user.getEmail());
        } catch (MailException ex) {
            logger.error("Couldn't send welcome email to {} ({}): {}", user.getUsername(), user.getEmail(), ex);
        }
    }
}