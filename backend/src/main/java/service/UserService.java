package service;

import entity.User;
import entity.UserRole;
import org.springframework.stereotype.Service;

public interface UserService {
  /**
   * Registers a new user with encrypted password.
   */
  User register(String username, String rawPassword, UserRole role);

  /**
   * Authenticates a user and verifies password.
   */
  User login(String username, String rawPassword);

  /**
   * Finds a user by username.
   */
  User findByUsername(String username);
}
