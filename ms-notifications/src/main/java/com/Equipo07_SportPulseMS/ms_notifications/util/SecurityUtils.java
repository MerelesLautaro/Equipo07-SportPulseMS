package com.Equipo07_SportPulseMS.ms_notifications.util;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public class SecurityUtils {

    public static UUID getCurrentUserId() {
        Object principal = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal();

        return (UUID) principal;
    }
}
