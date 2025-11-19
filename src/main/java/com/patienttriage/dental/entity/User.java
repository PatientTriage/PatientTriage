package com.patienttriage.dental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

// TODO： connect to database, password should be stored in hashed format (BCrypt).

/**
 * User class built for user login and authentication.
 * User: patient, doctor, and admin.
 */
@Entity
@Table(name="users")
public class User {

  public enum Role {
    PATIENT,
    DOCTOR,
    ADMIN
  }

  @Id  // Primary Key in database
  @GeneratedValue(strategy = GenerationType.IDENTITY)  //automatically increase
  private Long id;

  private String username;  //user login username (typically email)
  @JsonIgnore   // excluded from JSON responses. Stored in DB as hashed value.
  private String password;

  @Enumerated(EnumType.STRING)
  private Role role;

  @CreationTimestamp
  private LocalDateTime createdAt;

  /**
   * Constructor without parameters.
   * JPA（Hibernate） needs this to create objects.
   */
  public User(){}

  /**
   * Constructor with parameters.
   */
  public User(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  /* ========== getter and setter ========== */
  public Long getId() {
    return id;
  }

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

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

}
