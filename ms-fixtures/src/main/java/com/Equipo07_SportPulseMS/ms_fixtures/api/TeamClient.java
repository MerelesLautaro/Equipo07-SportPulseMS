package com.Equipo07_SportPulseMS.ms_fixtures.api;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.TeamResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-teams", url = "http://ms-gateway:8080/api/teams")
public interface TeamClient {

    @GetMapping(headers = "X-Internal-Secret=${internal-secret}")
    List<TeamResponse> getTeamsByLeagueAndSeason(@RequestParam int league, @RequestParam int season);

    @GetMapping(value = "/{teamId}", headers = "X-Internal-Secret=${internal-secret}")
    TeamResponse getTeamById(@PathVariable int teamId);
}
