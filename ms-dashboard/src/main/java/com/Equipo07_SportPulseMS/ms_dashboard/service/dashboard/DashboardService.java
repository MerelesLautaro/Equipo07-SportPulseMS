package com.Equipo07_SportPulseMS.ms_dashboard.service.dashboard;

import com.Equipo07_SportPulseMS.ms_dashboard.dto.response.dashboard.DashboardResponse;
import reactor.core.publisher.Mono;

public interface DashboardService {
    Mono<DashboardResponse> getDashboard(Integer league, Integer season);
}
