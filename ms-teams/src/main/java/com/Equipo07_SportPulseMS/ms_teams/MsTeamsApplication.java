package com.Equipo07_SportPulseMS.ms_teams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MsTeamsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsTeamsApplication.class, args);
    }
}