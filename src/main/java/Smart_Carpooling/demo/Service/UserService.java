package Smart_Carpooling.demo.Service;


import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean saveNewUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            logger.info("Saving new user: {}", user.getUsername());
            return true;
        } catch (Exception e) {
            logger.error("Error saving user: {}", user.getUsername(), e);
            return false;
        }

    }
    public User findByUsername(String userName){
        return userRepository.findByUsername(userName);
    }
}
