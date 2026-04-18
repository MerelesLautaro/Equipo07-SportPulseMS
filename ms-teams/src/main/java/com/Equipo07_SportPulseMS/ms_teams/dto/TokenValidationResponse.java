package com.Equipo07_SportPulseMS.ms_teams.dto;

public record TokenValidationResponse(
        String username,
        boolean valid
) {
}