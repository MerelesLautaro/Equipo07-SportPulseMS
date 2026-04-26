package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Event(
        EventTime time,
        EventTeam team,
        EventPlayer player,
        EventAssist assist,
        String type,
        String detail,
        String comments
) {
}
