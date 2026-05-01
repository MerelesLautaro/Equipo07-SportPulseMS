package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league.LeagueInfoDTO;

import java.util.List;

public record StandingsResponseDTO(
        LeagueInfoDTO league,
        List<StandingEntryDTO> standings
) {}