package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Venue(
        Integer id,
        String name,
        String address,
        String city,
        Integer capacity,
        String surface,
        String image
) {
}
