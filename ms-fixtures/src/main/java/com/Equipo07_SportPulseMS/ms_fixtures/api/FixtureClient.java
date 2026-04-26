package com.Equipo07_SportPulseMS.ms_fixtures.api;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureApiResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureEvent;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureItem;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "api-sport-football-fixture", url = "https://v3.football.api-sports.io/fixtures")
public interface FixtureClient {

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    FixtureApiResponse<FixtureItem> getFixturesByFilters(
            @RequestParam(required = false) Integer league,
            @RequestParam(required = false) Integer team,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String status,
            // Parámetro requerido por la api de football si se
            // pasa un valor para league o team
            @RequestParam(required = false) Integer season
    );

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    FixtureApiResponse<FixtureItem> getFixtureInLive(@RequestParam String live);

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    FixtureApiResponse<FixtureEvent> getFixtureById(@RequestParam int id);
}