package com.Equipo07_SportPulseMS.ms_dashboard.security;

import com.Equipo07_SportPulseMS.ms_dashboard.service.authentication.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationWebFilter implements WebFilter {

    private final TokenService jwtService;

    @Value("${jwt.prefix:Bearer }")
    private String prefix;

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(prefix)) {
            return chain.filter(exchange);
        }

        try {
            String token = authHeader.substring(prefix.length());

            Claims claims = jwtService.extractAllClaims(token);

            String userIdStr = claims.get("userId", String.class);
            String role = claims.get("role", String.class);

            UUID userId = UUID.fromString(userIdStr);

            List<SimpleGrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userId,
                            token,
                            authorities
                    );

            return chain.filter(exchange)
                    .contextWrite(
                            ReactiveSecurityContextHolder.withAuthentication(authentication)
                    );

        } catch (JwtException | IllegalArgumentException e) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}