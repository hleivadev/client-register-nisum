package com.nisum.test_jwt.client_register.infrastructure.adapter.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.nisum.test_jwt.client_register.config.JwtProperties;

@Component
public class JwtUtil {
    private final Key key;
    private final long expiration;

    public JwtUtil(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.expiration = jwtProperties.getExpiration();
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
    
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @PostConstruct
    public void init() {
        System.out.println("[JwtUtil] JWT Secret Key Loaded: " + (key != null));
        System.out.println("[JwtUtil] JWT Expiration Loaded: " + expiration);
    }
    
}
