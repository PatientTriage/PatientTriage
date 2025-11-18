package com.patienttriage.dental.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
public class Patient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private int age;
  private String gender;    //Enum: MALE, FEMALE, PREFER NOT TO SAY
  private String address;
  private String phone;

  private String symptoms;
  private String medicalHistory;
  private String allergies;
  private String currentMedications;

  private String triagePriority;     // AI pre-analysis: RED > YELLOW > GREEN

  private LocalDateTime createdAt;

  public Patient() {

  }

  public Patient(Long id, String name, int age, String gender, String address, String phone, String symptoms, String medicalHistory, String allergies, String currentMedications, String triagePriority, LocalDateTime createdAt) {
    this.id = id;
    //...
  }

  //getters & setters
}
