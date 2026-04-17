package com.Equipo07_SportPulseMS.ms_auth.service.authentication.impl;

import com.Equipo07_SportPulseMS.ms_auth.entity.Role;
import com.Equipo07_SportPulseMS.ms_auth.exception.InvalidTokenException;
import com.Equipo07_SportPulseMS.ms_auth.exception.TokenExpiredException;
import com.Equipo07_SportPulseMS.ms_auth.security.userdetails.CustomUserDetails;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public String generateToken(UserDetails userDetails) {

        CustomUserDetails custom = (CustomUserDetails) userDetails;

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", custom.getUser().getId());
        claims.put("role", custom.getUser().getRole().name());

        return buildToken(claims, custom, expiration);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public long getExpirationTime() {
        return expiration;
    }

    @Override
    public UUID extractUserId(String token) {
        try {
            String userId = extractClaim(token, claims -> claims.get("userId", String.class));
            return UUID.fromString(userId);
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    @Override
    public Role extractRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role", String.class));
        return Role.valueOf(role);
    }

    @Override
    public void validateToken(String token) {
        try {
            extractAllClaims(token);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new TokenExpiredException();
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // email como subject
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}