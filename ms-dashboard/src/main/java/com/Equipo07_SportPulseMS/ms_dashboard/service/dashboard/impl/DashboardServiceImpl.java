package com.Equipo07_SportPulseMS.ms_dashboard.service.dashboard.impl;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.dashboard.*;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.fixture.FixtureResponseDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league.LeagueDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing.StandingEntryDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing.StandingPreviewDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing.StandingsResponseDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team.MatchDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.topscore.ApiTopScorerResponse;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.topscore.TopScorerDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.service.client.ApiFootballClient;
import com.Equipo07_SportPulseMS.ms_dashboard.service.client.FixturesClient;
import com.Equipo07_SportPulseMS.ms_dashboard.service.client.LeagueClient;
import com.Equipo07_SportPulseMS.ms_dashboard.service.client.StandingsClient;
import com.Equipo07_SportPulseMS.ms_dashboard.service.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final FixturesClient fixturesClient;
    private final StandingsClient standingsClient;
    private final ApiFootballClient apiFootballClient;
    private final LeagueClient leagueClient;

    @Override
    public Mono<DashboardResponse> getDashboard(Integer league, Integer season) {

        List<String> failedServices = new CopyOnWriteArrayList<>();

        // LEAGUE
        Mono<LeagueDTO> leagueMono = leagueClient
                .getLeagueById(league)
                .map(l -> new LeagueDTO(l.id(), l.name(), l.country()))
                .onErrorResume(ex -> {
                    failedServices.add("leagues");
                    return Mono.just(new LeagueDTO(null, "Unknown", "Unknown"));
                });

        // FIXTURES
        Mono<List<MatchDTO>> matchesMono = fixturesClient
                .getFixtures(null, LocalDate.now().toString()) // 🔥 SOLO DATE
                .flatMap(list -> {
                    if (!list.isEmpty()) {
                        return Mono.just(list);
                    }

                    System.out.println(">>> No fixtures by date, fallback to league");

                    return fixturesClient.getFixtures(league, null);
                })
                .map(list -> list.stream()
                        .filter(f -> f.league() != null && f.league().id().equals(league)) // 🔥 filtrás vos
                        .limit(5)
                        .map(this::mapToMatchDTO)
                        .toList()
                )
                .onErrorResume(ex -> {
                    failedServices.add("fixtures");
                    return Mono.just(List.of());
                });

        // STANDINGS
        Mono<StandingsResponseDTO> standingsMono = standingsClient
                .getStandings(league, season)
                .onErrorResume(ex -> {
                    failedServices.add("standings");
                    return Mono.just(new StandingsResponseDTO(null, List.of()));
                });

        // TOP SCORERS
        Mono<List<TopScorerDTO>> scorersMono = apiFootballClient
                .getTopScorers(league, season)
                .map(resp -> resp.response().stream()
                        .limit(3)
                        .map(this::mapToTopScorerDTO)
                        .toList()
                )
                .onErrorResume(ex -> {
                    failedServices.add("topscorers");
                    return Mono.just(List.of());
                });

        // BUILD
        return Mono.zip(leagueMono, matchesMono, standingsMono, scorersMono)
                .map(tuple -> {

                    LeagueDTO leagueInfo = tuple.getT1();
                    List<MatchDTO> matches = tuple.getT2();
                    StandingsResponseDTO standingsRaw = tuple.getT3();
                    List<TopScorerDTO> scorers = tuple.getT4();

                    List<StandingPreviewDTO> standingsPreview =
                            standingsRaw.standings().stream()
                                    .limit(3)
                                    .map(this::mapToStandingPreview)
                                    .toList();

                    return new DashboardResponse(
                            leagueInfo,
                            LocalDate.now().toString(),
                            matches,
                            standingsPreview,
                            scorers,
                            Instant.now().toString(),
                            failedServices
                    );
                });
    }

    // MAPPERS

    private MatchDTO mapToMatchDTO(FixtureResponseDTO f) {
        return new MatchDTO(
                f.id(),
                f.date(),
                f.homeTeam(),
                f.awayTeam(),
                f.status().shortStatus()
        );
    }

    private StandingPreviewDTO mapToStandingPreview(StandingEntryDTO s) {
        return new StandingPreviewDTO(
                s.rank(),
                s.team().name(),
                s.points(),
                s.played()
        );
    }

    private TopScorerDTO mapToTopScorerDTO(ApiTopScorerResponse.PlayerWrapper p) {

        var stats = p.statistics().isEmpty() ? null : p.statistics().get(0);

        return new TopScorerDTO(
                p.player().name(),
                stats != null ? stats.team().name() : "Unknown",
                stats != null && stats.goals() != null ? stats.goals().total() : 0
        );
    }
}