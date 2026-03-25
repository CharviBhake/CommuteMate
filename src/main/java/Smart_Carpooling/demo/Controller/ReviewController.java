package Smart_Carpooling.demo.Controller;

import Smart_Carpooling.demo.Entity.Review;
import Smart_Carpooling.demo.Entity.Ride;
import Smart_Carpooling.demo.Entity.User;
import Smart_Carpooling.demo.Repository.ReviewRepository;
import Smart_Carpooling.demo.Repository.RideRepository;
import Smart_Carpooling.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/review")
public class ReviewController {
    @Autowired
    private UserService userService;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RideRepository rideRepository;
    @PostMapping
    public ResponseEntity<?> addReview(@RequestBody Map<String,Object> body){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        User user=userService.findById(email);

        String rideId=(String) body.get("rideId");
        int rating= (int)body.get("rating");
        String comment= (String) body.get("comment");
        if(reviewRepository.existsByReviewerIdAndRideId(user.getId(),rideId)){
            return ResponseEntity.badRequest().body("Already reviewed");
        }
        Ride ride=rideRepository.findById(rideId).orElseThrow();
        String driverId=ride.getDriver().getId();
        if(driverId.equals(user.getId())){
            return ResponseEntity.badRequest().body("cannot rate yoursel");
        }
        Review review=new Review();
        review.setReviewerId(review.getId());
        review.setRevieweeId(driverId);
        review.setRideId(rideId);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        return ResponseEntity.ok("Review added");
    }

    @GetMapping("/rating/{userId}")
    public double getRating(@PathVariable String userId){
        List<Review> reviews=reviewRepository.findByRevieweeId(userId);
        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0);
    }

    @GetMapping("/my")
    public Map<String,Integer> getMyReviews(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        User user=userService.findById(email);

        List<Review> reviews=reviewRepository.findByReviewerId(user.getId());
        Map<String,Integer> map=new HashMap<>();
        for(Review r:reviews){
            map.put(r.getRideId(),r.getRating());
        }
        return map;
    }
}
