package com.patienttriage.dental.controller;

import com.patienttriage.dental.service.UserService;
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
  private final UserService userService;

  public PatientController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public PatientRequest addPatient(@Valid @RequestBody PatientRequest request) {
    return userService.addPatient(request);
  }

  @GetMapping
  public List<PatientRequest> getPatients() {
    return userService.getAllPatients();
  }
}
