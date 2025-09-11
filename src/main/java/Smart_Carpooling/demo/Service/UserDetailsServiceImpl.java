package Smart_Carpooling.demo.Service;

import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUsername(username);
        System.out.println("USER: " + user);
        System.out.println("All users: " + userRepository.findAll());
        if(user!=null){
            UserDetails userDetails=org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build();

            return userDetails;
        }
        throw new UsernameNotFoundException("UseR not found with this username"+username);
    }
}
