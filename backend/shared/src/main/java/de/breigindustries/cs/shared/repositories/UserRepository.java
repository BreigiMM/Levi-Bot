package de.breigindustries.cs.shared.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.breigindustries.cs.shared.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
