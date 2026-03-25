package Smart_Carpooling.demo.Service;

import Smart_Carpooling.demo.Entity.*;
import Smart_Carpooling.demo.Repository.BookingRepo;
import Smart_Carpooling.demo.Repository.ChatRoomRepository;
import Smart_Carpooling.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    @Autowired
    private BookingRepo bookingRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private final ChatRoomRepository chatRoomRepository;
    @Autowired
    private final RideService rideService;
    @Autowired
    private final UserService userService;

    public ChatRoom getOrCreateChatRoom(String rideId) {
        return chatRoomRepository.findByRideId(rideId)
                .orElseGet(() ->
                        chatRoomRepository.save(
                                ChatRoom.builder()
                                        .rideId(rideId)
                                        .build()
                        )
                );
    }
  /*  public List<User> getUsersInRide(String rideId) {
        List<Booking> bookings =
                bookingRepo.findByRideIdAndStatus(
                        rideId, BookingStatus.CONFIRMED);
        List<String> userIds = bookings.stream()
                .map(b -> b.getPassenger().getId())
                .toList();

        return userRepository.findByIdIn(userIds);
    } */
    public boolean canUserChat(String rideId, String userId) {
        Optional<Ride> ride=rideService.getRide(rideId);
        Ride ride1=ride.orElse(null);
        boolean a=false;
        User user=userService.findById(userId);
        System.out.println("userId is"+ userId+" and ride driver id "+ride1.getDriver().getId() );
        if(ride1.getDriver().getId().equals(userId)){
            System.out.println("true");
            a=true;
        }
        return (bookingRepo.existsByRideIdAndPassengerIdAndStatus(
                rideId, userId, BookingStatus.CONFIRMED)|| a
        );
    }
    public Optional<ChatRoom> getChatRoomForRide(String rideId) {
        return chatRoomRepository.findByRideId(rideId);
    }


}
