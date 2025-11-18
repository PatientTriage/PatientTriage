package com.patienttriage.dental.controller;

import com.patienttriage.dental.dto.PatientRequest;
import com.patienttriage.dental.service.PatientService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @PostMapping
  public PatientRequest addPatient(@Valid @RequestBody PatientRequest request) {
    return patientService.addPatient(request);
  }

  @GetMapping
  public List<PatientRequest> getPatients() {
    return patientService.getAllPatients();
  }
}
