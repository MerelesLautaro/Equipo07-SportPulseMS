package com.Equipo07_SportPulseMS.ms_auth.security;

import com.Equipo07_SportPulseMS.ms_auth.exception.InvalidInternalSecretException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class InternalAuthFilter extends OncePerRequestFilter {

    @Value("${internal.secret}")
    private String internalSecret;

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // SOLO proteger /validate
        if (!"/api/auth/validate".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String headerSecret = request.getHeader("X-Internal-Secret");

        if (!internalSecret.equals(headerSecret)) {
            handlerExceptionResolver.resolveException(
                    request,
                    response,
                    null,
                    new InvalidInternalSecretException()
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
