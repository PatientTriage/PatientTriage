package com.patienttriage.controller;


import com.patienttriage.dto.UserLoginRequest;
import com.patienttriage.dto.UserRegisterRequest;
import com.patienttriage.entity.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.patienttriage.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }
  // TODO: need a get API for register, so that the web will not automatically go to the login page.

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

  // TODO: If there's new user, direct to the register link

  // TODO: split the different role with different functionality
}