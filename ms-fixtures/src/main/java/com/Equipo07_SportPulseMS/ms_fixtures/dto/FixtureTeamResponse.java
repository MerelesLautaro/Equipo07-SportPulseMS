package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FixtureTeamResponse(
        Integer id,
        String name,
        String logo,
        Integer goals
) {
    public static FixtureTeamResponse fromTeam(Team team, Integer goals) {
        if (team == null) {
            return null;
        }

        return new FixtureTeamResponse(team.id(), team.name(), team.logo(), goals);
    }

    public static FixtureTeamResponse fromTeamWithoutLogo(Team team, Integer goals) {
        if (team == null) return null;
        return new FixtureTeamResponse(team.id(), team.name(), null, goals);
    }
}
