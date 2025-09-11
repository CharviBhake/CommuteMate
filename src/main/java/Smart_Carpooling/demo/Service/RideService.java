package Smart_Carpooling.demo.Service;

import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RideService {
    @Autowired
    private RideRepository rideRepository;
    @Autowired
    private GeocodingService geocodingService;

    public void saveRide(Ride ride){
        rideRepository.save(ride);
    }
    public Optional<Ride> getRide(String rideId){
        return rideRepository.findById(rideId);
    }
    public void deleteById(String rideId){
        rideRepository.deleteById(rideId);
    }
    public List<Ride> getRides(){
        return rideRepository.findAll();
    }
    public double[] getLatLngFromAddress(String address) {
        double[] ans=geocodingService.getCoordinates(address);
        return ans;
    }
    public List<Ride> findRidesWithinRadius(double userLat, double userLng, double radiusKm) {
        List<Ride> allRides = rideRepository.findAll();
        List<Ride> nearbyRides = new ArrayList<>();

        for (Ride ride : allRides) {
            double distance = haversine(userLat, userLng, ride.getStartLatitude(), ride.getStartLongitude());
            if (distance <= radiusKm) {
                nearbyRides.add(ride);
            }
        }
        return nearbyRides;
    }
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of Earth in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in km
    }
}
