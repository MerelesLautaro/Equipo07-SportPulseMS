package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends ApiException {

    public TokenExpiredException() {
        super(
                "TOKEN_EXPIRED",
                "El token ha expirado",
                HttpStatus.UNAUTHORIZED
        );
    }

}
