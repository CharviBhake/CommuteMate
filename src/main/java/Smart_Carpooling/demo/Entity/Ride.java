package Smart_Carpooling.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.loader.internal.LoadAccessContext;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection="Ride")
public class Ride {
    @Id
    private String id;
    @DBRef
    @JsonIgnoreProperties("rideList")
    private User driver;
    private String startlocation;
    private String endLocation;
    private LocalDate rideDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;
    private int availableSeats;
    private double pricePerSeat;
    private RideStatus status;
    private int totalSeats;
    private List<Booking> bookings=new ArrayList<>();

    private double startLatitude;
    private double startLongitude;

    private double endLatitude;
    private double endLongitude;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private Point startPoint;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private Point endPoint;

}
