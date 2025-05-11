package com.RicipeWeb.recetas.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private SecretKey secretKey;

    private final long EXPIRATION_MS = 3600000; // 1 hora

    @PostConstruct
    public void init() {
        // Genera una clave secreta una vez (mejor usar una fija en producción)
        this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    /**
     * Genera un token JWT con claims personalizados
     */
    public String generateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // "typ": "JWT"
                .compact();
    }

    /**
     * Genera un token JWT sin claims personalizados
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of(), userDetails.getUsername());
    }

    /**
     * Extrae el "subject" (usualmente el email) del token
     */
    public String extractUsername(String token) {
        return getAllClaims(token).getSubject();
    }

    /**
     * Extrae el claim "role", si existe
     */
    public String extractRole(String token) {
        Object role = getAllClaims(token).get("role");
        return role != null ? role.toString() : null;
    }

    /**
     * Verifica si el token es válido para el usuario
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Verifica si el token está expirado
     */
    private boolean isTokenExpired(String token) {
        Date expiration = getAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    /**
     * Obtiene todos los claims del token
     */
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}