package com.patienttriage.dental.service.impl;

import com.patienttriage.dental.entity.Role;
import com.patienttriage.dental.entity.User;
import com.patienttriage.dental.repository.UserRepository;
import com.patienttriage.dental.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Registers a new user with an encrypted password.
   */
  @Override
  public User register(String username, String rawPassword, Role role) {

    // Check duplicates
    if (userRepository.existsByUsername(username)) {
      throw new RuntimeException("Username already exists: " + username);
    }

    // Encrypt password
    String encodedPassword = passwordEncoder.encode(rawPassword);

    // Create user entity
    User newUser = new User(username, encodedPassword, role);

    // Save to database
    return userRepository.save(newUser);
  }

  /**
   * User login validation.
   */
  @Override
  public User login(String username, String rawPassword) {

    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // Validate password
    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
      throw new RuntimeException("Invalid password");
    }

    return user;
  }

  /**
   * Fetch a user by username.
   */
  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
  }
}