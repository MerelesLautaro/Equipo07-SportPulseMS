package com.Equipo07_SportPulseMS.ms_gateway.controller;

import com.Equipo07_SportPulseMS.ms_gateway.dto.HealthResponse;
import com.Equipo07_SportPulseMS.ms_gateway.service.HealthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
public class HealthController {

    private final HealthService healthService;

    @GetMapping("/health")
    public Mono<HealthResponse> health() {

        return healthService.getServicesHealth()
                .map(services -> new HealthResponse(
                        "UP",
                        Instant.now(),
                        services
                ));
    }
}