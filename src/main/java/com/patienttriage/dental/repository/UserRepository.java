package com.patienttriage.dental.repository;

import com.patienttriage.dental.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User entity.
 * Provides data access operations using Spring Data JPA.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds a user by username.
   * Commonly used in login/authentication.
   *
   * @param username the login username
   * @return Optional<User> if found
   */
  Optional<User> findByUsername(String username);

  /**
   * Checks whether a username already exists.
   * Useful during registration to avoid duplicates.
   */
  boolean existsByUsername(String username);
}
