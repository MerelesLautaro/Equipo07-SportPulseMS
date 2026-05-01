package com.Equipo07_SportPulseMS.ms_fixtures.controller;

import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureEventResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureFilters;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureLiveResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.dto.FixtureResponse;
import com.Equipo07_SportPulseMS.ms_fixtures.service.FixtureService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fixtures")
@RequiredArgsConstructor
    @Tag(name = "Fixtures", description = "Operaciones para consultar fixtures, partidos en vivo y eventos del partido")
public class FixtureController {

    private final FixtureService fixtureService;

    @Operation(
            summary = "Lista fixtures mediante filtros",
            description = "Retorna una lista de fixtures. Mediante filtros como league, team, date y status.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Requiere cabecera con JWT token con el formato: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de fixtures obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FixtureResponse.class)),
                            examples = @ExampleObject(
                                    name = "Success response",
                                    value = """
                                            [
                                              {
                                                "id": 1508400,
                                                "date": "2026-04-27T00:00:00+00:00",
                                                "status": {
                                                  "short": "1H",
                                                  "long": "First Half"
                                                },
                                                "league": {
                                                  "id": 254,
                                                  "name": "NWSL Women",
                                                  "round": "Group Stage"
                                                },
                                                "homeTeam": {
                                                  "id": 3002,
                                                  "name": "Seattle Reign FC",
                                                  "logo": "https://media.api-sports.io/football/teams/3002.png",
                                                  "goals": 0
                                                },
                                                "awayTeam": {
                                                  "id": 3004,
                                                  "name": "Utah Royals W",
                                                  "logo": "https://media.api-sports.io/football/teams/3004.png",
                                                  "goals": 2
                                                },
                                                "venue": {
                                                  "name": "Lumen Field",
                                                  "city": "Seattle"
                                                }
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetros de consulta inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.Equipo07_SportPulseMS.ms_fixtures.exception.ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Error de filtro inválido",
                                    value = """
                                            {
                                              "error": "REQUEST_PARAMETER_INVALID",
                                              "message": "Formato de fecha inválido. El formato esperado es YYYY-MM-DD.",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - token de autenticación faltante o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Respuesta Forbidden",
                                    value = ""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error inesperado",
                                    value = """
                                            {
                                              "error": "INTERNAL_SERVER_ERROR",
                                               "message": "Ocurrió un error inesperado",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<FixtureResponse>> getAllBy(
            @Parameter(description = "Identificador de league.", example = "254")
            @RequestParam(required = false) Integer league,
            @Parameter(description = "Identificador de team.", example = "3002")
            @RequestParam(required = false) Integer team,
            @Parameter(description = "Fecha del fixture en formato YYYY-MM-DD.", example = "2026-04-27")
            @RequestParam(required = false) String date,
            @Parameter(description = "Status del fixture: NS, LIVE, o FT.", example = "LIVE")
            @RequestParam(required = false) String status) {

        var filters = FixtureFilters.fromRequestParams(league, team, date, status);
        var response = fixtureService.filterFixtureBy(filters);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Listar fixtures en vivo",
            description = "Retorna partidos en vivo con minuto transcurrido y marcador parcial.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Requiere header con JWT token en el formato: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fixtures en vivo obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FixtureLiveResponse.class)),
                            examples = @ExampleObject(
                                    name = "Respuesta exitosa",
                                    value = """
                                            [
                                              {
                                                "id": 1508400,
                                                "elapsed": 16,
                                                "status": {
                                                  "short": "1H",
                                                  "long": "First Half"
                                                },
                                                "league": {
                                                  "id": 254,
                                                  "name": "NWSL Women"
                                                },
                                                "homeTeam": {
                                                  "id": 3002,
                                                  "name": "Seattle Reign FC",
                                                  "goals": 0
                                                },
                                                "awayTeam": {
                                                  "id": 3004,
                                                  "name": "Utah Royals W",
                                                  "goals": 2
                                                }
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - token de autenticación faltante o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Respuesta Forbidden",
                                    value = ""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error inesperado",
                                    value = """
                                            {
                                              "error": "INTERNAL_SERVER_ERROR",
                                               "message": "Ocurrió un error inesperado",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            )

    })
    @GetMapping("/live")
    public ResponseEntity<List<FixtureLiveResponse>> getByInLive() {
        var response = fixtureService.getFixtureInLive();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener eventos del fixture",
            description = "Retorna eventos de un partido específico (goles, tarjetas, sustituciones, etc.).",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Requiere header con JWT token en el formato: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Eventos del fixture obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FixtureEventResponse.class)),
                            examples = @ExampleObject(
                                    name = "Respuesta exitosa",
                                    value = """
                                            [
                                              {
                                                "elapsed": 2,
                                                "type": "Goal",
                                                "detail": "Normal Goal",
                                                "team": {
                                                  "id": 3004,
                                                  "name": "Utah Royals W"
                                                },
                                                "player": {
                                                  "id": 102272,
                                                  "name": "P. Cronin"
                                                },
                                                "assist": {
                                                  "id": 301790,
                                                  "name": "M. Tanaka"
                                                }
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Fixture no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.Equipo07_SportPulseMS.ms_fixtures.exception.ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Fixture no encontrado",
                                    value = """
                                            {
                                              "error": "FIXTURE_NOT_FOUND",
                                               "message": "No se encontró el fixture con id 1",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - token de autenticación faltante o inválido",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Respuesta Forbidden",
                                    value = ""
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Error inesperado",
                                    value = """
                                            {
                                              "error": "INTERNAL_SERVER_ERROR",
                                               "message": "Ocurrió un error inesperado",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{fixtureId}/events")
    public ResponseEntity<List<FixtureEventResponse>> getById(
            @Parameter(description = "Identificador de fixture a consultar.", example = "1508400")
            @PathVariable int fixtureId) {
        var response = fixtureService.getFixtureById(fixtureId);
        return ResponseEntity.ok(response);
    }
}
