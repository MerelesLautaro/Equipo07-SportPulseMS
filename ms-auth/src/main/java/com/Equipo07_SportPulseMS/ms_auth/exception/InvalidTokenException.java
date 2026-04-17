package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {

    public InvalidTokenException() {
        super(
                "INVALID_TOKEN",
                "El token es inválido",
                HttpStatus.UNAUTHORIZED
        );
    }
}
