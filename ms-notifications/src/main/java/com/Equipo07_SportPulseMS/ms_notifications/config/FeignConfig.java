package com.Equipo07_SportPulseMS.ms_notifications.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class FeignConfig {

    @Value("${internal.secret}")
    private String internalSecret;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {

            Authentication auth = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (auth != null && auth.getCredentials() != null) {
                String token = auth.getCredentials().toString();
                template.header("Authorization", "Bearer " + token);
            } else {
                template.header("X-Internal-Secret", internalSecret);
            }
        };
    }
}