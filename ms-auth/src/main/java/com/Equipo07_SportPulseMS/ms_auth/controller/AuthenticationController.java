package com.Equipo07_SportPulseMS.ms_auth.controller;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
