package Smart_Carpooling.demo.Service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class JwtBlacklistService {

    private final RedisTemplate<String,Object> redisTemplate;
    public JwtBlacklistService(RedisTemplate<String,Object> redisTemplate){
        this.redisTemplate=redisTemplate;
    }
    public void blacklistToken(String token,long ttlMillis){
        String key=buildKey(token);
        redisTemplate.opsForValue().set(key,"BLACKLISTED",ttlMillis, TimeUnit.MILLISECONDS);
    }
    public boolean isBlacklisted(String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(buildKey(token)));
    }
    private String buildKey(String token){
        return "blacklist:"+token;
    }
}
