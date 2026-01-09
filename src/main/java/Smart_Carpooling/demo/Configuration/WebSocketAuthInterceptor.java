package Smart_Carpooling.demo.Configuration;

import Smart_Carpooling.demo.Filter.JwtFilter;
import Smart_Carpooling.demo.Service.UserDetailsServiceImpl;
import Smart_Carpooling.demo.Utitly.JWTUTIL;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    private final JWTUTIL jwtutil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message,MessageChannel channel){
        StompHeaderAccessor accessor=MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
        if(accessor!=null && StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader=accessor.getFirstNativeHeader("Authorization");
            if(authHeader!=null && authHeader.startsWith("Bearer")){
                String token=authHeader.substring(7);
                String username=jwtutil.extractusername(token);
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                if(jwtutil.validateToken(token,userDetails)){
                    UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}
