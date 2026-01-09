package Smart_Carpooling.demo.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatNotification {
    @Id
    private String id;
    private String chatRoomId;
    private String senderId;
    private String recipientId;
    private String content;
    private boolean seen;
}
