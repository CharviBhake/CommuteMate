package Smart_Carpooling.demo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="reviews")
public class Review {
    @Id
    private String id;
    private String reviewerId;
    private String revieweeId;
    private String rideId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
