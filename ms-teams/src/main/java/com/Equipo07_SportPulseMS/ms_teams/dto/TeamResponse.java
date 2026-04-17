package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public final class TeamResponse {

    private Integer id;
    private String name;
    private String country;
    private String logo;
    private Integer founded;

    /**
     * Este campo es opcional únicamente para respuesta en el endpoint de teams
     * de recuperación por ligas y temporada, esto se hace para no aumentar la complejidad
     * con @JsonView en todos los campos o crear una segunda clase
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean national;

    private StadiumResponse stadium;
}