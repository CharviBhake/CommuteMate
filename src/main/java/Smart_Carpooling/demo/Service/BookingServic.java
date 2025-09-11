package Smart_Carpooling.demo.Service;

import Smart_Carpooling.demo.Entity.Booking;
import Smart_Carpooling.demo.Repository.BookingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingServic {
    @Autowired
    private BookingRepo bookingRepo;
    public void saveBooking(Booking booking){
        bookingRepo.save(booking);
    }
    public Optional<Booking> findBooking(String bookingId){
        Optional<Booking> list=bookingRepo.findById(bookingId);
        return list;
    }
}
