package com.patienttriage.dental.controller;

import com.patienttriage.dental.dto.UserLoginRequest;
import com.patienttriage.dental.dto.UserRegisterRequest;
import com.patienttriage.dental.entity.User;
import com.patienttriage.dental.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   * User Registration
   * Why POST?
   * - Registration creates a new resource in DB → POST is correct HTTP method.
   */
  @PostMapping("/register")
  public User register(@RequestBody UserRegisterRequest request) {
    return userService.register(
        request.getUsername(),
        request.getPassword(),
        request.getRole()
    );
  }

  /**
   * User Login
   * Why POST?
   * - Login involves sending sensitive data (password)
   * - POST body is safer than GET params
   */
  @PostMapping("/login")
  public User login(@RequestBody UserLoginRequest request) {
    return userService.login(
        request.getUsername(),
        request.getPassword()
    );
  }
}