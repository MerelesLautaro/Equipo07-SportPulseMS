package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidInternalSecretException extends ApiException {

    public InvalidInternalSecretException() {
        super(
                "INVALID_INTERNAL_SECRET",
                "Token interno invalido",
                HttpStatus.UNAUTHORIZED
        );
    }
}
