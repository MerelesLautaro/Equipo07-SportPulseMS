package com.Equipo07_SportPulseMS.ms_standings.controller;

import com.Equipo07_SportPulseMS.ms_standings.dto.StandingsResponse;
import com.Equipo07_SportPulseMS.ms_standings.service.StandingsService;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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
    public StandingsResponse getStandings(
            @RequestParam @NotNull Integer league,
            @RequestParam @NotNull Integer season
    ) {
        return standingsService.getStandings(league, season);
    }
}
