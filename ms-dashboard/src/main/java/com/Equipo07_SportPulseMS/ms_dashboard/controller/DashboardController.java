package com.Equipo07_SportPulseMS.ms_dashboard.controller;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.dashboard.DashboardResponse;
import com.Equipo07_SportPulseMS.ms_dashboard.service.dashboard.DashboardService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public Mono<DashboardResponse> getDashboard(
            @RequestParam @NotNull Integer league,
            @RequestParam @NotNull Integer season
    ) {
        return dashboardService.getDashboard(league, season);
    }
}