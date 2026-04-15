package com.Equipo07_SportPulseMS.ms_leagues.client;

import com.Equipo07_SportPulseMS.ms_leagues.client.dto.ApiSportsLeaguesEnvelope;
import com.Equipo07_SportPulseMS.ms_leagues.config.ApiSportsFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "apiSportsLeaguesClient",
        url = "${apisports.base-url}",
        configuration = ApiSportsFeignConfig.class
)
public interface ApiSportsLeaguesClient {

    @GetMapping("/leagues")
    ApiSportsLeaguesEnvelope getLeagues(
            @RequestParam(name = "id", required = false) Integer leagueId,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "season", required = false) Integer season
    );
}
