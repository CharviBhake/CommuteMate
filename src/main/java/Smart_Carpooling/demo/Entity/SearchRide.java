package Smart_Carpooling.demo.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRide {
    private String startLocation;
    private String endLocation;
    private double startLat;
    private double startLng;
    private double endLat;
    private double endLng;
    private LocalDate rideDate;
    private LocalTime time;
}
