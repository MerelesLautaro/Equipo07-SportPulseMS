package com.Equipo07_SportPulseMS.ms_auth.service.authentication;

import com.Equipo07_SportPulseMS.ms_auth.entity.Role;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;
import java.util.function.Function;

public interface TokenService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    long getExpirationTime();

    UUID extractUserId(String token);

    Role extractRole(String token);

    void validateToken(String token);
}
