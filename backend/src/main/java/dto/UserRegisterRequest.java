package dto;

import entity.User;
import entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for user registration.
 * Contains the required fields for creating a new user.
 */
public class UserRegisterRequest {

  @NotBlank(message = "Username (email) is required")
  @Email(message = "Invalid email format")
  private String username;

  @NotBlank(message = "Password is required")
  private String password;

  @NotNull(message = "Role is required")
  private UserRole role;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }
}
