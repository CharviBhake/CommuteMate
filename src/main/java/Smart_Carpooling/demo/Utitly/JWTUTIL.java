package Smart_Carpooling.demo.Utitly;

import lombok.Data;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public String extractusername(String token){
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
    public String generateToken(String username){
        Map<String,Object> claims=new HashMap<>();
        return createToken(claims,username);
    }
    private String createToken(Map<String,Object> claims,String subject){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*3000))
                .signWith(getSigningKey())
                .compact();
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractusername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }
}
