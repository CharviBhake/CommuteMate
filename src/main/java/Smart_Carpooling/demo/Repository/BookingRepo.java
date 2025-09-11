package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepo extends MongoRepository<Booking,String> {
}
