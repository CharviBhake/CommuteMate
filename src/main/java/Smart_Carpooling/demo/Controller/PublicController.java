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
    public ResponseEntity<String> signup(@RequestBody User user){
        User user1=new User();
        user1.setEmail(user.getEmail());
        user1.setUsername(user.getUsername());
        user1.setPassword(user.getPassword());
        userService.saveNewUser(user1);
        String token=jwtutil.generateToken(user1.getUsername());
        log.info("Saving user: {}", user.getUsername());
        return ResponseEntity.ok("Bearer"+token);
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
            UserDetails userDetails=userDetailsService.loadUserByUsername(user.getUsername());
            String jwt=jwtutil.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occured wjile creating token incorrect pass or name",e);
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/health-check")
    public String HealthCheck(){return "OK";}
}
