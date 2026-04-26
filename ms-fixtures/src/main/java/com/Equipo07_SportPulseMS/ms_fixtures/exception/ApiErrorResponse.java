package com.Equipo07_SportPulseMS.ms_fixtures.exception;

import java.time.Instant;

public record ApiErrorResponse(
        String error,
        String message,
        Instant timestampt
) {
}
