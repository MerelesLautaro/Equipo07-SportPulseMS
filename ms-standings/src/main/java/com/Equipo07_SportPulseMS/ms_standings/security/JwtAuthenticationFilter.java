package com.Equipo07_SportPulseMS.ms_standings.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthValidationService authValidationService;

    public JwtAuthenticationFilter(AuthValidationService authValidationService) {
        this.authValidationService = authValidationService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/api/standings/swagger-ui")
                || path.startsWith("/api/standings/api-docs")
                || path.equals("/api/standings/swagger-ui.html")
                || path.startsWith("/actuator");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!path.startsWith("/api/standings")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            unauthorized(response, "UNAUTHORIZED", "Token JWT requerido");
            return;
        }

        try {
            TokenValidationResponse validation = authValidationService.validate(authorization);
            String username = validation.username();
            String role = validation.role();

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    username != null ? username : "authenticated-user",
                    null,
                    role != null ? List.of(new SimpleGrantedAuthority("ROLE_" + role)) : List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (InvalidTokenException ex) {
            unauthorized(response, "INVALID_TOKEN", "Token JWT invalido o expirado");
        } catch (AuthServiceUnavailableException ex) {
            serviceUnavailable(response, "AUTH_SERVICE_UNAVAILABLE", "No se pudo validar el token con ms-auth");
        }
    }

    private void unauthorized(HttpServletResponse response, String error, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"error\":\"" + error + "\",\"message\":\"" + message + "\",\"timestamp\":\"" + Instant.now() + "\"}"
        );
    }

    private void serviceUnavailable(HttpServletResponse response, String error, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"error\":\"" + error + "\",\"message\":\"" + message + "\",\"timestamp\":\"" + Instant.now() + "\"}"
        );
    }
}
