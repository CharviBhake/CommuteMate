package Smart_Carpooling.demo.Service;

import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Entity.UserPreferences;
import Smart_Carpooling.demo.Repository.UserPreferenceRepo;
import Smart_Carpooling.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserPreferenceService {
    @Autowired
    private UserPreferenceRepo userPreferenceRepo;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    public UserPreferences getUserPreferences(String userName){
        User user=userService.findByUsername(userName);
        UserPreferences userPreferences=user.getUserPreferences();
        return userPreferences;
    }

}
