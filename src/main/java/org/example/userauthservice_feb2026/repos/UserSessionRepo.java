package org.example.userauthservice_feb2026.repos;

import org.example.userauthservice_feb2026.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepo extends JpaRepository<UserSession,Long> {
}
