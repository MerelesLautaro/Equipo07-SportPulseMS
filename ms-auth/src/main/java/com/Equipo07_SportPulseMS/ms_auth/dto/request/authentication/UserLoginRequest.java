package com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(@NotBlank @Email(message = "Invalid email format") String email,
                               @NotBlank String password) {
}
