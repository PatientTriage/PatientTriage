package com.patienttriage.repository;

import com.patienttriage.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {

  // find PatientProfile by UserId
  PatientProfile findByPatient_Id(Long patientId);
}
