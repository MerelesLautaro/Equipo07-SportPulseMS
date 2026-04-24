package com.Equipo07_SportPulseMS.ms_standings.controller;

import com.Equipo07_SportPulseMS.ms_standings.dto.StandingsResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.TeamStandingResponse;
import com.Equipo07_SportPulseMS.ms_standings.service.StandingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/standings")
public class StandingsController {

    private final StandingsService standingsService;

    public StandingsController(StandingsService standingsService) {
        this.standingsService = standingsService;
    }

    @GetMapping
    @Operation(summary = "Obtener clasificacion completa por liga y temporada")
    public StandingsResponse getStandings(
            @RequestParam @NotNull Integer league,
            @RequestParam @NotNull Integer season
    ) {
        return standingsService.getStandings(league, season);
    }

    @GetMapping("/team/{teamId}")
    @Operation(summary = "Obtener posicion de un equipo en la clasificacion")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posicion del equipo encontrada"),
            @ApiResponse(responseCode = "401", description = "Token JWT invalido o ausente"),
            @ApiResponse(responseCode = "404", description = "El equipo no participa en la liga o temporada"),
            @ApiResponse(responseCode = "503", description = "Servicio de autenticacion no disponible")
    })
    public TeamStandingResponse getTeamStanding(
            @Parameter(description = "ID del equipo", required = true)
            @PathVariable @NotNull Integer teamId,
            @Parameter(description = "ID de la liga", required = true)
            @RequestParam @NotNull Integer league,
            @Parameter(description = "Temporada", required = true)
            @RequestParam @NotNull Integer season
    ) {
        return standingsService.getTeamStanding(teamId, league, season);
    }
}
