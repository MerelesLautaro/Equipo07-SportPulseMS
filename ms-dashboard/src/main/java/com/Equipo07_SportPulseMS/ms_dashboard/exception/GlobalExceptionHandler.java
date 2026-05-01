package com.Equipo07_SportPulseMS.ms_dashboard.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, Object> buildResponse(String error, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);
        response.put("timestamp", Instant.now());
        return response;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        return new ResponseEntity<>(
                buildResponse(
                        ex.getErrorCode(),
                        ex.getMessage(),
                        ex.getStatus()
                ),
                ex.getStatus()
        );
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<Map<String, Object>> handleWebInputException(ServerWebInputException ex) {

        String paramName = extractParamName(ex.getReason());

        return new ResponseEntity<>(
                buildResponse(
                        "MISSING_QUERY_PARAM",
                        "Query parameter '" + paramName + "' is required",
                        HttpStatus.BAD_REQUEST
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    private String extractParamName(String reason) {

        if (reason == null) return "unknown";

        int start = reason.indexOf('\'');
        int end = reason.indexOf('\'', start + 1);

        if (start != -1 && end != -1) {
            return reason.substring(start + 1, end);
        }

        return "unknown";
    }
}