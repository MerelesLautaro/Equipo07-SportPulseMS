package com.Equipo07_SportPulseMS.ms_leagues.controller;

import com.Equipo07_SportPulseMS.ms_leagues.dto.LeagueDetailResponse;
import com.Equipo07_SportPulseMS.ms_leagues.dto.LeagueSummaryResponse;
import com.Equipo07_SportPulseMS.ms_leagues.service.LeagueService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leagues")
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping
    public List<LeagueSummaryResponse> getLeagues(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Integer season
    ) {
        return leagueService.getLeagues(country, season);
    }

    @GetMapping("/{leagueId}")
    public LeagueDetailResponse getLeagueById(@PathVariable Integer leagueId) {
        return leagueService.getLeagueById(leagueId);
    }
}
