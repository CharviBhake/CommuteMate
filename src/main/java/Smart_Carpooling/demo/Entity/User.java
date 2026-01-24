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
    private String displayUsername;
    private String nickname;

    private String phone;
    private String location;
    private String city;
    private String bio;

    private String carModel;
    private String carNumber;
    private String carColor;

    private Status status;
    private UserPreferences userPreferences;
    private int rating;
    private String car;

    private int totalRides;
    private int ridesAsPassenger;
    private int ridesAsDriver;


}
