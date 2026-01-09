package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mail")
public class MailTestController {
    @Autowired
    private EmailService emailService;
    @GetMapping("/test")
    public String sendTestMail(@RequestParam String to) {
        emailService.sendEmail(to, "Smart Carpooling Test 🚗",
                "This is a test email from your Smart Car Pooling project!");
        return "Email sent to " + to;
    }
}
