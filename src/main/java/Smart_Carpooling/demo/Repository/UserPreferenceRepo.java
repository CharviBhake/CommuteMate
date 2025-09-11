package Smart_Carpooling.demo.Repository;

import Smart_Carpooling.demo.Entity.UserPreferences;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepo extends MongoRepository<UserPreferences,String> {
}
