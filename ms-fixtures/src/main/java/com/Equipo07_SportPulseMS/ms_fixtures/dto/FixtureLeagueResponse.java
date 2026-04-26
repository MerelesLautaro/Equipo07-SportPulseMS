package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FixtureLeagueResponse(
        Integer id,
        String name,
        String round
) {
    public static FixtureLeagueResponse fromLeague(League league) {
        if (league == null) {
            return null;
        }

        return new FixtureLeagueResponse(league.id(), league.name(), league.round());
    }

    public static FixtureLeagueResponse fromLeagueWithoutRound(League league) {
        if (league == null) return null;

        return new FixtureLeagueResponse(league.id(), league.name(), null);
    }
}
