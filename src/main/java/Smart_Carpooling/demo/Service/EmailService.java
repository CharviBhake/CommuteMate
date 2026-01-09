package Smart_Carpooling.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("charvibhake@gmail.com"); // must match your sender
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            System.out.println("Preparing to send email...");
            mailSender.send(message);
            System.out.println("Email sent!");

        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println(" Email not sent successfully to " + to);
        }
    }
}
