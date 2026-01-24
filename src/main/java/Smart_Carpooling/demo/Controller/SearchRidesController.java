package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Entity.SearchRide;
import Smart_Carpooling.demo.Repository.RideRepository;
import Smart_Carpooling.demo.Service.GeocodingService;
import Smart_Carpooling.demo.Service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("SearchRides")
public class SearchRidesController {
    @Autowired
    private GeocodingService geocodingService;
    @Autowired
    private RideService rideService;
    @Autowired
    private RideRepository rideRepository;
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

    @PostMapping("/search")
    public ResponseEntity<List<Ride>> searchRides(@RequestBody SearchRide req) {
        if (req.getStartLocation() == null || req.getStartLocation().isBlank()) {
            return ResponseEntity.badRequest().body(List.of());
        }
        double[] userStartLatLng;
        try {
            userStartLatLng =
                    rideService.getLatLngFromAddress(req.getStartLocation());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(List.of());
        }
        if (userStartLatLng == null) {
            return ResponseEntity.badRequest().body(List.of());
        }
        System.out.println("USER LAT = " + userStartLatLng[0]);
        System.out.println("USER LNG = " + userStartLatLng[1]);
        Point userStart = new Point(
                userStartLatLng[1], // lng
                userStartLatLng[0]  // lat
        );
        Distance radius = new Distance(50, Metrics.KILOMETERS);
        List<Ride> nearbyRides =
                rideRepository.findByStartPointNear(userStart, radius);
        List<Ride> result = rideRepository.findByStartPointNear(userStart,radius);
        System.out.println("Found rides= "+result.size());

        return ResponseEntity.ok(result);
    }


}
