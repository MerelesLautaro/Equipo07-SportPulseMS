package com.Equipo07_SportPulseMS.ms_dashboard.service.client;

import com.Equipo07_SportPulseMS.ms_dashboard.config.ResilienceFactory;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league.LeagueDetailResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LeagueClient {

    private final WebClient leaguesWebClient;
    private final ResilienceFactory resilienceFactory;

    public Mono<LeagueDetailResponseDTO> getLeagueById(Integer leagueId) {
        return leaguesWebClient.get()
                .uri("/api/leagues/{id}", leagueId)
                .retrieve()
                .bodyToMono(LeagueDetailResponseDTO.class)
                .transform(resilienceFactory.decorate());
    }
}