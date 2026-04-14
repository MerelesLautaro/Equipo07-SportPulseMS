package com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication;

import com.Equipo07_SportPulseMS.ms_auth.entity.Role;

import java.time.Instant;
import java.util.UUID;

public record UserDetailsResponse(UUID id,
                                  String username,
                                  String email,
                                  Role role,
                                  Instant createdAt) {
}
