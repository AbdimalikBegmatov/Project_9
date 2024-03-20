package com.example.project_9.Services.Impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.lifetime}")
    private long lifetime;
    @Value("${jwt.expiration-refresh}")
    private long refreshExpiration;

    public String generateToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,lifetime);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return buildToken(new HashMap<>(),userDetails,refreshExpiration);
    }

    private String buildToken(Map<String, Objects> extraClaims, UserDetails user,long expiration){

        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(secretKey())
                .compact();
    }

    public boolean isTokenValid(String token,UserDetails userDetails){
        String username = getUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token){
        return getAllClaims(token).getExpiration().before(new Date());
    }
    public String getUserName(String token){
        return getAllClaims(token).getSubject();
    }

    public Claims getAllClaims(String token){

        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private SecretKey secretKey() {
        byte[] bytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(bytes);
    }
}
