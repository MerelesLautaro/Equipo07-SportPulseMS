package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public final class StadiumResponse {

    private String name;
    private String address;
    private String city;
    private Integer capacity;
    private String surface;


}