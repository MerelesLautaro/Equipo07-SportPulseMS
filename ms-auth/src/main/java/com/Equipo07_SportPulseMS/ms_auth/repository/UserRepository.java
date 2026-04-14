package com.Equipo07_SportPulseMS.ms_auth.repository;

import com.Equipo07_SportPulseMS.ms_auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
    Optional<User> findByEmail(String username);
}
