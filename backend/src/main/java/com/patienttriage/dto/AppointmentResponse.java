package com.patienttriage.dto;

import java.time.LocalDateTime;

/**
 * AppointmentResponse is a DTO used to return appointment details back to the client, including
 * appointment ID, patient/doctor IDs, time, reason, and status.
 * Nested objects are populated based on user role:
 *  - PATIENT: patientInfo = null, limitedDoctorInfo = populated, doctorInfo = null
 *  - DOCTOR: patientInfo = full PatientInfo, doctorInfo = full DoctorInfo, limitedDoctorInfo = null
 *  - ADMIN: patientInfo = full PatientInfo, doctorInfo = full DoctorInfo, limitedDoctorInfo = null
 */
public class AppointmentResponse {

  // will always present - basic appointment info
  private Long id;
  private Long patientId;
  private Long doctorId;
  private LocalDateTime appointmentTime;
  private String reason;
  private String status;
  private LocalDateTime createdAt;

  // optional, based on the role
  private PatientInfo patientInfo; // null for patient, full for DOCTOR/ADMIN
  private DoctorInfo doctorInfo; // DoctorInfo for DOCTOR/ADMIN
  private LimitedDoctorInfo limitedDoctorInfo; //LimitedDoctorInfo for PATIENT

  // Default constructor
  public AppointmentResponse() {}

  // basic appointment info for all roles
  public AppointmentResponse(Long id, Long patientId, Long doctorId,
      LocalDateTime appointmentTime, String reason, String status, LocalDateTime createdAt) {
    this.id = id;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.appointmentTime = appointmentTime;
    this.reason = reason;
    this.status = status;
    this.createdAt = createdAt;
  }

  // Full constructor with nested objects
  public AppointmentResponse(Long id, Long patientId, Long doctorId,
      LocalDateTime appointmentTime, String reason, String status, LocalDateTime createdAt,
      PatientInfo patientInfo, DoctorInfo doctorInfo, LimitedDoctorInfo limitedDoctorInfo) {
    this.id = id;
    this.patientId = patientId;
    this.doctorId = doctorId;
    this.appointmentTime = appointmentTime;
    this.reason = reason;
    this.status = status;
    this.createdAt = createdAt;
    this.patientInfo = patientInfo;
    this.doctorInfo = doctorInfo;
    this.limitedDoctorInfo = limitedDoctorInfo;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public Long getPatientId() {
    return patientId;
  }

  public Long getDoctorId() {
    return doctorId;
  }

  public LocalDateTime getAppointmentTime() {
    return appointmentTime;
  }

  public void setAppointmentTime(LocalDateTime appointmentTime) {
    this.appointmentTime = appointmentTime;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public PatientInfo getPatientInfo() {
    return patientInfo;
  }

  public void setPatientInfo(PatientInfo patientInfo) {
    this.patientInfo = patientInfo;
  }

  public DoctorInfo getDoctorInfo() {
    return doctorInfo;
  }

  public void setDoctorInfo(DoctorInfo doctorInfo) {
    this.doctorInfo = doctorInfo;
  }

  public LimitedDoctorInfo getLimitedDoctorInfo() {
    return limitedDoctorInfo;
  }

  public void setLimitedDoctorInfo(LimitedDoctorInfo limitedDoctorInfo) {
    this.limitedDoctorInfo = limitedDoctorInfo;
  }

  // Helper methods for convenience
  public boolean hasPatientInfo() {
    return patientInfo != null;
  }

  public boolean hasDoctorInfo() {
    return doctorInfo != null;
  }

  public boolean hasLimitedDoctorInfo() {
    return limitedDoctorInfo != null;
  }
}
