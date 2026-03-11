package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Repository.UserRepository;
import Smart_Carpooling.demo.Service.JwtBlacklistService;
import Smart_Carpooling.demo.Service.UserService;
import Smart_Carpooling.demo.Utitly.JWTUTIL;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUTIL jwtutil;
    @Autowired
    private JwtBlacklistService jwtBlacklistService;
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(){
        try{
            Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
            String userId=authentication.getName();
            User user=userService.findById(userId);
            Map<String,Object> profile=new HashMap<>();
            profile.put("id", user.getId());
         //   profile.put("username", user.getUsername());
            profile.put("email", user.getEmail());
            profile.put("name", user.getDisplayUsername());
            profile.put("phone", user.getPhone());
            profile.put("location", user.getLocation());
            profile.put("city", user.getCity());
            profile.put("bio", user.getBio());
            profile.put("carModel", user.getCarModel());
            profile.put("carNumber", user.getCarNumber());
            profile.put("carColor", user.getCarColor());
           return ResponseEntity.ok(profile);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String,Object> updates){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();
            User user = userService.findById(userId);
            if (user != null) {
                if (updates.containsKey("name")) {
                    user.setDisplayUsername((String) updates.get("name"));
                }
                if (updates.containsKey("email")) {
                    user.setEmail((String) updates.get("email"));
                    user.setUsername((String) updates.get("email"));
                }
                if (updates.containsKey("phone")) {
                    user.setPhone((String) updates.get("phone"));
                }
                if (updates.containsKey("location")) {
                    user.setLocation((String) updates.get("location"));
                }
                if (updates.containsKey("city")) {
                    user.setCity((String) updates.get("city"));
                }
                if (updates.containsKey("bio")) {
                    user.setBio((String) updates.get("bio"));
                }
                if (updates.containsKey("carModel")) {
                    user.setCarModel((String) updates.get("carModel"));
                }
                if (updates.containsKey("carNumber")) {
                    user.setCarNumber((String) updates.get("carNumber"));
                }
                if (updates.containsKey("carColor")) {
                    user.setCarColor((String) updates.get("carColor"));
                }
                User updatedUser=userService.updateUser(user);
                Map<String, Object> profile = new HashMap<>();
                profile.put("id", updatedUser.getId());
                profile.put("username", updatedUser.getUsername());
                profile.put("email", updatedUser.getEmail());
                profile.put("name", updatedUser.getDisplayUsername());
                profile.put("phone", updatedUser.getPhone());
                profile.put("location", updatedUser.getLocation());
                profile.put("city", updatedUser.getCity());
                profile.put("bio", updatedUser.getBio());
                profile.put("carModel", updatedUser.getCarModel());
                profile.put("carNumber", updatedUser.getCarNumber());
                profile.put("carColor", updatedUser.getCarColor());
                return ResponseEntity.ok(profile);
            }
        }catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserByUserName(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // because username = email
        User user = userService.findById(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse request){
        String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer")){
            return ResponseEntity.badRequest().body("no token");
        }
        String token=header.substring(7);
        long ttl=jwtutil.getRemainingTime(token);
        jwtBlacklistService.blacklistToken(token,ttl);
        return ResponseEntity.ok("logged out successfully");
    }

}
