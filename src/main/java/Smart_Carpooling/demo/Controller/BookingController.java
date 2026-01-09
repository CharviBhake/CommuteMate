package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.Booking;
import Smart_Carpooling.demo.Entity.BookingStatus;
import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Service.BookingServic;
import Smart_Carpooling.demo.Service.RideService;
import Smart_Carpooling.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingServic bookingServic;
    @Autowired
    private UserService userService;
    @Autowired
    private RideService rideService;

    @PutMapping("{rideId}")
    public ResponseEntity<?> addBooking(@PathVariable String rideId, @RequestBody Booking booking){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb=userService.findByUsername(userName);
        Optional<Ride> ride1=rideService.getRide(rideId);
        Ride ride2=ride1.orElse(null);
        Booking booking1= Booking.builder()
                .passenger(userInDb)
                .ride(ride2)
                .seatsBooked(booking.getSeatsBooked())
                .status(BookingStatus.PENDING)
                .build();
        ride2.getBookings().add(booking1);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("{rideId}")
    public ResponseEntity<?> getAll(@PathVariable String rideId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb=userService.findByUsername(userName);
        Optional<Ride> ride1=rideService.getRide(rideId);
        Ride ride2=ride1.orElse(null);
        User driver=ride2.getDriver();
        if(userInDb.equals(driver)){
            List<Booking> bookingList=ride2.getBookings();
            return new ResponseEntity<>(bookingList, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PutMapping("{rideId}/{bookingId}/CONFIRMED")
    public ResponseEntity<?> changeStatus(@PathVariable String rideId, String bookingId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb=userService.findByUsername(userName);
        Optional<Ride> ride1=rideService.getRide(rideId);
        Ride ride2=ride1.orElse(null);
        User driver=ride2.getDriver();
        if(userInDb==driver){
            Optional<Booking> booking=bookingServic.findBooking(bookingId);
            Booking booking1=booking.orElse(null);
            int numSeats=booking1.getSeatsBooked();
            int rideSeats=ride2.getAvailableSeats();
            if(numSeats>rideSeats) return changeStatus2(rideId,bookingId);
            booking1.setStatus(BookingStatus.CONFIRMED);
            ride2.setAvailableSeats(rideSeats-numSeats);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
    @PutMapping("{rideId}/{bookingId}/CANCELLED")
    public ResponseEntity<?> changeStatus2(@PathVariable String rideId, String bookingId){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDb=userService.findByUsername(userName);
        Optional<Ride> ride1=rideService.getRide(rideId);
        Ride ride2=ride1.orElse(null);
        User driver=ride2.getDriver();
        if(userInDb==driver){
            Optional<Booking> booking=bookingServic.findBooking(bookingId);
            Booking booking1=booking.orElse(null);
            booking1.setStatus(BookingStatus.CANCELLED);
            ride2.getBookings().remove(booking1);
            rideService.saveRide(ride2);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

}
