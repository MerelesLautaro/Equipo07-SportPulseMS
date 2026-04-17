package com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication;

import com.Equipo07_SportPulseMS.ms_auth.entity.Role;

import java.util.UUID;

public record TokenValidationResponse(UUID userId,
                                      String username,
                                      Role role,
                                      boolean valid) {
}
