package Smart_Carpooling.demo.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRide {
    private String start;
    private String end;
    private int departureTime;
    private int seats;
    private UserPreferences userPreferences;
}
