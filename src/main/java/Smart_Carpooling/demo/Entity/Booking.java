package Smart_Carpooling.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="Bookings")
public class Booking {
    @Id
    private String id;
    @ManyToOne
    private User passenger;
    @ManyToOne
    private Ride ride;
    private int seatsBooked;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
