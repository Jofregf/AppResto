package app.portfolioj0euahn7zjofregf.vercel.appbookingrestaurant.utilities;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String mail;

    public void sendEmail(String to, String subject, String text) {

        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(mail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText("<html><body><p style=\"color: #2F4858; font-size: 20px;\">" + text + "</p></body></html>", true);
            mailSender.send(message);
        }
        catch (MessagingException e){

        }
    }
}
