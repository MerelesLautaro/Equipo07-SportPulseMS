package com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication;

import com.Equipo07_SportPulseMS.ms_auth.util.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRegisterRequest(

        @NotBlank(message = "Username is required")
        @Pattern(
                regexp = "^\\S+$",
                message = "Username must not contain spaces"
        )
        String username,

        @Password
        String password,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email

) {}
