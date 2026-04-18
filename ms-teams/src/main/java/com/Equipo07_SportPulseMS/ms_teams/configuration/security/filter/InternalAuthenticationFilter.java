package com.Equipo07_SportPulseMS.ms_teams.configuration.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
public class InternalAuthenticationFilter extends OncePerRequestFilter {

    @Value("${internal-secret}")
    private String internalSecret;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String xInternalSecret = request.getHeader("X-Internal-Secret");
        log.info("Received request with X-Internal-Secret: {}", xInternalSecret);

        if (internalSecret.equals(xInternalSecret)) {
            log.info("Internal secret matched. Setting authentication for internal service.");
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    "internal",
                    null,
                    Collections.emptyList()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
