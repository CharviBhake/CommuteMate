package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.Ride;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
@Repository
public interface RideRepository extends MongoRepository<Ride,String> {
    List<Ride> findByDriverId(String driverId);
    List<Ride> findByStartPointNear(Point point, Distance distance);
}
