package com.Equipo07_SportPulseMS.ms_dashboard.controller;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.dashboard.DashboardResponse;
import com.Equipo07_SportPulseMS.ms_dashboard.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Validated
@Tag(
        name = "Dashboard",
        description = "Agregación de datos deportivos desde múltiples microservicios"
)
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(
            summary = "Obtener dashboard principal",
            description = """
                    Retorna un resumen completo del día para una liga específica:
                    
                    - Información de la liga
                    - Partidos del día (fixtures)
                    - Tabla de posiciones (standings)
                    - Top scorers (API externa)
                    - Servicios fallidos (si aplica)
                    
                    ⚠️ Requiere JWT válido.
                    ⚠️ Top scorers depende de API externa con API Key interna.
                    """
    )
    public Mono<DashboardResponse> getDashboard(
            @RequestParam @NotNull Integer league,
            @RequestParam @NotNull Integer season
    ) {
        return dashboardService.getDashboard(league, season);
    }
}