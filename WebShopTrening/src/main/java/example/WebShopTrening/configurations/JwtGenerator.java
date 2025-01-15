package example.WebShopTrening.configurations;

import java.time.Instant;
import java.util.Date;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
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
            System.out.println("Validating token: " + token);
            System.out.println("Using secret: " + SecurityConstants.JWT_SECRET);
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            System.out.println("Token is valid");
            return true;
        } catch (Exception e) {
        	System.out.println("Token validation failed: " + e.getMessage());
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }
}