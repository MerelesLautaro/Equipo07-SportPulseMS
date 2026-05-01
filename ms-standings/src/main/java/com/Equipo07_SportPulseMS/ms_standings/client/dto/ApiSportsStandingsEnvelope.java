package com.Equipo07_SportPulseMS.ms_standings.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ApiSportsStandingsEnvelope(
        List<ApiSportsStandingLeagueResponse> response
) {

    public record ApiSportsStandingLeagueResponse(
            ApiSportsStandingLeague league
    ) {
    }

    public record ApiSportsStandingLeague(
            Integer id,
            String name,
            String country,
            Integer season,
            List<List<ApiSportsStandingRow>> standings
    ) {
    }

    public record ApiSportsStandingRow(
            Integer rank,
            Team team,
            Integer points,
            All all,
            @JsonProperty("goalsDiff") Integer goalsDiff,
            String form,
            String description
    ) {
        public record Team(
                Integer id,
                String name,
                String logo
        ) {
        }

        public record All(
                Integer played,
                @JsonProperty("win") Integer won,
                @JsonProperty("draw") Integer drawn,
                @JsonProperty("lose") Integer lost,
                Goals goals
        ) {
        }

        public record Goals(
                @JsonProperty("for") Integer goalsFor,
                Integer against
        ) {
        }
    }
}
