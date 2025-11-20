package repository;

import entity.Appointment;
import entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

  /**
   * Find all appointments belongs to a specific patientId
   * @param patientId the patient id (unique in patient_profile)
   * @return a list of appointment for this patient
   */
  List<Appointment> findByPatientId(Long patientId);

  /**
   * Find all appointments belongs to a specific doctorId
   * @param doctorId the doctor id (unique in doctor_profile)
   * @return a list of appointment for this doctor
   */
  List<Appointment> findByDoctorId(Long doctorId);

  /**
   * Find all appointments belongs to a specific doctorId and patientId
   * @param patientId the patientId which are unique in the patient_profile
   * @param doctorId doctorId which are unique in the doctor_profile
   * @return a list of appointment for the specific doctor with the specific doctor
   */
  List<Appointment> findByPatientIdAndDoctorId(Long patientId, Long doctorId);

  // add any if necessary
}
