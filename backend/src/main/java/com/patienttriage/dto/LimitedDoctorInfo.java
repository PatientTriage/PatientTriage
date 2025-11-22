package com.patienttriage.dto;

/**
 * LimitedDoctorInfo DTO - Contains limited doctor information
 * Used in AppointmentResponse for PATIENT role
 * Only includes: firstName, lastName, specialty (as per requirements)
 */
public class LimitedDoctorInfo {
    
    private String firstName;
    private String lastName;
    private String specialty;
    
    // Default constructor
    public LimitedDoctorInfo() {}
    
    // Full constructor
    public LimitedDoctorInfo(String firstName, String lastName, String specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialty = specialty;
    }
    
    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getSpecialty() {
        return specialty;
    }
    
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}

