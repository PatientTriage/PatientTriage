package com.patienttriage.dental.repository;

import com.patienttriage.dental.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public class PatientRepository extends JpaRepository<Patient, Long> {

}
