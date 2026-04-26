package com.Equipo07_SportPulseMS.ms_fixtures.api;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ms-auth", url = "http://ms-gateway:8080")
public interface AuthClient {

    @PostMapping(
            value = "/api/auth/validate",
            headers = "X-Internal-Secret=${internal-secret}")
    TokenValidationResponse validateToken(@RequestHeader("Authorization") String token);
}
