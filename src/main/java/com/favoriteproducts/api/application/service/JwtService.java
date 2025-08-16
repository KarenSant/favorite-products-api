package com.favoriteproducts.api.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.expirationSeconds}")
    private long expSec;

    private Key key;

    @PostConstruct
    void init() {
        if (secret.length() < 32) {
            throw new IllegalArgumentException("A chave JWT deve ter pelo menos 32 caracteres.");
        }
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generate(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expSec)))
                .signWith(key)
                .compact();
    }

    public String validateAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}