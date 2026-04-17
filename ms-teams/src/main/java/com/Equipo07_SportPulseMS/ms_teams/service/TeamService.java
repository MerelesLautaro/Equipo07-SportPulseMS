package com.Equipo07_SportPulseMS.ms_teams.service;

import com.Equipo07_SportPulseMS.ms_teams.dto.TeamItem;
import com.Equipo07_SportPulseMS.ms_teams.dto.TeamResponse;

import java.util.List;

public interface TeamService {

    List<TeamResponse> getByLeagueAndSeason(int league, int season);

    TeamResponse getById(int id);
}
