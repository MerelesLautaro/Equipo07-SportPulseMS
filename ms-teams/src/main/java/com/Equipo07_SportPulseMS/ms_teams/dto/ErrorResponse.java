package com.Equipo07_SportPulseMS.ms_teams.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record ErrorResponse(
        String error,
        String message,
        LocalDateTime timestamp
) {
}
