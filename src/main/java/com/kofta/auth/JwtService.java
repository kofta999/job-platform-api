package com.kofta.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${auth.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateToken(UserDetails userDetails) {
        var now = Instant.now();
        return Jwts.builder()
            .subject(userDetails.getUsername())
            .expiration(Date.from(now.plus(1, ChronoUnit.DAYS)))
            .issuedAt(Date.from(now))
            .claim(
                "roles",
                userDetails
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList()
            )
            .signWith(getSigningKey())
            .compact();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String extractUsername(Claims claims) {
        return claims.getSubject();
    }

    public boolean isTokenValid(final Claims claims, UserDetails userDetails) {
        String username = claims.getSubject();
        boolean isExpired = claims.getExpiration().before(new Date());

        return (username.equals(userDetails.getUsername()) && !isExpired);
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
