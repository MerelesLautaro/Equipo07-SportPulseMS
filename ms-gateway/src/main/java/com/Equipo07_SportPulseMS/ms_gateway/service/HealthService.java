package com.Equipo07_SportPulseMS.ms_gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HealthService {

    private final WebClient webClient;

    @Value("${services.auth}")
    private String authUrl;

    @Value("${services.leagues}")
    private String leaguesUrl;

    @Value("${services.teams}")
    private String teamsUrl;

    @Value("${services.fixtures}")
    private String fixturesUrl;

    @Value("${services.standings}")
    private String standingsUrl;

    @Value("${services.notifications}")
    private String notificationsUrl;

    @Value("${services.dashboard}")
    private String dashboardUrl;

    public Mono<Map<String, String>> getServicesHealth() {

        return Mono.zip(
                check(authUrl),
                check(leaguesUrl),
                check(teamsUrl),
                check(fixturesUrl),
                check(standingsUrl),
                check(notificationsUrl),
                check(dashboardUrl)
        ).map(tuple -> {
            Map<String, String> result = new HashMap<>();
            result.put("ms-auth", tuple.getT1());
            result.put("ms-leagues", tuple.getT2());
            result.put("ms-teams", tuple.getT3());
            result.put("ms-fixtures", tuple.getT4());
            result.put("ms-standings", tuple.getT5());
            result.put("ms-notifications", tuple.getT6());
            result.put("ms-dashboard", tuple.getT7());
            return result;
        });
    }

    private Mono<String> check(String baseUrl) {
        return webClient.get()
                .uri(baseUrl + "/actuator/health")
                .retrieve()
                .bodyToMono(String.class)
                .map(res -> "UP")
                .timeout(Duration.ofSeconds(2))
                .onErrorReturn("DOWN");
    }
}
