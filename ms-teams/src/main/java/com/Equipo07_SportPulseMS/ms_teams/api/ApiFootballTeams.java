package com.Equipo07_SportPulseMS.ms_teams.api;

import com.Equipo07_SportPulseMS.ms_teams.dto.TeamResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "api-football", url = "https://v3.football.api-sports.io/teams")
public interface ApiFootballTeams {

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    TeamResponse findByLeagueAndSeason(@RequestParam int league, @RequestParam int season);

    @GetMapping(headers = "x-apisports-key=${api-sports.key}")
    TeamResponse findById(@RequestParam int id);
}
