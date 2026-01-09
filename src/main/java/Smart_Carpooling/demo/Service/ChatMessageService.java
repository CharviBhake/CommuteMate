package Smart_Carpooling.demo.Service;


import Smart_Carpooling.demo.Entity.ChatMessage;
import Smart_Carpooling.demo.Entity.ChatRoom;
import Smart_Carpooling.demo.Repository.ChatMessageRepository;
import Smart_Carpooling.demo.Repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatRoomService chatRoomService;
    @Autowired
    private final ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(
            String rideId,
            String senderId,
            String content
    ) {
        ChatRoom room = chatRoomService.getOrCreateChatRoom(rideId);
        ChatMessage message = ChatMessage.builder()
                .chatRoomId(room.getId())
                .senderId(senderId)
                .content(content)
                .timestamp(LocalDateTime.now())
                .build();

        return chatMessageRepository.save(message);
    }
    public List<ChatMessage> findChatMessages(String rideId) {

        return chatRoomService.getChatRoomForRide(rideId)
                .map(room ->
                        chatMessageRepository
                                .findByChatRoomIdOrderByTimestampAsc(room.getId())
                )
                .orElse(List.of());
    }
}
