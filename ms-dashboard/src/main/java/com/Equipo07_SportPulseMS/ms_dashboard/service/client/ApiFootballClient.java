package com.Equipo07_SportPulseMS.ms_dashboard.service.client;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.topscore.ApiTopScorerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ApiFootballClient {

    private final WebClient apiFootballWebClient;

    @Value("${APISPORTS_KEY}")
    private String apiKey;

    public Mono<ApiTopScorerResponse> getTopScorers(Integer league, Integer season) {
        return apiFootballWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/players/topscorers")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .header("x-apisports-key", apiKey)
                .retrieve()
                .bodyToMono(ApiTopScorerResponse.class);
    }
}