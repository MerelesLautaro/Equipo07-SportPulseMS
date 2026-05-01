package com.Equipo07_SportPulseMS.ms_leagues.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthValidationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AuthValidationService service;

    @Test
    void validateThrowsWhenAuthReturnsUnauthorized() {
        service = new AuthValidationService(restTemplate, "http://ms-auth:8081", "internal-secret");
        when(restTemplate.exchange(eq("http://ms-auth:8081/api/auth/validate"), any(), any(), eq(TokenValidationResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));

        assertThrows(InvalidTokenException.class, () -> service.validate("Bearer bad-token"));
    }

    @Test
    void validateThrowsWhenAuthUnavailable() {
        service = new AuthValidationService(restTemplate, "http://ms-auth:8081", "internal-secret");
        when(restTemplate.exchange(eq("http://ms-auth:8081/api/auth/validate"), any(), any(), eq(TokenValidationResponse.class)))
                .thenThrow(new RestClientException("connection refused"));

        assertThrows(AuthServiceUnavailableException.class, () -> service.validate("Bearer token"));
    }
}
