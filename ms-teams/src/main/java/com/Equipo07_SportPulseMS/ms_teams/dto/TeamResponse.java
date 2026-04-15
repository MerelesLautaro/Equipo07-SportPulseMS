package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TeamResponse(
        @JsonProperty("get") String endpoint,
        Parameters parameters,
        List<String> errors,
        int results,
        Paging paging,
        List<TeamItem> response
){
}
