package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team.TeamDTO;

public record StandingEntryDTO(
        Integer rank,
        TeamDTO team,
        Integer points,
        Integer played
) {}