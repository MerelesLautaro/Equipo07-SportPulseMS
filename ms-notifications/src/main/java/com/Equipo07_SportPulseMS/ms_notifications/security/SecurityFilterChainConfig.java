package com.Equipo07_SportPulseMS.ms_notifications.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final InternalAuthFilter internalAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/actuator/health",
                                "/api/fixtures/**",
                                "/api/notifications/swagger-ui.html",
                                "/api/notifications/swagger-ui/**",
                                "/api/notifications/api-docs/**",
                                "/api/notifications/openapi.yaml"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(internalAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}