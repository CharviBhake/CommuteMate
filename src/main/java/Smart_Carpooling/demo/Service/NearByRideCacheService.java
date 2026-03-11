package Smart_Carpooling.demo.Service;


import Smart_Carpooling.demo.Entity.Ride;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NearByRideCacheService {
    private final RedisTemplate<String,Object> redisTemplate;
    private static final long TTL_MINUTES=5;
    public String buildKey(double lat, double lng, double radius){
        return String.format("nearby:%.4f:%4f:%.0f",lat,lng,radius);
    }
    public List<Ride> get(String key){
        Object cached=redisTemplate.opsForValue().get(key);
        if(cached==null) return null;
        return (List<Ride>) cached;
    }
    public void set(String key,List<Ride> rides){
    redisTemplate.opsForValue().set(key,rides,TTL_MINUTES, TimeUnit.MINUTES);
    }
}
