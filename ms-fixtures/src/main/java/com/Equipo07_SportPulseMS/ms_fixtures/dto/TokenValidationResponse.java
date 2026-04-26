package com.Equipo07_SportPulseMS.ms_fixtures.dto;

public record TokenValidationResponse(
        String username,
        boolean valid
) {
}