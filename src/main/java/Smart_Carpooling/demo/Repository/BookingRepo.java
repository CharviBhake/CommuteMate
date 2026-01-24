package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.Booking;
import Smart_Carpooling.demo.Entity.BookingStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepo extends MongoRepository<Booking,String> {
    List<Booking> findByRideIdAndStatus(String rideId, BookingStatus status);
    boolean existsByRideIdAndPassengerIdAndStatus(
            String rideId,
            String passengerId,
            BookingStatus status
    );
    List<Booking> findByRideId(String rideId);

}
