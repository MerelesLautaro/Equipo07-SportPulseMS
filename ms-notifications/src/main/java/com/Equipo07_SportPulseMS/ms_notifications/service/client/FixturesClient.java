package com.Equipo07_SportPulseMS.ms_notifications.service.client;

import com.Equipo07_SportPulseMS.ms_notifications.dto.response.fixture.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.fixture.FixtureResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "fixtures-client",
        url = "${fixtures.service.url}"
)
public interface FixturesClient {

    @GetMapping("/api/fixtures/live")
    List<FixtureResponse> getLiveFixtures();

    @GetMapping("/api/fixtures/{fixtureId}/events")
    List<FixtureEventResponse> getFixtureEvents(
            @PathVariable Integer fixtureId
    );

    @GetMapping("/api/fixtures")
    List<FixtureResponse> getFixturesByTeam(
            @RequestParam Integer team,
            @RequestParam String status
    );
}