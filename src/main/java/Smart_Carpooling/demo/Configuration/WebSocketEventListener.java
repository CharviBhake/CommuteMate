package Smart_Carpooling.demo.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(event.getMessage());

        Principal principal = accessor.getUser();
        if (principal != null) {
            String username = principal.getName();
            log.info("User disconnected: {}", username);
            // OPTIONAL: broadcast presence update (not a chat message)
            // You can later enhance this with rideId if needed
            messagingTemplate.convertAndSend(
                    "/topic/presence",
                    username + " disconnected"
            );
        }
    }
}
