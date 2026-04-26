package com.Equipo07_SportPulseMS.ms_fixtures.dto;

public record FixtureResponse(
        Integer id,
        String date,
        FixtureStatusResponse status,
        FixtureLeagueResponse league,
        FixtureTeamResponse homeTeam,
        FixtureTeamResponse awayTeam,
        FixtureVenueResponse venue
) {

    public static FixtureResponse fromFixtureItem(FixtureItem item) {
        if (item == null) return null;

        var fixture = item.fixture();
        var status = item.fixture().status();
        var league = item.league();
        var teams = item.teams();
        var venue = item.fixture().venue();

        return new FixtureResponse(
                fixture.id(),
                fixture.date(),
                FixtureStatusResponse.fromStatus(status),
                FixtureLeagueResponse.fromLeague(league),
                FixtureTeamResponse.fromTeam(teams.home(), item.goals() != null ? item.goals().home() : null),
                FixtureTeamResponse.fromTeam(teams.away(), item.goals() != null ? item.goals().away() : null),
                FixtureVenueResponse.fromVenue(venue)
        );
    }
}
