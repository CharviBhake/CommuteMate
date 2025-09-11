package Smart_Carpooling.demo.Controller;


import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Entity.UserPreferences;
import Smart_Carpooling.demo.Repository.UserPreferenceRepo;
import Smart_Carpooling.demo.Service.UserPreferenceService;
import Smart_Carpooling.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/preference")
public class UserPreferenceController {
    @Autowired
    private UserPreferenceService userPreferenceService;
    @Autowired
    private UserPreferenceRepo userPreferenceRepo;
    @Autowired
    private UserService userService;

    @PutMapping
    public ResponseEntity<?> putPreferences(UserPreferences userPreferences){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user=userService.findByUsername(userName);
        if(user!=null){
            user.getUserPreferences().setGenderPreference(userPreferences.getGenderPreference());
            user.getUserPreferences().setMusicAllowed(userPreferences.isMusicAllowed());
            user.getUserPreferences().setPetFriendly(userPreferences.isPetFriendly());
            user.getUserPreferences().setMusicAllowed(userPreferences.isMusicAllowed());
            userService.saveNewUser(user);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

    }
    @GetMapping
    public ResponseEntity<UserPreferences> getPreferences(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user=userService.findByUsername(userName);
        if(user!=null){
            UserPreferences userPreferences=user.getUserPreferences();
            return new ResponseEntity<>(userPreferences,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("/update")
    public ResponseEntity<?> updatePeferences(@RequestBody UserPreferences userPreferences){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user=userService.findByUsername(userName);
        if(user!=null){
            user.setUserPreferences(userPreferences);
            userService.saveNewUser(user);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
