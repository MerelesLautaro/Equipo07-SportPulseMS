package com.Equipo07_SportPulseMS.ms_gateway.controller;

import com.Equipo07_SportPulseMS.ms_gateway.dto.HealthResponse;
import com.Equipo07_SportPulseMS.ms_gateway.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequiredArgsConstructor
@Tag(name = "Health", description = "Monitoreo del gateway y microservicios")
public class HealthController {

    private final HealthService healthService;

    @Operation(
            summary = "Estado del sistema",
            description = "Endpoint público que retorna el estado del gateway y de todos los microservicios downstream"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sistema operativo correctamente"),
            @ApiResponse(responseCode = "500", description = "Error al obtener el estado de los servicios")
    })
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