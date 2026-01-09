package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.ChatNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatNotificationRepository extends MongoRepository<ChatNotification,String> {
    List<ChatNotification> findByrecipientIdAndSeenFalse(String recipientId);
}
