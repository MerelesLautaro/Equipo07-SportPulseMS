package com.Equipo07_SportPulseMS.ms_gateway.dto;

import java.time.Instant;
import java.util.Map;

public record HealthResponse(
        String gateway,
        Instant timestamp,
        Map<String, String> services
) {}