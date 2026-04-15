package com.Equipo07_SportPulseMS.ms_leagues.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiSportsFeignConfig {

    @Bean
    RequestInterceptor apiSportsKeyInterceptor(@Value("${apisports.key}") String apiSportsKey) {
        return template -> template.header("x-apisports-key", apiSportsKey);
    }
}
