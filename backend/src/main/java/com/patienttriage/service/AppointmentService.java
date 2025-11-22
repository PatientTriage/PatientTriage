package com.patienttriage.service;

import com.patienttriage.dto.AppointmentRequest;
import com.patienttriage.dto.AppointmentResponse;
import java.util.List;

/**
 * Service interface for appointment management.
 * All methods require currentUserId for role-based authorization.
 */
public interface AppointmentService {

  /**
   * Creates a new appointment.
   * Role constraints:
   * - PATIENT: can only create appointments for themselves (patientId must match currentUserId)
   * - DOCTOR: can only create appointments for themselves (doctorId must match currentUserId)
   * - ADMIN: can create appointments for any patient and doctor
   * 
   * @param request the appointment request containing patientId, doctorId, appointmentTime, and reason
   * @param currentUserId the ID of the user making the request (for authorization)
   * @return AppointmentResponse with role-appropriate information
   */
  AppointmentResponse createAppointment(AppointmentRequest request, Long currentUserId);

  /**
   * Retrieves a single appointment by ID with role-based access control.
   * Role constraints:
   * - PATIENT: can only view their own appointments (with limited doctor info)
   * - DOCTOR: can view appointments assigned to them (with full patient profile)
   * - ADMIN: can view any appointment (with full patient and doctor profiles)
   * 
   * @param appointmentId the ID of the appointment to retrieve
   * @param currentUserId the ID of the user making the request (for authorization)
   * @return AppointmentResponse with role-appropriate information
   */
  AppointmentResponse checkAppointment(Long appointmentId, Long currentUserId);

  /**
   * Updates an existing appointment.
   * Role constraints:
   * - PATIENT: can only update their own appointments
   * - DOCTOR: can only update appointments assigned to them
   * - ADMIN: can update any appointment
   * Note: Appointment can only be updated if status is not CANCELLED or COMPLETED
   * 
   * @param appointmentId the ID of the appointment to update
   * @param request the appointment request containing updated fields
   * @param currentUserId the ID of the user making the request (for authorization)
   * @return AppointmentResponse with updated information
   */
  AppointmentResponse updateAppointment(Long appointmentId, AppointmentRequest request, Long currentUserId);

  /**
   * Cancels an appointment by setting status to CANCELLED.
   * Role constraints:
   * - PATIENT: can only cancel their own appointments
   * - DOCTOR: can only cancel appointments assigned to them
   * - ADMIN: can cancel any appointment
   * 
   * @param appointmentId the ID of the appointment to cancel
   * @param currentUserId the ID of the user making the request (for authorization)
   * @return AppointmentResponse with cancelled status
   */
  AppointmentResponse cancelAppointment(Long appointmentId, Long currentUserId);

  /**
   * Retrieves all appointments for a specific patient.
   * Role constraints:
   * - PATIENT: can only view their own appointments (patientId must match currentUserId)
   * - DOCTOR/ADMIN: can view appointments for any patient
   * 
   * @param patientId the ID of the patient
   * @param currentUserId the ID of the user making the request (for authorization)
   * @return List of AppointmentResponse with role-appropriate information
   */
  List<AppointmentResponse> getAppointmentsByPatientId(Long patientId, Long currentUserId);

  /**
   * Retrieves all appointments for a specific doctor.
   * Role constraints:
   * - DOCTOR: can only view their own appointments (doctorId must match currentUserId)
   * - PATIENT/ADMIN: can view appointments for any doctor
   * 
   * @param doctorId the ID of the doctor
   * @param currentUserId the ID of the user making the request (for authorization)
   * @return List of AppointmentResponse with role-appropriate information
   */
  List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId, Long currentUserId);

  /**
   * Retrieves all appointments in the system (admin only).
   * 
   * @param currentUserId the ID of the user making the request (must be ADMIN)
   * @return List of all AppointmentResponse with full information
   */
  List<AppointmentResponse> getAllAppointments(Long currentUserId);

}
