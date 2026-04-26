package com.Equipo07_SportPulseMS.ms_fixtures.advice;

import com.Equipo07_SportPulseMS.ms_fixtures.exception.ApiErrorResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.exception.FixtureNotFound;
import com.Equipo07_SportPulseMS.ms_fixtures.exception.InvalidFixtureFilterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class FixtureExceptionHandler {

    @ExceptionHandler(InvalidFixtureFilterException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidFixtureFilter(InvalidFixtureFilterException ex) {
        var response = new ApiErrorResponse(
                "REQUEST_PARAMETER_INVALID",
                ex.getMessage(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(FixtureNotFound.class)
    public ResponseEntity<ApiErrorResponse> handleFixtureNotFound(FixtureNotFound ex) {
        var response = new ApiErrorResponse(
                "FIXTURE_NOT_FOUND",
                ex.getMessage(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex) {
        var response = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "An unexpected error occurred",
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
