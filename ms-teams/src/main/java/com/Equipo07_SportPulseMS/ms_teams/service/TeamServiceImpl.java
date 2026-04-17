package com.Equipo07_SportPulseMS.ms_teams.service;

import com.Equipo07_SportPulseMS.ms_teams.api.TeamClient;
import com.Equipo07_SportPulseMS.ms_teams.dto.TeamResponse;
import com.Equipo07_SportPulseMS.ms_teams.exception.TeamNotFoundException;
import com.Equipo07_SportPulseMS.ms_teams.util.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamClient teamClient;
    private final TeamMapper teamMapper;

    @Override
    public List<TeamResponse> getByLeagueAndSeason(int league, int season) {
        var teams = teamClient.findByLeagueAndSeason(league, season);
        return teams.response().stream().map(teamMapper::toTeamResponseWithoutNational).toList();
    }

    @Override
    public TeamResponse getById(int id) {
        var team = teamClient.findById(id);
        return team.response().stream()
                .findFirst()
                .map(teamMapper::toTeamResponse)
                .orElseThrow(TeamNotFoundException::new);
    }
}
