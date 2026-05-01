package com.Equipo07_SportPulseMS.ms_teams.controller;

import com.Equipo07_SportPulseMS.ms_teams.dto.TeamResponse;
import com.Equipo07_SportPulseMS.ms_teams.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Equipos", description = "Operaciones relacionadas con equipos de fútbol")
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    @Operation(
            summary = "Listar equipos por liga y temporada",
            description = "Lista equipos filtrados por liga y temporada. Body: no aplica.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Require cabecera con JWT token con el formato: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Listado de equipos",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = TeamResponse.class)),
                            examples = @ExampleObject(
                                    name = "Ejemplo",
                                    value = "[\n  {\n    \"id\": 33,\n    \"name\": \"Manchester United\",\n    \"country\": \"England\",\n    \"logo\": \"https://media.api-sports.io/football/teams/33.png\",\n    \"founded\": 1878,\n    \"stadium\": {\n      \"name\": \"Old Trafford\",\n      \"city\": \"Manchester\",\n      \"capacity\": 76212\n    }\n  },\n  {\n    \"id\": 34,\n    \"name\": \"Newcastle\",\n    \"country\": \"England\",\n    \"logo\": \"https://media.api-sports.io/football/teams/34.png\",\n    \"founded\": 1892,\n    \"stadium\": {\n      \"name\": \"St. James' Park\",\n      \"city\": \"Newcastle upon Tyne\",\n      \"capacity\": 52758\n    }\n  }\n]"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros inválidos o faltantes",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Invalid parameters",
                                    value = ""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado (Authorization: Bearer <token> requerido)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Unauthorized response",
                                    value = ""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - missing or invalid authentication token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Forbidden response",
                                    value = ""
                            )
                    )
            ),
    })
    public ResponseEntity<List<TeamResponse>> get(
            @Parameter(description = "ID de la liga", example = "39", required = true)
            @RequestParam int league,
            @Parameter(description = "Temporada", example = "2023", required = true)
            @RequestParam int season
    ) {
        var response = teamService.getByLeagueAndSeason(league, season);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{teamId}")
    @Operation(
            summary = "Detalle de equipo por ID",
            description = "Obtiene el detalle de un equipo por su ID. Body: no aplica.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Require cabecera con JWT token con el formato: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Detalle del equipo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TeamResponse.class),
                            examples = @ExampleObject(
                                    name = "Ejemplo",
                                    value = "{\n  \"id\": 4,\n  \"name\": \"Russia\",\n  \"country\": \"Russia\",\n  \"logo\": \"https://media.api-sports.io/football/teams/4.png\",\n  \"founded\": 1912,\n  \"national\": true,\n  \"stadium\": {\n    \"name\": \"Stadion Luzhniki\",\n    \"city\": \"Moskva\",\n    \"capacity\": 81000\n  }\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipo no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "TEAM_NOT_FOUND",
                                    value = "{\n  \"error\": \"TEAM_NOT_FOUND\",\n  \"message\": \"No existe un equipo con el ID proporcionado\",\n  \"timestamp\": \"2026-04-28T02:36:14.65053758\"\n}"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - missing or invalid authentication token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Forbidden response",
                                    value = ""
                            )
                    )
            ),
    })
    public ResponseEntity<TeamResponse> getById(@PathVariable int teamId) {
        var response = teamService.getById(teamId);
        return ResponseEntity.ok(response);
    }
}
