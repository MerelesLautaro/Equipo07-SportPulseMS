package com.Equipo07_SportPulseMS.ms_fixtures.configuration.security;

import com.Equipo07_SportPulseMS.ms_fixtures.configuration.security.filter.InternalAuthenticationFilter;
import com.Equipo07_SportPulseMS.ms_fixtures.configuration.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final InternalAuthenticationFilter internalAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(
                            "/api/fixtures/swagger-ui.html",
                            "/api/fixtures/swagger-ui*/**",
                            "/api/fixtures/api-docs*/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/fixtures/**").authenticated();
                    http.requestMatchers("/actuator/health").permitAll();
                    http.anyRequest().denyAll();
                })
                .addFilterBefore(internalAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
