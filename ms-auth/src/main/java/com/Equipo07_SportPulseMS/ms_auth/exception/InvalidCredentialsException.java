package com.Equipo07_SportPulseMS.ms_auth.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {

  public InvalidCredentialsException() {
    super(
            "INVALID_CREDENTIALS",
            "Email o contraseña incorrectos",
            HttpStatus.UNAUTHORIZED
    );
  }
}
