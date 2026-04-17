package com.Equipo07_SportPulseMS.ms_teams.api;

import com.Equipo07_SportPulseMS.ms_teams.dto.TeamApiSportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-football", url = "https://v3.football.api-sports.io/teams")
public interface TeamClient {

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    TeamApiSportResponse findByLeagueAndSeason(@RequestParam int league, @RequestParam int season);

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    TeamApiSportResponse findById(@RequestParam int id);
}
