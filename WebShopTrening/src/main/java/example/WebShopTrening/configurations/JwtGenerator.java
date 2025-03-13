package example.WebShopTrening.configurations;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import example.WebShopTrening.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

@Component
public class JwtGenerator {
    
    private SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes());
    
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Instant expirationInstant = Instant.now().plusMillis(SecurityConstants.JWT_EXPERATION);
        Date expirationDate = Date.from(expirationInstant);

        String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(expirationDate)
            .signWith(key)
            .compact();
        
        return token;
    }

    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject();
    }

    public boolean validToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            
            // Check if token is expired
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}