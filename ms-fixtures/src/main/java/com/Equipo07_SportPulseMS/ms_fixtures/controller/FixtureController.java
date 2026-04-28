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
@Tag(name = "Fixtures", description = "Operations to query fixtures, live matches, and match events")
public class FixtureController {

    private final FixtureService fixtureService;

    @Operation(
            summary = "List fixtures with filters",
            description = "Returns a list of fixtures. Supports filtering by league, team, date, and status.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Required header with JWT token in the format: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fixture list retrieved successfully",
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
                    description = "Invalid query parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.Equipo07_SportPulseMS.ms_fixtures.exception.ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Invalid filter error",
                                    value = """
                                            {
                                              "error": "REQUEST_PARAMETER_INVALID",
                                              "message": "Invalid date format. Expected format is YYYY-MM-DD.",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
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
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Unexpected error",
                                    value = """
                                            {
                                              "error": "INTERNAL_SERVER_ERROR",
                                              "message": "An unexpected error occurred",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<FixtureResponse>> getAllBy(
            @Parameter(description = "League identifier.", example = "254")
            @RequestParam(required = false) Integer league,
            @Parameter(description = "Team identifier.", example = "3002")
            @RequestParam(required = false) Integer team,
            @Parameter(description = "Fixture date in YYYY-MM-DD format.", example = "2026-04-27")
            @RequestParam(required = false) String date,
            @Parameter(description = "Fixture status: NS, LIVE, or FT.", example = "LIVE")
            @RequestParam(required = false) String status) {

        var filters = FixtureFilters.fromRequestParams(league, team, date, status);
        var response = fixtureService.filterFixtureBy(filters);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "List live fixtures",
            description = "Returns live matches with elapsed minute and partial score.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Required header with JWT token in the format: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Live fixtures retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FixtureLiveResponse.class)),
                            examples = @ExampleObject(
                                    name = "Success response",
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
                    description = "Forbidden - missing or invalid authentication token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Forbidden response",
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
                                    name = "Unexpected error",
                                    value = """
                                            {
                                              "error": "INTERNAL_SERVER_ERROR",
                                              "message": "An unexpected error occurred",
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
            summary = "Get fixture events",
            description = "Returns events of a specific match (goals, cards, substitutions, etc.).",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            in = ParameterIn.HEADER,
                            required = true,
                            description = "Required header with JWT token in the format: Bearer <token>",
                            example = "Bearer <token>"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fixture events retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FixtureEventResponse.class)),
                            examples = @ExampleObject(
                                    name = "Success response",
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
                    description = "Fixture not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.Equipo07_SportPulseMS.ms_fixtures.exception.ApiErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "Fixture not found",
                                    value = """
                                            {
                                              "error": "FIXTURE_NOT_FOUND",
                                              "message": "Fixture with id 1 not found",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
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
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Unexpected error",
                                    value = """
                                            {
                                              "error": "INTERNAL_SERVER_ERROR",
                                              "message": "An unexpected error occurred",
                                              "timestamp": "2026-04-27T04:16:25.606498737Z"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/{fixtureId}/events")
    public ResponseEntity<List<FixtureEventResponse>> getById(
            @Parameter(description = "Fixture identifier to query.", example = "1508400")
            @PathVariable int fixtureId) {
        var response = fixtureService.getFixtureById(fixtureId);
        return ResponseEntity.ok(response);
    }
}
