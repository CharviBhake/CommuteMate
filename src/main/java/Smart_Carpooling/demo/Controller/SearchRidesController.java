package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Service.GeocodingService;
import Smart_Carpooling.demo.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("SearchRides")
public class SearchRidesController {
    @Autowired
    private GeocodingService geocodingService;
    @Autowired
    private RideService rideService;

    @GetMapping("/nearby")
    public ResponseEntity<List<Ride>> getNearbyRides(@RequestParam String address) {
        try {
          
            double[] latLng = rideService.getLatLngFromAddress(address);
            double userLat = latLng[0];
            double userLng = latLng[1];

            List<Ride> nearbyRides = rideService.findRidesWithinRadius(userLat, userLng, 1.0);

            return ResponseEntity.ok(nearbyRides);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
