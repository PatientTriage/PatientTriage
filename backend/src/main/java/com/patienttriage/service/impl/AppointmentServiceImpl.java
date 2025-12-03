package com.patienttriage.service.impl;

import com.patienttriage.dto.AppointmentRequest;
import com.patienttriage.dto.AppointmentResponse;
import com.patienttriage.dto.DoctorInfo;
import com.patienttriage.dto.LimitedDoctorInfo;
import com.patienttriage.dto.PatientInfo;
import com.patienttriage.entity.Appointment;
import com.patienttriage.entity.DoctorProfile;
import com.patienttriage.entity.PatientProfile;
import com.patienttriage.entity.User;
import com.patienttriage.entity.UserRole;
import com.patienttriage.repository.AppointmentRepository;
import com.patienttriage.repository.UserRepository;
import com.patienttriage.service.AppointmentService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  private final AppointmentRepository appointmentRepository;
  private final UserRepository userRepository;

  // Constructor injection
  public AppointmentServiceImpl(AppointmentRepository appointmentRepository, 
                                UserRepository userRepository) {
    this.appointmentRepository = appointmentRepository;
    this.userRepository = userRepository;
  }

  @Override
  @Transactional
  public void createAppointment(AppointmentRequest request, Long currentUserId) {
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
    LocalDateTime appointmentTime = request.getStartDateTime();
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
  }

  @Override
  public AppointmentResponse checkAppointment(Long appointmentId, Long currentUserId) {
    // load current user for validation
    User currentUser = userRepository.findById(currentUserId)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + currentUserId));

    Appointment appointment = appointmentRepository.findById(appointmentId)
        .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + appointmentId));

    UserRole currentRole = currentUser.getRole();

    // role validation:
    //patient only can see their own appointment detail
    if (currentRole == UserRole.PATIENT) {
      if (!appointment.getPatient().getId().equals(currentUserId)) {
        throw new RuntimeException("Doctors can only create appointments for themselves");
      }
    }

    // doctor only can see their own appointment detail
    if (currentRole == UserRole.DOCTOR) {
      if (!appointment.getDoctor().getId().equals(currentUserId)) {
        throw new RuntimeException("Patients can only create appointments for themselves");
      }
    }

    // create response using constructor (since IDs are immutable, no setters)
    AppointmentResponse response = new AppointmentResponse(
        appointment.getId(),
        appointment.getPatient().getId(),
        appointment.getDoctor().getId(),
        appointment.getAppointmentTime(),
        appointment.getReason(),
        // TODO: solve this by search for how to use enumerate entity type change to String that are
        //  same in the data schema.
        appointment.getStatus(),
        appointment.getCreatedAt()
    );

    // corresponding to different role, return different type of response
    // Patient can get basic appointment info + limited doctor info
    if (currentRole == UserRole.PATIENT) {
      // Get doctor's profile from the appointment
      User doctor = appointment.getDoctor();
      DoctorProfile doctorProfile = doctor.getDoctorProfile();

      if (doctorProfile != null) {
        LimitedDoctorInfo limitedDoctorInfo = new LimitedDoctorInfo(
            doctorProfile.getFirstName(),
            doctorProfile.getLastName(),
            doctorProfile.getSpecialty()
        );
        response.setLimitedDoctorInfo(limitedDoctorInfo);
      }
    } else if (currentRole == UserRole.DOCTOR) {
      // DOCTOR can see current appointment's patient's info
      // Get patient profile
      User patient = appointment.getPatient();
      PatientProfile patientProfile = patient.getPatientProfile();

      if (patient.getPatientProfile() != null) {
        PatientInfo patientInfo = new PatientInfo(
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
        );
        response.setPatientInfo(patientInfo);
      }
    }
    return response;
  }


  @Override
  public AppointmentResponse updateAppointment(Long appointmentId, AppointmentRequest request,
      Long currentUserId) {
      // TODO: Implement updateAppointment
      throw new UnsupportedOperationException("Not implemented yet");
    }



  @Override
  public AppointmentResponse cancelAppointment(Long appointmentId, Long currentUserId) {
    // TODO: Implement cancelAppointmen
    throw new UnsupportedOperationException("Not implemented yet");
  }

//  @Override
//  public List<AppointmentResponse> getAppointmentsByPatientId(Long patientId, Long currentUserId) {
//    // TODO: Implement getAppointmentsByPatientId
//    throw new UnsupportedOperationException("Not implemented yet");
//  }
//
//  @Override
//  public List<AppointmentResponse> getAppointmentsByDoctorId(Long doctorId, Long currentUserId) {
//    // TODO: Implement getAppointmentsByDoctorId
//    throw new UnsupportedOperationException("Not implemented yet");
//  }
//
//  @Override
//  public List<AppointmentResponse> getAllAppointments(Long currentUserId) {
//    // TODO: Implement getAllAppointments
//    throw new UnsupportedOperationException("Not implemented yet");
//    }

}
