package com.patienttriage.dental.service;

import com.patienttriage.dental.dto.PatientRequest;
import com.patienttriage.dental.entity.Patient;
import com.patienttriage.dental.repository.PatientRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

  private final PatientRepository repository;

  public PatientService(PatientRepository repository) {
    this.repository = repository;
  }

  public PatientRequest addPatient(PatientRequest request) {
    Patient patient = new Patient();
    //todo
  }

  public List<PatientRequest> getAllPatients() {
    return repository.findAll();
  }
}
