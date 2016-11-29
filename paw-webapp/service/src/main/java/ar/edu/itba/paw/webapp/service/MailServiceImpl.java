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
    public void sendEmailChangePassword(User user) {


        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String password="Hola pepito";
                mimeMessage.setFrom(new InternetAddress("powerappservice@gmail.com"));
                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(//user.getEmail()
                                "julrodriguez@itba.edu.ar" ));
                mimeMessage.setText("Dear "
                        + user.getUsername()
                        + ", \nYour new password is: "
                        + password
                        + "\nPlease head to your userpage and change your password");
                mimeMessage.setSubject("Change your Trade My Game password");
            }
        };

        try {
            mailSender.send(preparator);
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
