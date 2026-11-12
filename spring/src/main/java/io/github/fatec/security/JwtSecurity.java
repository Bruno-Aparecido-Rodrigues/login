package io.github.fatec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtSecurity {
    private static final long EXP = 2L * 600 * 1000;

    private final byte[] secret;

    public JwtSecurity(@Value("${app.jwt.secret}") String secret) {
        byte[] bytes = secret.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET precisa ter pelo menos 32 caracteres");
        }
        this.secret = bytes;
    }

    private byte[] getSecret() {
        return secret;
    }

    public long getExpirationSeconds() {
        return EXP / 1000;
    }

    public String generateToken(UserDetails user) {
        Date date = new Date();
        SecretKey key = Keys.hmacShaKeyFor(getSecret());
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .issuedAt(date)
                .expiration(new Date(date.getTime() + EXP))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails user) {
        String username = getUsername(token);
        return username.equals(user.getUsername()) && !isExpired(token);
    }

    private Claims parseClaims(String token) {
        SecretKey secretKey = Keys.hmacShaKeyFor(getSecret());
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}