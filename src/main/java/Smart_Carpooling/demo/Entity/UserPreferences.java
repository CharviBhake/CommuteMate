package Smart_Carpooling.demo.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="user_preferences")
public class UserPreferences {
    @Id
    Long id;
    boolean smokingAllowed;
    boolean musicAllowed;
    String genderPreference;
    boolean petFriendly;
}
