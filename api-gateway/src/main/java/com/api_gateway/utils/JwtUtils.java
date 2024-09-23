package com.api_gateway.utils;

import java.security.Key;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {


    private String secretKey = "10927c79d47a054a5bf13304904370dbe338f4cf252f7557194649942043b9e2";

    private Key getSigninKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Check Toke is Valid or not
     * 
     * @param token
     * @param userDetails
     * @return
     */
    public void validateToken(String token) {
        Key key = getSigninKey(secretKey);
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        Key key = getSigninKey(secretKey); // Ensure the key is used here as well
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


}
