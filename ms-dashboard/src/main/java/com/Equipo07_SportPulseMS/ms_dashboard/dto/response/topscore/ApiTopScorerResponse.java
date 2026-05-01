package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.topscore;

import java.util.List;

public record ApiTopScorerResponse(
        List<PlayerWrapper> response
) {

    public record PlayerWrapper(
            Player player,
            List<Statistic> statistics
    ) {}

    public record Player(
            Integer id,
            String name
    ) {}

    public record Statistic(
            Team team,
            Goals goals
    ) {
    }

    public record Team(
            Integer id,
            String name
    ) {}

    public record Goals(
            Integer total
    ) {}
}