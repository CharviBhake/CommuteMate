package Smart_Carpooling.demo.Entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AddRideRequest {
    private String startLocation;
    private String endLocation;
    private LocalDate rideDate;
    private LocalTime departureTime;
    private int availableSeats;
    private double pricePerSeat;
}

