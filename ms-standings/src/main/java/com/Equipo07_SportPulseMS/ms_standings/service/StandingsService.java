package com.Equipo07_SportPulseMS.ms_standings.service;

import com.Equipo07_SportPulseMS.ms_standings.client.ApiSportsStandingsClient;
import com.Equipo07_SportPulseMS.ms_standings.client.TeamsClient;
import com.Equipo07_SportPulseMS.ms_standings.client.dto.ApiSportsStandingsEnvelope;
import com.Equipo07_SportPulseMS.ms_standings.client.dto.TeamDetailResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.StandingEntryResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.StandingsResponse;
import com.Equipo07_SportPulseMS.ms_standings.dto.TeamSummaryResponse;
import com.Equipo07_SportPulseMS.ms_standings.exception.StandingsNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class StandingsService {

    private final ApiSportsStandingsClient standingsClient;
    private final TeamsClient teamsClient;

    public StandingsService(ApiSportsStandingsClient standingsClient, TeamsClient teamsClient) {
        this.standingsClient = standingsClient;
        this.teamsClient = teamsClient;
    }

    @Cacheable(value = "standingsByLeagueSeason", key = "#league + '|' + #season")
    public StandingsResponse getStandings(Integer league, Integer season) {
        ApiSportsStandingsEnvelope envelope = standingsClient.getStandings(league, season);
        if (envelope == null || envelope.response() == null || envelope.response().isEmpty() || envelope.response().get(0).league() == null) {
            throw new StandingsNotFoundException("No existe tabla para la liga y temporada proporcionadas");
        }

        ApiSportsStandingsEnvelope.ApiSportsStandingLeague leagueData = envelope.response().get(0).league();
        List<ApiSportsStandingsEnvelope.ApiSportsStandingRow> standingsRows =
                leagueData.standings() == null || leagueData.standings().isEmpty()
                        ? List.of()
                        : leagueData.standings().get(0);

        List<StandingEntryResponse> entries = standingsRows.stream()
                .map(this::toEntry)
                .sorted(Comparator.comparing(StandingEntryResponse::rank, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        StandingsResponse.LeagueInfo leagueInfo = new StandingsResponse.LeagueInfo(
                leagueData.id(),
                leagueData.name(),
                leagueData.country(),
                leagueData.season()
        );

        return new StandingsResponse(leagueInfo, entries);
    }

    private StandingEntryResponse toEntry(ApiSportsStandingsEnvelope.ApiSportsStandingRow row) {
        TeamSummaryResponse team = enrichTeam(row);

        Integer played = row.all() != null ? row.all().played() : null;
        Integer won = row.all() != null ? row.all().won() : null;
        Integer drawn = row.all() != null ? row.all().drawn() : null;
        Integer lost = row.all() != null ? row.all().lost() : null;
        Integer goalsFor = row.all() != null && row.all().goals() != null ? row.all().goals().goalsFor() : null;
        Integer goalsAgainst = row.all() != null && row.all().goals() != null ? row.all().goals().against() : null;

        return new StandingEntryResponse(
                row.rank(),
                team,
                row.points(),
                played,
                won,
                drawn,
                lost,
                goalsFor,
                goalsAgainst,
                row.goalsDiff(),
                row.form()
        );
    }

    private TeamSummaryResponse enrichTeam(ApiSportsStandingsEnvelope.ApiSportsStandingRow row) {
        Integer teamId = row.team() != null ? row.team().id() : null;
        String teamName = row.team() != null ? row.team().name() : null;
        String teamLogo = row.team() != null ? row.team().logo() : null;

        if (teamId == null) {
            return new TeamSummaryResponse(null, teamName, teamLogo);
        }

        try {
            TeamDetailResponse teamDetail = teamsClient.getTeam(teamId);
            if (teamDetail == null) {
                return new TeamSummaryResponse(teamId, teamName, teamLogo);
            }
            return new TeamSummaryResponse(
                    teamDetail.id() != null ? teamDetail.id() : teamId,
                    teamDetail.name() != null ? teamDetail.name() : teamName,
                    teamDetail.logo() != null ? teamDetail.logo() : teamLogo
            );
        } catch (Exception ex) {
            return new TeamSummaryResponse(teamId, teamName, teamLogo);
        }
    }
}
