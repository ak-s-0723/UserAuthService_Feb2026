package org.example.userauthservice_feb2026.repos;

import org.example.userauthservice_feb2026.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepo extends JpaRepository<UserSession,Long> {
    Optional<UserSession> findByToken(String token);
}
