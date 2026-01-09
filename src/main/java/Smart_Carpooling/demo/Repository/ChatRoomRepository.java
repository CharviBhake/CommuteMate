package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom,String> {
   // Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId,String recipientId);
    Optional<ChatRoom> findByRideId(String rideid);
}
