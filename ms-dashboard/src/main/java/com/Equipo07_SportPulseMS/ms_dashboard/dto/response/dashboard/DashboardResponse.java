package com.Equipo07_SportPulseMS.ms_dashboard.dto.response.dashboard;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.league.LeagueDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.standing.StandingPreviewDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.team.MatchDTO;
import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.topscore.TopScorerDTO;

import java.util.List;

public record DashboardResponse(

        LeagueDTO league,
        String today,

        List<MatchDTO> matchesToday,

        List<StandingPreviewDTO> standingsPreview,

        List<TopScorerDTO> topScorers,

        String lastUpdated,

        List<String> failedServices

) {}