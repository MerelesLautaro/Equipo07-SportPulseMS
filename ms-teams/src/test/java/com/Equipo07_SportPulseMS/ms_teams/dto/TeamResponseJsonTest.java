package com.Equipo07_SportPulseMS.ms_teams.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TeamResponseJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeTeamsJsonIntoTeamResponse() throws Exception {
        Path jsonPath = Path.of("teams.json");
        String rawJson = Files.readString(jsonPath);

        TeamResponse teamResponse = objectMapper.readValue(rawJson, TeamResponse.class);

        assertNotNull(teamResponse);
        assertEquals("teams", teamResponse.endpoint());
        assertEquals(20, teamResponse.results());
        assertNotNull(teamResponse.response());
        assertEquals(20, teamResponse.response().size());
        assertFalse(teamResponse.response().isEmpty());
        assertEquals("Manchester United", teamResponse.response().get(0).team().name());
    }
}

