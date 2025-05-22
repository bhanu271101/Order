package com.example.order.Authorization;

import java.security.Key;
import java.util.Base64;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Value; 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtToken {


    @Value("${jwt.secret}")
    private String SECRET;

    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String extractUserId(String token)
    {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token)
    {
        
        return Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token)
    {
        try{
            getClaims(token);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
