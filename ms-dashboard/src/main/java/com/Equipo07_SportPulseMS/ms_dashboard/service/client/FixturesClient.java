package com.Equipo07_SportPulseMS.ms_dashboard.service.client;

import com.Equipo07_SportPulseMS.ms_dashboard.config.ResilienceFactory;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.fixture.FixtureResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FixturesClient {

    private final WebClient fixturesWebClient;
    private final ResilienceFactory resilienceFactory;

    public Mono<List<FixtureResponseDTO>> getFixtures(Integer league, String date) {
        return fixturesWebClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/fixtures");

                    if (league != null) uriBuilder.queryParam("league", league);
                    if (date != null) uriBuilder.queryParam("date", date);

                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(FixtureResponseDTO.class)
                .collectList()
                .transform(resilienceFactory.decorate());
    }
}