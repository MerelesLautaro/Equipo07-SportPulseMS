package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FixtureLiveResponse(
        Integer id,
        Integer elapsed,
        FixtureStatusResponse status,
        FixtureLeagueResponse league,
        FixtureTeamResponse homeTeam,
        FixtureTeamResponse awayTeam
) {

    public static FixtureLiveResponse fromFixtureItem(FixtureItem item) {
        if (item == null) return null;

        var fixture = item.fixture();
        var status = item.fixture().status();
        var league = item.league();
        var teams = item.teams();

        return new FixtureLiveResponse(
                fixture.id(),
                fixture.status().elapsed(),
                FixtureStatusResponse.fromStatus(status),
                FixtureLeagueResponse.fromLeagueWithoutRound(league),
                FixtureTeamResponse.fromTeamWithoutLogo(teams.home(), item.goals() != null ? item.goals().home() : null),
                FixtureTeamResponse.fromTeamWithoutLogo(teams.away(), item.goals() != null ? item.goals().away() : null)
        );
    }
}
