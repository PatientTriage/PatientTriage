package com.patienttriage.controller;

import com.patienttriage.dto.AppointmentRequest;
import com.patienttriage.dto.AppointmentResponse;
import com.patienttriage.entity.UserRole;
import com.patienttriage.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
  private final AppointmentService appointmentService;

  public AppointmentController(AppointmentService appointmentService) {
    this.appointmentService = appointmentService;
  }

  // -------------------------------------------------------------------
  // Create Appointment
  // -------------------------------------------------------------------
  /**
   * Creates a new appointment.
   * Role constraints:
   * - PATIENT: can only create appointments for themselves (patientId must match currentUserId)
   * - DOCTOR: can only create appointments for themselves (doctorId must match currentUserId)
   * - ADMIN: can create appointments for any patient and doctor
   * 
   * @param request the appointment request containing patientId, doctorId, appointmentTime, and reason
   * @param session HTTP session containing logged-in user information
   * @return HTTP 201 Created with success message, or error response
   */
  @PostMapping("/create")
  public ResponseEntity<Object> createAppointment(
      @Valid @RequestBody AppointmentRequest request,
      HttpSession session) {
    
    // Get current logged-in user ID from session (set during login in UserController)
    Long currentUserId = (Long) session.getAttribute("userId");
    UserRole role = (UserRole) session.getAttribute("role");
    String currentUsername = (String) session.getAttribute("username");
    
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Not logged in. Please login first."));
    }
    
    // Create appointment with current logged-in user's ID
    // The service will validate that currentUserId matches patientId (for PATIENT) 
    // or doctorId (for DOCTOR) based on role
    appointmentService.createAppointment(request, role, currentUserId);
    
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of(
            "message", "Appointment created successfully",
            "createdBy", currentUsername != null ? currentUsername : "User ID: " + currentUserId,
            "currentUserId", currentUserId
        ));
  }

  // -------------------------------------------------------------------
  // Get All Appointments (role-based)
  // -------------------------------------------------------------------
  @GetMapping("/my")
  public ResponseEntity<Object> getAppointments(HttpSession session) {

    Long currentUserId = (Long) session.getAttribute("userId");
    UserRole role = (UserRole) session.getAttribute("role");

    if (currentUserId == null || role == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Not logged in. Please login first."));
    }

    List<AppointmentResponse> responseList = appointmentService.getAppointments(role, currentUserId);

    return ResponseEntity.ok(Map.of(
        "appointments", responseList,
        "count", responseList.size()
    ));
  }

  // -------------------------------------------------------------------
  // Get Single Appointment by ID
  // -------------------------------------------------------------------
  @GetMapping("/{appointmentId}")
  public ResponseEntity<Object> getAppointmentById(
      @PathVariable Long appointmentId,
      HttpSession session) {

    Long currentUserId = (Long) session.getAttribute("userId");
    UserRole role = (UserRole) session.getAttribute("role");

    if (currentUserId == null || role == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Not logged in. Please login first."));
    }

    AppointmentResponse response = appointmentService.getAppointmentById(appointmentId, role, currentUserId);

    return ResponseEntity.ok(Map.of(
        "appointment", response
    ));
  }

  // -------------------------------------------------------------------
  // Update Appointment
  // -------------------------------------------------------------------
  @PutMapping("/{appointmentId}")
  public ResponseEntity<Object> updateAppointment(
      @PathVariable Long appointmentId,
      @Valid @RequestBody AppointmentRequest request,
      HttpSession session) {

    Long currentUserId = (Long) session.getAttribute("userId");
    UserRole role = (UserRole) session.getAttribute("role");

    if (currentUserId == null || role == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Not logged in. Please login first."));
    }

    AppointmentResponse response =
        appointmentService.updateAppointment(appointmentId, request, role, currentUserId);

    return ResponseEntity.ok(Map.of(
        "message", "Appointment updated successfully",
        "appointment", response
    ));
  }

  // -------------------------------------------------------------------
  // Cancel Appointment
  // -------------------------------------------------------------------
  @DeleteMapping("/{appointmentId}")
  public ResponseEntity<Object> cancelAppointment(
      @PathVariable Long appointmentId,
      HttpSession session) {

    Long currentUserId = (Long) session.getAttribute("userId");
    UserRole role = (UserRole) session.getAttribute("role");

    if (currentUserId == null || role == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Not logged in. Please login first."));
    }

    AppointmentResponse response =
        appointmentService.cancelAppointment(appointmentId, role, currentUserId);

    return ResponseEntity.ok(Map.of(
        "message", "Appointment cancelled successfully",
        "appointment", response
    ));
  }
}
