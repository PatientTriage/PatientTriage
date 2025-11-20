package com.patienttriage.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;


// done by Yuyun
@Entity
@Table(name="users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username", unique = true, nullable = false)
  private String username;  //login

  @Column(name = "password", nullable = false)
  private String password;  //login

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  public User(){}

  public User(String username, String password, UserRole role) {
    this.username = username;
    this.password = password;
    this.role = role;
    this.createdAt = LocalDateTime.now();
  }

  // for each user only have one file based on their role and user id
  // mappedBy = "user", the user is defined in the profile entities, they are mapping
  @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private PatientProfile patientProfile;

  @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private DoctorProfile doctorProfile;

  @OneToOne(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private AdminProfile adminProfile;

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

  public UserRole getRole() {
    return role;
  }

  public void setRole(UserRole role) {
    this.role = role;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  // ----------- Helpers ----------
  public boolean isPatient() { return role == UserRole.PATIENT; }
  public boolean isDoctor() { return role == UserRole.DOCTOR; }
  public boolean isAdmin() { return role == UserRole.ADMIN; }


}
