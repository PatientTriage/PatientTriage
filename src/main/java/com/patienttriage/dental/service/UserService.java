package com.patienttriage.dental.service;

import com.patienttriage.dental.entity.User;

public interface UserService {
  /**
   * Registers a new user with encrypted password.
   */
  User register(String username, String rawPassword, User.Role role);

  /**
   * Authenticates a user and verifies password.
   */
  User login(String username, String rawPassword);

  /**
   * Finds a user by username.
   */
  User findByUsername(String username);
}
