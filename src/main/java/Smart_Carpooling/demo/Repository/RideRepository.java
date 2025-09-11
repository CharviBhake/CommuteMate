package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepository extends MongoRepository<Ride,String> {
}
