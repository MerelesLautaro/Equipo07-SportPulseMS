package com.Equipo07_SportPulseMS.ms_standings.client;

import com.Equipo07_SportPulseMS.ms_standings.client.dto.ApiSportsStandingsEnvelope;
import com.Equipo07_SportPulseMS.ms_standings.config.ApiSportsFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "apiSportsStandingsClient",
        url = "${apisports.base-url}",
        configuration = ApiSportsFeignConfig.class
)
public interface ApiSportsStandingsClient {

    @GetMapping("/standings")
    ApiSportsStandingsEnvelope getStandings(
            @RequestParam("league") Integer league,
            @RequestParam("season") Integer season
    );
}
