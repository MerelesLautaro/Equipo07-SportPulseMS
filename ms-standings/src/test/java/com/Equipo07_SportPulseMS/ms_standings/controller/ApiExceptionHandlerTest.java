package com.Equipo07_SportPulseMS.ms_standings.controller;

import com.Equipo07_SportPulseMS.ms_standings.exception.StandingsNotFoundException;
import com.Equipo07_SportPulseMS.ms_standings.exception.TeamStandingNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void standingsNotFoundMapsTo404AndExpectedErrorCode() {
        var response = handler.handleStandingsNotFound(new StandingsNotFoundException("No existe tabla para league/season"));

        assertEquals(404, response.getStatusCode().value());
        assertEquals("STANDINGS_NOT_FOUND", response.getBody().get("error"));
    }

    @Test
    void teamStandingNotFoundMapsTo404AndExpectedErrorCode() {
        var response = handler.handleTeamStandingNotFound(
                new TeamStandingNotFoundException("El equipo no participa en la liga o temporada proporcionada")
        );

        assertEquals(404, response.getStatusCode().value());
        assertEquals("TEAM_STANDING_NOT_FOUND", response.getBody().get("error"));
    }
}
