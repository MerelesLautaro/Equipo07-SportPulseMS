package com.Equipo07_SportPulseMS.ms_notifications.controller;

import com.Equipo07_SportPulseMS.ms_notifications.dto.response.fixture.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_notifications.dto.response.fixture.FixtureResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/fixtures")
public class MockFixturesController {

    private static int counter = 0;

    @GetMapping("/live")
    public List<FixtureResponse> getLiveFixtures() {

        return List.of(
                new FixtureResponse(
                        1001,
                        "LIVE"
                )
        );
    }

    @GetMapping("/{fixtureId}/events")
    public List<FixtureEventResponse> getEvents(@PathVariable Integer fixtureId) {

        counter++;

        List<FixtureEventResponse> events = new ArrayList<>();

        // Evento 1 siempre
        events.add(new FixtureEventResponse(10, "Goal", "Normal Goal"));

        // Evento 2 aparece después
        if (counter > 1) {
            events.add(new FixtureEventResponse(55, "Card", "Yellow Card"));
        }

        // Evento 3 aparece después
        if (counter > 2) {
            events.add(new FixtureEventResponse(80, "Goal", "Penalty"));
        }

        return events;
    }

    @GetMapping
    public List<FixtureResponse> getFixturesByTeam(
            @RequestParam Integer team,
            @RequestParam String status
    ) {
        return List.of(
                new FixtureResponse(1001, "LIVE")
        );
    }
}