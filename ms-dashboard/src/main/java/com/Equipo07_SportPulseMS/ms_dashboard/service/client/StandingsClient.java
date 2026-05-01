package com.Equipo07_SportPulseMS.ms_dashboard.service.client;

import com.Equipo07_SportPulseMS.ms_dashboard.config.ResilienceFactory;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing.StandingsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StandingsClient {

    private final WebClient standingsWebClient;
    private final ResilienceFactory resilienceFactory;

    // standing no responde con seasons fuera del 22 al 24 debido a las limitaciones del plan gratuito
    public Mono<StandingsResponseDTO> getStandings(Integer league, Integer season) {
        return standingsWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/standings")
                        .queryParam("league", league)
                        .queryParam("season", season)
                        .build())
                .retrieve()
                .bodyToMono(StandingsResponseDTO.class)
                .transform(resilienceFactory.decorate());
    }
}