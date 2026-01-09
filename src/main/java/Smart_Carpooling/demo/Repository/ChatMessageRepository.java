package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String > {
    List<ChatMessage> findByChatRoomId(String chatRoomId);
    List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(String chatRoomId);
}
