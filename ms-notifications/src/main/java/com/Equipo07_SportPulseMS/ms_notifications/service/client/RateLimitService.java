package com.Equipo07_SportPulseMS.ms_notifications.service.client;

import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private volatile boolean rateLimited = false;

    public boolean isRateLimited() {
        return rateLimited;
    }

    public void activate() {
        this.rateLimited = true;
    }

    public void reset() {
        this.rateLimited = false;
    }
}