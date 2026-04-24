package com.Equipo07_SportPulseMS.ms_notifications.service.authentication;

import io.jsonwebtoken.Claims;

public interface TokenService {
    Claims extractAllClaims(String token);
}
