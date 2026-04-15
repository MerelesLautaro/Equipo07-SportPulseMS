package com.Equipo07_SportPulseMS.ms_leagues.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JwtAuthenticationFilterTest {

    private static final String SECRET = "default-jwt-secret-key-default-jwt-secret-key-12345";

    @Test
    void missingBearerTokenReturnsUnauthorized() throws ServletException, IOException {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new JwtService(SECRET));
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/leagues");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
    }

    @Test
    void validBearerTokenAllowsRequest() throws ServletException, IOException {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(new JwtService(SECRET));
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/leagues");
        request.addHeader("Authorization", "Bearer " + buildToken());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
    }

    private String buildToken() {
        return Jwts.builder()
                .subject("user-1")
                .claim("username", "test-user")
                .claim("role", "USER")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(Keys.hmacShaKeyFor(deriveKey(SECRET)), SignatureAlgorithm.HS256)
                .compact();
    }

    private byte[] deriveKey(String secret) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(secret.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
