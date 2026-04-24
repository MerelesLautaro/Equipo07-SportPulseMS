package com.Equipo07_SportPulseMS.ms_standings.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthValidationService {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;
    private final String internalSecret;

    public AuthValidationService(
            RestTemplate restTemplate,
            @Value("${auth.service.url}") String authServiceUrl,
            @Value("${internal.secret}") String internalSecret
    ) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
        this.internalSecret = internalSecret;
    }

    public TokenValidationResponse validate(String authorizationHeader) {
        String url = authServiceUrl + "/api/auth/validate";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorizationHeader);
        headers.set("X-Internal-Secret", internalSecret);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    TokenValidationResponse.class
            );

            TokenValidationResponse body = response.getBody();
            if (body == null || !Boolean.TRUE.equals(body.valid())) {
                throw new InvalidTokenException();
            }

            return body;
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().value() == 401) {
                throw new InvalidTokenException();
            }
            throw new AuthServiceUnavailableException();
        } catch (RestClientException ex) {
            throw new AuthServiceUnavailableException();
        }
    }
}
