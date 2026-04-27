package com.Equipo07_SportPulseMS.ms_standings.security;

public record TokenValidationResponse(
        Boolean valid,
        String userId,
        String username,
        String role
) {
}
