package com.Equipo07_SportPulseMS.ms_fixtures.controller;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureFilters;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureLiveResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.service.FixtureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fixtures")
@RequiredArgsConstructor
public class FixtureController {

    private final FixtureService fixtureService;

    @GetMapping
    public ResponseEntity<List<FixtureResponse>> getAllBy(
            @RequestParam(required = false) Integer league,
            @RequestParam(required = false) Integer team,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status) {

        var filters = FixtureFilters.fromRequestParams(league, team, date, status);
        var response = fixtureService.filterFixtureBy(filters);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/live")
    public ResponseEntity<List<FixtureLiveResponse>> getByInLive() {
        var response = fixtureService.getFixtureInLive();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{fixtureId}/events")
    public ResponseEntity<List<FixtureEventResponse>> getById(@PathVariable int fixtureId) {
        var response = fixtureService.getFixtureById(fixtureId);
        return ResponseEntity.ok(response);
    }
}
