package com.patienttriage.dental.controller;

import com.patienttriage.dental.entity.User;
import com.patienttriage.dental.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Register a new user.
   */
  @PostMapping("/register")
  public ResponseEntity<User> registerUser(
      @RequestParam String username,
      @RequestParam String password,
      @RequestParam User.Role role
  ) {
    User registeredUser = userService.register(username, password, role);
    return ResponseEntity.ok(registeredUser);
  }

  /**
   * User login.
   */
  @PostMapping("/login")
  public ResponseEntity<User> login(
      @RequestParam String username,
      @RequestParam String password
  ) {
    User loggedInUser = userService.login(username, password);
    return ResponseEntity.ok(loggedInUser);
  }

  /**
   * Get user by username.
   */
  @GetMapping("/{username}")
  public ResponseEntity<User> getUser(@PathVariable String username) {
    User user = userService.findByUsername(username);
    return ResponseEntity.ok(user);
  }
}
