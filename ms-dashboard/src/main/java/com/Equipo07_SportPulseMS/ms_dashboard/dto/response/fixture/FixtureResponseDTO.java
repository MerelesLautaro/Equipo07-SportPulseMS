package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.fixture;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team.StatusDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team.TeamDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league.LeagueDTO;

public record FixtureResponseDTO(
        Integer id,
        String date,
        StatusDTO status,
        LeagueDTO league,
        TeamDTO homeTeam,
        TeamDTO awayTeam
) {}