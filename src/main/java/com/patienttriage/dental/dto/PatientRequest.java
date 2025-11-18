package com.patienttriage.dental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PatientRequest {
  @NotBlank(message = "Patient name is required")
  private String name;

  @NotNull(message = "Patient age is required")
  private int age;

  @NotBlank(message = "Symptom description is required")
  private String symptoms;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public String getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(String symptoms) {
    this.symptoms = symptoms;
  }
}
