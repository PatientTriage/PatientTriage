package service.impl;

import entity.User;
import entity.UserRole;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import service.UserService;

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
  public User register(String username, String rawPassword, UserRole role) {

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