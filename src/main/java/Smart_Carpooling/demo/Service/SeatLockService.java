package Smart_Carpooling.demo.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeatLockService {
    private final RedisTemplate<String,Object> redisTemplate;
    private static final long LOCK_TTL_SECONDS=120;
    private String key(String rideId,String userId){
        return "seatlock:"+rideId+":"+userId;
    }
    public boolean lockSeats(String rideId,String userId,int seats){
        String key=key(rideId,userId);
        Boolean success=redisTemplate.opsForValue().setIfAbsent(key,seats,LOCK_TTL_SECONDS, TimeUnit.SECONDS);
        return Boolean.TRUE.equals(success);
    }
    public void releaseLock(String rideId,String userId){
        redisTemplate.delete(key(rideId,userId));
    }
    public int totalLockedSeats(String rideId){
        Set<String> keys=redisTemplate.keys("seatlock:"+rideId+":*");
        if(keys==null) return 0;
        int sum=0;
        for(String key:keys){
            Integer seats=(Integer) redisTemplate.opsForValue().get(key);
            if(seats!=null) sum+=seats;
        }
        return sum;
    }

}
