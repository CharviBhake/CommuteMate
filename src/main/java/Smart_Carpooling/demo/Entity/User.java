package Smart_Carpooling.demo.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="users")
public class User {
    private String username;
    @Id
    private String id;
    private String email;
    private String password;
    private String role;
    private UserPreferences userPreferences;
    @DBRef
    @JsonIgnoreProperties("driver")
    private ArrayList<Ride> rideList;
}
