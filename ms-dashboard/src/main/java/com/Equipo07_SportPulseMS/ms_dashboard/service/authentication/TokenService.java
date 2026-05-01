package com.Equipo07_SportPulseMS.ms_dashboard.service.authentication;

import io.jsonwebtoken.Claims;

public interface TokenService {
    Claims extractAllClaims(String token);
}