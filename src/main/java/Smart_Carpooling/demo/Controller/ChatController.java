package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.ChatMessage;
import Smart_Carpooling.demo.Entity.ChatMessageRequest;
import Smart_Carpooling.demo.Entity.ChatNotification;
import Smart_Carpooling.demo.Repository.ChatMessageRepository;
import Smart_Carpooling.demo.Service.ChatMessageService;
import Smart_Carpooling.demo.Service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private  final ChatRoomService chatRoomService;
    private final ChatMessageRepository chatMessageRepository;
    @MessageMapping("/chat.send")
    public void sendMessage(
            @Payload ChatMessageRequest request,
            Principal principal
    ) {
        String senderId = principal.getName(); // from JWT
        System.out.println("🔥 CHAT MESSAGE RECEIVED FROM RIDEID"+request.getRideId());
        // Authorization check
        if (!chatRoomService.canUserChat(request.getRideId(), senderId)) {
            throw new RuntimeException("User not allowed to chat in this ride");
        }
        ChatMessage savedMessage = chatMessageService.saveMessage(
                request.getRideId(),
                senderId,
                request.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/ride/" + request.getRideId(),
                savedMessage
        );
    }

    @GetMapping("/rides/{rideId}/messages")
    public ResponseEntity<List<ChatMessage>> getChatMessages(
            @PathVariable String rideId
    ) {
        return ResponseEntity.ok(
                chatMessageService.findChatMessages(rideId)
        );
    }
    @GetMapping("/chat/history/{rideId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(
            @PathVariable String rideId,
            Authentication authentication
    ){
        System.out.println("CHAT HISTORY HIT FOR RIDID="+rideId);
        String user=authentication.getName();
       /* if(!chatRoomService.canUserChat(rideId,user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } */
        return ResponseEntity.ok(chatMessageRepository.findByRideIdOrderByTimestampAsc(rideId));
    }
}
