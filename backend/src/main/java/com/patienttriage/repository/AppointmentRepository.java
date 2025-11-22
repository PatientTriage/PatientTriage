package com.patienttriage.repository;

import com.patienttriage.entity.Appointment;
import com.patienttriage.entity.AppointmentStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  /**
   * Find all appointments belongs to a specific patientId
   * @param patientId the patient id (unique in patient_profile)
   * @return a list of appointment for this patient
   */
  List<Appointment> findByPatient_Id(Long patientId);

  /**
   * Find all appointments belongs to a specific doctorId
   * @param doctorId the doctor id (unique in doctor_profile)
   * @return a list of appointment for this doctor
   */
  List<Appointment> findByDoctor_Id(Long doctorId);

  /**
   * Find all appointments belongs to a specific doctorId and patientId
   * @param patientId the patientId which are unique in the patient_profile
   * @param doctorId doctorId which are unique in the doctor_profile
   * @return a list of appointment for the specific doctor with the specific doctor
   */
  List<Appointment> findByPatient_IdAndDoctor_Id(Long patientId, Long doctorId);

  /**
   * Find all appointments belongs to a specific id and patientId for permission checks
   * @param patientId the patientId which are unique in the patient_profile
   * @param id User id which are unique in the user table
   * @return a list of appointment for the specific doctor with the specific doctor
   */
  List<Appointment> findByIdAndPatient_Id(Long id, Long patientId);

  /**
   * Find all appointments belongs to a specific id and doctorId for permission checks
   * @param doctorId the doctorId which are unique in the patient_profile
   * @param id User id which are unique in the user table
   * @return a list of appointment for the specific doctor with the specific doctor
   */
  List<Appointment> findByIdAndDoctor_Id(Long id, Long doctorId);

  /**
   * Find all appointments for a doctor with a specific status.
   * @param doctorId the doctor id (FK to users.id)
   * @param status the appointment status
   * @return a list of appointments matching doctor and status
   */
  List<Appointment> findByDoctor_IdAndStatus(Long doctorId, AppointmentStatus status);

}
