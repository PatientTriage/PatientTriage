package com.patienttriage.repository;

import com.patienttriage.entity.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

  // find DoctorProfile by UserId
  DoctorProfile findByDoctor_Id(Long doctorId);
}
