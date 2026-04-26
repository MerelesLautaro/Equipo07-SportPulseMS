package com.Equipo07_SportPulseMS.ms_fixtures.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Team(
        Integer id,
        String name,
        String logo,
        Boolean winner
) {
    public static Team create(Integer id, String name) {
        return new Team(id, name, null, null);
    }
}
