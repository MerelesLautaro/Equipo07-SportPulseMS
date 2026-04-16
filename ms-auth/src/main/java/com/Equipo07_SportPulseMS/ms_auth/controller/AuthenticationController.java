package com.Equipo07_SportPulseMS.ms_auth.controller;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserLoginRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.TokenValidationResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserLoginResponse;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> registerUser(
            @RequestBody @Valid UserRegisterRequest request) {

        UserDetailsResponse response = authenticationService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid
                                                   UserLoginRequest loginRequest) {

        return ResponseEntity.ok(authenticationService.login(loginRequest));
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        return ResponseEntity.ok(authenticationService.validateToken(token));
    }
}
