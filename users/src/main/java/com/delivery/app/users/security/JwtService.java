package com.delivery.app.users.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    public String getUserName(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
       final Claims claims = extractAllClaims(token);
       return claimsResolver.apply(claims); //extracts a specific claim and returns it a generic T
    }
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>() , userDetails);
    }

    public boolean isTokenValid(String token , UserDetails userDetails){
        final String username = getUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token , Claims::getExpiration);
    }


    public String generateToken(
            Map<String , Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 24)
                )
                .signWith(getSignInKey() , SignatureAlgorithm.HS384)
                .compact();

    }

    //create, parse, and validate JWT tokens.
    private Claims extractAllClaims(String token){ // represents the claims of a JWT token
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token) //parses the provided JWT token into a Jws<Claims>(holds the signed JWT and its corresponding header and signature.)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
