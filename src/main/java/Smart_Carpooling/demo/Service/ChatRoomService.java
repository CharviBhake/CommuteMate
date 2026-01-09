package Smart_Carpooling.demo.Service;

import Smart_Carpooling.demo.Entity.Booking;
import Smart_Carpooling.demo.Entity.BookingStatus;
import Smart_Carpooling.demo.Entity.ChatRoom;
import Smart_Carpooling.demo.Entity.User;
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
    public List<User> getUsersInRide(String rideId) {
        List<Booking> bookings =
                bookingRepo.findByRideIdAndStatus(
                        rideId, BookingStatus.CONFIRMED);
        List<String> userIds = bookings.stream()
                .map(b -> b.getPassenger().getId())
                .toList();

        return userRepository.findByIdIn(userIds);
    }
    public boolean canUserChat(String rideId, String userId) {
        return bookingRepo.existsByRide_IdAndPassenger_IdAndStatus(
                rideId, userId, BookingStatus.CONFIRMED
        );
    }
    public Optional<ChatRoom> getChatRoomForRide(String rideId) {
        return chatRoomRepository.findByRideId(rideId);
    }


}
