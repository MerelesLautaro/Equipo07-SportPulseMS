package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatusDTO(
        @JsonProperty("short") String shortStatus
) {}