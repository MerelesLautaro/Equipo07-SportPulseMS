package com.Equipo07_SportPulseMS.ms_teams.controller;

import com.Equipo07_SportPulseMS.ms_teams.dto.TeamResponse;
import com.Equipo07_SportPulseMS.ms_teams.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamResponse>> get(
            @RequestParam int league,
            @RequestParam int season
    ) {
        var response = teamService.getByLeagueAndSeason(league, season);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getById(@PathVariable int teamId) {
        log.info("Recibiendo solicitud GET /api/teams/{}", teamId);
        var response = teamService.getById(teamId);
        return ResponseEntity.ok(response);
    }
}
