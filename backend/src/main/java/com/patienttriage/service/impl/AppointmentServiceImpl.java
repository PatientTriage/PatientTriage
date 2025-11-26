package com.patienttriage.service.impl;

import com.patienttriage.dto.AppointmentRequest;
import com.patienttriage.dto.AppointmentResponse;
import com.patienttriage.dto.DoctorInfo;
import com.patienttriage.dto.LimitedDoctorInfo;
import com.patienttriage.dto.PatientInfo;
import com.patienttriage.entity.Appointment;
import com.patienttriage.entity.AppointmentStatus;
import com.patienttriage.entity.DoctorProfile;
import com.patienttriage.entity.PatientProfile;
import com.patienttriage.entity.User;
import com.patienttriage.entity.UserRole;
import com.patienttriage.repository.AppointmentRepository;
import com.patienttriage.repository.UserRepository;
import com.patienttriage.repository.PatientProfileRepository;
import com.patienttriage.repository.DoctorProfileRepository;
import com.patienttriage.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final UserRepository userRepository;
  private final PatientProfileRepository patientProfileRepository;
  private final DoctorProfileRepository doctorProfileRepository;

  // Constructor injection
  public AppointmentServiceImpl(AppointmentRepository appointmentRepository, 
                                UserRepository userRepository, PatientProfileRepository patientProfileRepository, DoctorProfileRepository doctorProfileRepository) {
    this.appointmentRepository = appointmentRepository;
    this.userRepository = userRepository;
    this.patientProfileRepository = patientProfileRepository;
    this.doctorProfileRepository = doctorProfileRepository;
  }

  // ------------- Create appointments -------------- //
  @Override
  @Transactional
  public AppointmentResponse createAppointment(AppointmentRequest request, UserRole role, Long currentUserId) {
    // 1. Load and validate current logged-in user exists
    // currentUserId comes from HttpSession (set during login in UserController)
    User currentUser = userRepository.findById(currentUserId)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + currentUserId));

    // 2. Validate role-based constraints
    // Ensure the logged-in user can only create appointments for themselves
    UserRole currentUserRole = currentUser.getRole();
    
    if (currentUserRole == UserRole.PATIENT) {
      // PATIENT can only create appointments for themselves
      if (!request.getPatientId().equals(currentUserId)) {
        throw new RuntimeException("Patients can only create appointments for themselves");
      }
    } else if (currentUserRole == UserRole.DOCTOR) {
      // DOCTOR can only create appointments for themselves
      if (!request.getDoctorId().equals(currentUserId)) {
        throw new RuntimeException("Doctors can only create appointments for themselves");
      }
    }
    // ADMIN can create appointments for any patient and doctor (no validation needed)

    // 3. Validate patient exists and has PATIENT role
    User patient= userRepository.findById(request.getPatientId())
        .orElseThrow(() -> new RuntimeException("Patient not found with id: " + request.getPatientId()));
    
    if (patient.getRole() != UserRole.PATIENT) {
      throw new RuntimeException("User with id " + request.getPatientId() + " is not a patient");
    }

    // 4. Validate doctor exists and has DOCTOR role
    User doctor = userRepository.findById(request.getDoctorId())
        .orElseThrow(() -> new RuntimeException("Doctor not found with id: " + request.getDoctorId()));
    
    if (doctor.getRole() != UserRole.DOCTOR) {
      throw new RuntimeException("User with id " + request.getDoctorId() + " is not a doctor");
    }

    // 5. Validate appointment time is in the future
    // TODO: make sure there's no conflict appointment
    LocalDateTime appointmentTime = request.getAppointmentTime();
    if (appointmentTime.isBefore(LocalDateTime.now())) {
      throw new RuntimeException("Appointment time must be in the future");
    }

    // 6. Create appointment entity
    Appointment appointment = new Appointment(
        patient,
        doctor,
        appointmentTime,
        request.getReason()
    );

    // 7. Save appointment
    Appointment savedAppointment = appointmentRepository.save(appointment);
    return toResponse(savedAppointment, currentUserRole);
  }

  // ------------- Get appointments -------------- //
  @Override
  public List<AppointmentResponse> getAppointments(UserRole role, Long currentUserId) {
    // 1. load current user
    User currentUser = userRepository.findById(currentUserId)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + currentUserId));

    List<Appointment> appointments;

    // 2. role-based query
    switch (role) {
      case PATIENT:
        appointments = appointmentRepository.findByPatient_Id(currentUserId);
        break;
      case DOCTOR:
        appointments = appointmentRepository.findByDoctor_Id(currentUserId);
        break;
      case ADMIN:
        appointments = appointmentRepository.findAll();
        break;
      default:
        throw new RuntimeException("Invalid role");
    }

    // 3. map to responses
    return appointments.stream()
        .map(a -> toResponse(a, role))
        .toList();
  }

  @Override
  public AppointmentResponse getAppointmentById(Long appointmentId, UserRole role, Long currentUserId) {
    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

    if (!hasAccess(appointment, role, currentUserId)) {
      throw new IllegalArgumentException("You do not have permission to view this appointment.");
    }

    return toResponse(appointment, role);
  }


  // ------------- Update appointments -------------- //
  @Override
  public AppointmentResponse updateAppointment(Long appointmentId, AppointmentRequest request, UserRole role, Long currentUserId) {
      Appointment appointment = appointmentRepository.findById(appointmentId)
          .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

      if (!hasAccess(appointment, role, currentUserId)) {
        throw new IllegalArgumentException("You do not have permission to update this appointment.");
      }

      // Cannot update if cancelled / completed
      if (appointment.getStatus() == AppointmentStatus.CANCELLED ||
          appointment.getStatus() == AppointmentStatus.COMPLETED) {
        throw new IllegalArgumentException("Cannot update cancelled or completed appointments.");
      }

      // ----- Time conflict check -----
      checkTimeConflicts(
          request.getAppointmentTime(),
          appointment.getDoctor().getId(),
          appointment.getPatient().getId(),
          appointmentId
      );

      appointment.setAppointmentTime(request.getAppointmentTime());
      appointment.setReason(request.getReason());

      appointmentRepository.save(appointment);
      return toResponse(appointment, role);
    }


  // ------------- Cancel appointments -------------- //
  @Override
  public AppointmentResponse cancelAppointment(Long appointmentId, UserRole role, Long currentUserId) {
    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

    if (!hasAccess(appointment, role, currentUserId)) {
      throw new IllegalArgumentException("You do not have permission to cancel this appointment.");
    }

    appointment.setStatus(AppointmentStatus.CANCELLED);
    appointmentRepository.save(appointment);
    return toResponse(appointment, role);
  }

  // ========================================================================
  // Helper Methods
  // ========================================================================

  /** Check doctor + patient time conflicts **/
  private void checkTimeConflicts(LocalDateTime time, Long doctorId, Long patientId, Long ignoreAppointmentId) {
    // doctor conflicts
    List<Appointment> doctorConflicts =
        appointmentRepository.findConflictsByDoctor(doctorId, time);

    // ignore appointment itself when updating
    if (ignoreAppointmentId != null) {
      doctorConflicts = doctorConflicts.stream()
          .filter(a -> !a.getId().equals(ignoreAppointmentId))
          .toList();
    }

    if (!doctorConflicts.isEmpty()) {
      throw new IllegalArgumentException("Doctor already has an appointment at this time.");
    }

    // patient conflicts
    List<Appointment> patientConflicts =
        appointmentRepository.findConflictsByPatient(patientId, time);

    if (ignoreAppointmentId != null) {
      patientConflicts = patientConflicts.stream()
          .filter(a -> !a.getId().equals(ignoreAppointmentId))
          .toList();
    }

    if (!patientConflicts.isEmpty()) {
      throw new IllegalArgumentException("Patient already has an appointment at this time.");
    }
  }

  /** Role access check **/
  private boolean hasAccess(Appointment appointment, UserRole role, Long currentUserId) {
    return switch (role) {
      case ADMIN -> true;
      case DOCTOR -> appointment.getDoctor().getId().equals(currentUserId);
      case PATIENT -> appointment.getPatient().getId().equals(currentUserId);
    };
  }

  /** Convert Appointment entity to DTO with role-based visibility **/
  private AppointmentResponse toResponse(Appointment appointment, UserRole role) {

    AppointmentResponse dto = new AppointmentResponse();
    dto.setAppointmentId(appointment.getId());
    dto.setAppointmentTime(appointment.getAppointmentTime());
    dto.setReason(appointment.getReason());
    dto.setStatus(appointment.getStatus());
    dto.setCreatedAt(appointment.getCreatedAt());

    // Always include basic IDs
    Long patientUserId = appointment.getPatient().getId();
    Long doctorUserId = appointment.getDoctor().getId();

    // backend -> frontend
    dto.setPatientId(patientUserId);
    dto.setDoctorId(doctorUserId);

    // Load full profiles
    PatientProfile patientProfile = patientProfileRepository.findByPatient_Id(patientUserId);
    DoctorProfile doctorProfile = doctorProfileRepository.findByDoctor_Id(doctorUserId);

    switch (role) {

      case ADMIN:
      case DOCTOR:
        // 1. Full Patient Info
        dto.setPatientInfo(new PatientInfo(
            patientProfile.getPatientId(),
            patientProfile.getFirstName(),
            patientProfile.getLastName(),
            patientProfile.getAge(),
            patientProfile.getGender(),
            patientProfile.getSymptom(),
            patientProfile.getMedicalHistory(),
            patientProfile.getAllergies(),
            patientProfile.getCurrentMedications(),
            patientProfile.getTriagePriority()
        ));

        // 2. Full Doctor Info
        dto.setDoctorInfo(new DoctorInfo(
            doctorProfile.getDoctorId(),
            doctorProfile.getFirstName(),
            doctorProfile.getLastName(),
            doctorProfile.getSpecialty(),
            doctorProfile.getLicenseNumber(),
            doctorProfile.getWorkTime()
        ));
        break;

      case PATIENT:
        // Patient sees only limited doctor info
        dto.setLimitedDoctorInfo(new LimitedDoctorInfo(
            doctorProfile.getFirstName(),
            doctorProfile.getLastName(),
            doctorProfile.getSpecialty()
        ));
        break;
    }

    return dto;
  }

}
