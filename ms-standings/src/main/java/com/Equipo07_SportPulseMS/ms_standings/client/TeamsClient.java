package com.Equipo07_SportPulseMS.ms_standings.client;

import com.Equipo07_SportPulseMS.ms_standings.client.dto.TeamDetailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msTeamsClient", url = "${teams.service.url}")
public interface TeamsClient {

    @GetMapping("/api/teams/{teamId}")
    TeamDetailResponse getTeam(@PathVariable Integer teamId);
}
