package Smart_Carpooling.demo;


import Smart_Carpooling.demo.Service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.mongodb.assertions.Assertions.assertTrue;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;

    @Test
    void testSendMail(){
        emailService.sendEmail("charvibhake2@gmail.com",
                "Testing java mail sender",
                "hi aap kaise hp?");
        assertTrue(true);
    }
}
