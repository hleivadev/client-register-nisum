package com.nisum.test_jwt.client_register.infrastructure.adapter.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.nisum.test_jwt.client_register.config.JwtProperties;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    // The secret key used for signing the JWT tokens. || Secreto utilizado para firmar los tokens JWT.
    private final Key key;
    // The expiration time for the JWT tokens in milliseconds. || Expiracion del token en milisegundos.
    private final long expiration;

    // Constructor that initializes the key and expiration time. || Constructor que inicializa la clave y el tiempo de expiracion.
    public JwtUtil(JwtProperties jwtProperties) {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.expiration = jwtProperties.getExpiration();
    }

    // Method to generate a JWT token for a given email. || Metodo para generar un token JWT para un email dado.
    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
    
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256) // algorithm used for signing the token using SHA-256 || algoritmo utilizado para firmar el token con SHA-256
                .compact();
    }
    // Extrae el email (subject) desde un token JWT
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Valida que el token sea correcto y no esté expirado
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
}