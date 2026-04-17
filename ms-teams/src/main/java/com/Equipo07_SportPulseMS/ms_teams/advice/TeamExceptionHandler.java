package com.Equipo07_SportPulseMS.ms_teams.advice;

import com.Equipo07_SportPulseMS.ms_teams.dto.ErrorResponse;
import com.Equipo07_SportPulseMS.ms_teams.exception.TeamNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;

@RestControllerAdvice
public class TeamExceptionHandler {

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFound(TeamNotFoundException exception) {
        var errorResponse = new ErrorResponse(
                "TEAM_NOT_FOUND",
                "No existe un equipo con el ID proporcionado",
                LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
