package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.Booking;
import Smart_Carpooling.demo.Entity.BookingStatus;
import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Repository.BookingRepo;
import Smart_Carpooling.demo.Repository.RideRepository;
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
    @Autowired
    private BookingRepo bookingRepo;

    @PutMapping("/{rideId}")
    public ResponseEntity<?> addBooking(
            @PathVariable String rideId,
            @RequestBody Booking bookingReq
    ) {
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("JWT USER ID = " + userId);
        User passenger=userService.findById(userId);
        if (passenger == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid user");
        }

        Ride ride = rideService.getRide(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        if (bookingReq.getSeatsBooked() > ride.getAvailableSeats()) {
            return ResponseEntity.badRequest().body("Not enough seats");
        }
        if(bookingRepo.existsByRideIdAndPassengerIdAndStatus(passenger.getId(),rideId,BookingStatus.CONFIRMED)||
                bookingRepo.existsByRideIdAndPassengerIdAndStatus(passenger.getId(),rideId,BookingStatus.PENDING)){
            return ResponseEntity.badRequest().body("Already Tried to book");
        }
        Booking booking = Booking.builder()
                .passenger(passenger)
                .ride(ride)
                .seatsBooked(bookingReq.getSeatsBooked())
                .status(BookingStatus.PENDING)
                .build();
        bookingServic.saveBooking(booking);
        return ResponseEntity.ok("Booking request sent");
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<?> getAll(@PathVariable String rideId) {
        String userId=SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userService.findById(userId);
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user");
        }
        List<Booking> bookings=bookingRepo.findByRideId(rideId);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{rideId}/{bookingId}/CONFIRMED")
    public ResponseEntity<?> confirmBooking(
            @PathVariable String rideId,
            @PathVariable String bookingId
    ) {

        User driver = userService.findById(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Ride ride = rideService.getRide(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        if (!driver.getId().equals(ride.getDriver().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Booking booking = bookingServic.findBooking(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getSeatsBooked() > ride.getAvailableSeats()) {
            return ResponseEntity.badRequest().body("Not enough seats");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        ride.setAvailableSeats(
                ride.getAvailableSeats() - booking.getSeatsBooked()
        );
        bookingServic.saveBooking(booking);
        rideService.saveRide(ride);
        return ResponseEntity.ok("Booking confirmed");
    }

    @PutMapping("/{rideId}/{bookingId}/CANCELLED")
    public ResponseEntity<?> cancelBooking(
            @PathVariable String rideId,
            @PathVariable String bookingId
    ) {
        User driver = userService.findById(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Ride ride = rideService.getRide(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        if (!driver.getId().equals(ride.getDriver().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Booking booking = bookingServic.findBooking(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        bookingServic.saveBooking(booking);
        return ResponseEntity.ok("Booking cancelled");
    }
}
