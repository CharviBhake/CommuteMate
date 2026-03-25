package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review,String> {
    boolean existsByReviewerIdAndRideId(String reviewerId,String rideId);
    List<Review> findByRevieweeId(String revieweeId);
    List<Review> findByReviewerId(String reviewerId);
}
