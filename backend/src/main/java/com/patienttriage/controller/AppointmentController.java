package com.patienttriage.controller;

import com.patienttriage.dto.AppointmentRequest;
import com.patienttriage.service.AppointmentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
    String currentUsername = (String) session.getAttribute("username");
    
    if (currentUserId == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Not logged in. Please login first."));
    }
    
    // Create appointment with current logged-in user's ID
    // The service will validate that currentUserId matches patientId (for PATIENT) 
    // or doctorId (for DOCTOR) based on role
    appointmentService.createAppointment(request, currentUserId);
    
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of(
            "message", "Appointment created successfully",
            "createdBy", currentUsername != null ? currentUsername : "User ID: " + currentUserId,
            "currentUserId", currentUserId
        ));
  }
}
