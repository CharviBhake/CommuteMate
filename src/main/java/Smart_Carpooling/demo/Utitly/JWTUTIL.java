package Smart_Carpooling.demo.Utitly;

import Smart_Carpooling.demo.Service.CustomUserDetails;
import Smart_Carpooling.demo.Service.UserDetailsServiceImpl;
import lombok.Data;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JWTUTIL {
    private String SECRET_KEY="Tal+HaV^uvCHEFsEVfpW#7g9^k*Z8$V!";
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String extractId(String token){
        Claims claim=extractAllClaims(token);
        return claim.getSubject();
    }
    public Date extractExpiration(String token){ return extractAllClaims(token).getExpiration();}
    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private boolean isTokenExpired(String token){ return extractExpiration(token).before(new Date());}
    public String generateToken(String userId){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,userId);
    }
    private String createToken(Map<String,Object> claims,String subject){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+10000*30000))
                .signWith(getSigningKey())
                .compact();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userId = extractUserId(token);
        return userId.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }
    public long getRemainingTime(String token){
        Date expration=extractExpiration(token);
        return expration.getTime()-System.currentTimeMillis();
    }
}
