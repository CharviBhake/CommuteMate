package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Service.RideService;
import Smart_Carpooling.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/ride")
public class RiderController {
    @Autowired
    private RideService rideService;
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<Ride>> getAllRides(){
        List<Ride> rides=rideService.getRides();
        if(rides.size()!=0) {
            System.out.println(rides);
            return new ResponseEntity<>(rides, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @GetMapping("{rideId}")
    public ResponseEntity<Ride> getRideWithID(@PathVariable String rideId){
        return rideService.getRide(rideId)
                .map(ride -> new ResponseEntity<>(ride, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/add")
    public ResponseEntity<String> addRide(@RequestBody Ride ride){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb=userService.findByUsername(userName);
        System.out.println(authentication);
        System.out.println(authentication.getAuthorities());
        System.out.println(authentication.isAuthenticated());
        if(userInDb.getRideList() == null){
            userInDb.setRideList(new ArrayList<>());
        }
        if(userInDb!=null){
            double[] langlat1=rideService.getLatLngFromAddress(ride.getStartlocation());
            double[] langlat2=rideService.getLatLngFromAddress(ride.getEndLocation());
            Ride ride1=Ride.builder()
                    .driver(userInDb)
                    .availableSeats(ride.getAvailableSeats())
                    .rideDate(ride.getRideDate())
                    .departureTime(ride.getDepartureTime())
                    .pricePerSeat(ride.getPricePerSeat())
                    .startlocation(ride.getStartlocation())
                    .endLocation(ride.getEndLocation())
                    .departureTime(ride.getDepartureTime())
                    .status(ride.getStatus())
                    .startLatitude(langlat1[0])
                    .startLongitude(langlat1[1])
                    .endLatitude(langlat2[0])
                    .endLongitude(langlat2[1])
                    .bookings(new ArrayList<>())
                    .build();

            userInDb.getRideList().add(ride1);
            userService.saveNewUser(userInDb);
            rideService.saveRide(ride1);
            return ResponseEntity.ok("Ride added successfully");
        }
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
    @PutMapping("/{RideId}/update")
    public ResponseEntity<Ride> updateRide(@RequestBody Ride ride, @PathVariable String RideId){
        Optional<Ride> ride1=rideService.getRide(RideId);
        Ride ride2=ride1.orElse(null);
        System.out.println(ride2);
        if(ride2!=null){
            ride2.setAvailableSeats(ride.getAvailableSeats());
            ride2.setStatus(ride.getStatus());
            ride2.setDepartureTime(ride.getDepartureTime());
            ride2.setPricePerSeat(ride.getPricePerSeat());
            rideService.saveRide(ride2);
            return new ResponseEntity<>(ride2,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @DeleteMapping("/{rideId}/delete")
    public ResponseEntity<?> deleteRide(@PathVariable String rideId ){
       Optional<Ride> ride=rideService.getRide(rideId);
       Ride ride1=ride.orElse(null);
       if(ride1!=null){
           rideService.deleteById(rideId);
           return new ResponseEntity<>(HttpStatus.OK);
       }
       return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
