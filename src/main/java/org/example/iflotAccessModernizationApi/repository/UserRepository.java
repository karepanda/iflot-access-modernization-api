package org.example.iflotAccessModernizationApi.repository;

import org.example.iflotAccessModernizationApi.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameWithRoles(String username);
}
