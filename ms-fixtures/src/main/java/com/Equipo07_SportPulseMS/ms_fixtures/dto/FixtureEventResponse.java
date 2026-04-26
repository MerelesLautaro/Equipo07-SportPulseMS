package com.Equipo07_SportPulseMS.ms_fixtures.dto;

public record FixtureEventResponse(
        Integer elapsed,
        String type,
        String detail,
        Team team,
        EventPlayer player,
        EventAssist assist
) {

    public static FixtureEventResponse fromEvent(Event event) {
        return new FixtureEventResponse(
                event.time().elapsed(),
                event.type(),
                event.detail(),
                Team.create(event.team().id(), event.team().name()),
                new EventPlayer(event.player().id(), event.player().name()),
                new EventAssist(event.assist().id(), event.assist().name())
        );
    }
}
