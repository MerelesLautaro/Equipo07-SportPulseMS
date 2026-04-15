package com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication;

import java.util.UUID;

public record UserLoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        UUID userId
) {}