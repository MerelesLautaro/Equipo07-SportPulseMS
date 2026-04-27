package com.Equipo07_SportPulseMS.ms_auth.controller;

import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserLoginRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.request.authentication.UserRegisterRequest;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.TokenValidationResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserDetailsResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.UserLoginResponse;
import com.Equipo07_SportPulseMS.ms_auth.dto.response.authentication.ErrorResponse;
import com.Equipo07_SportPulseMS.ms_auth.service.authentication.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Registro, login y validación de tokens JWT")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Registrar usuario")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDetailsResponse.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Usuario ya existente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "error": "USER_ALREADY_EXISTS",
                              "message": "Ya existe un usuario con ese email",
                              "timestamp": "2026-04-27T08:36:30.992Z"
                            }
                            """)
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> registerUser(
            @RequestBody @Valid UserRegisterRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authenticationService.registerUser(request));
    }

    @Operation(summary = "Login de usuario")
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Login exitoso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserLoginResponse.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Credenciales inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                            {
                              "error": "INVALID_CREDENTIALS",
                              "message": "Email o contraseña incorrectos",
                              "timestamp": "2026-04-27T08:36:30.992Z"
                            }
                            """)
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @Operation(
            summary = "Validar token JWT",
            security = {
                    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "internalAuth")
            }
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Token válido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenValidationResponse.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "401",
                    description = "Token inválido o expirado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(value = """
                                    {
                                      "error": "INVALID_TOKEN",
                                      "message": "El token es inválido",
                                      "timestamp": "2026-04-27T08:36:30.992Z"
                                    }
                                    """),
                                    @ExampleObject(value = """
                                    {
                                      "error": "TOKEN_EXPIRED",
                                      "message": "El token ha expirado",
                                      "timestamp": "2026-04-27T08:36:30.992Z"
                                    }
                                    """)
                            }
                    )
            )
    })
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        return ResponseEntity.ok(authenticationService.validateToken(token));
    }
}