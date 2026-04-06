package org.example.userauthservice_feb2026.repos;

import org.example.userauthservice_feb2026.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository     // This is optional
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Optional<User>findById(Long id);
}
