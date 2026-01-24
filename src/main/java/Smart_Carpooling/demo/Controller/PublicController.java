package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Service.UserDetailsServiceImpl;
import Smart_Carpooling.demo.Service.UserService;
import Smart_Carpooling.demo.Utitly.JWTUTIL;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@CrossOrigin(origins="http://localhost:5173")
@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUTIL jwtutil;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody User user) {
        user.setDisplayUsername(user.getUsername());
        user.setUsername(user.getEmail());   // email stored as username
        user.setEmail(user.getEmail());
       // user.setDisplayUsername(user.getUsername());
        userService.saveNewUser(user);

        String token = jwtutil.generateToken(user.getId());
        return ResponseEntity.ok(Map.of("token", token));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        System.out.println("LOGIN HIT (email): " + loginRequest.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            User userFromDb = userService.findByUsername(loginRequest.getEmail());
            if (userFromDb == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "User not found"));
            }
            String jwt = jwtutil.generateToken(userFromDb.getId());
            System.out.println("LOGIN JWT SUBJECT = " + userFromDb.getId());

            return ResponseEntity.ok(Map.of("token", jwt));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Incorrect email or password"));
        }
    }



    @GetMapping("/health-check")
    public String HealthCheck(){return "OK";}
}
