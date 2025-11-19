package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="records")
public class Record {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name="patient_id", nullable=false)
  private User patient;
  private String symptoms;   //visitReason
  private String medicalHistory;
  private String allergies;
  private String currentMedications;

  public Record(){}

  public Record(String symptoms, String medicalHistory, String allergies, String currentMedications) {
    this.symptoms = symptoms;
    this.medicalHistory = medicalHistory;
    this.allergies = allergies;
    this.currentMedications = currentMedications;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getPatient() {
    return patient;
  }

  public void setPatient(User patient) {
    this.patient = patient;
  }

  public String getSymptoms() {
    return symptoms;
  }

  public void setSymptoms(String symptoms) {
    this.symptoms = symptoms;
  }

  public String getMedicalHistory() {
    return medicalHistory;
  }

  public void setMedicalHistory(String medicalHistory) {
    this.medicalHistory = medicalHistory;
  }

  public String getAllergies() {
    return allergies;
  }

  public void setAllergies(String allergies) {
    this.allergies = allergies;
  }

  public String getCurrentMedications() {
    return currentMedications;
  }

  public void setCurrentMedications(String currentMedications) {
    this.currentMedications = currentMedications;
  }
}
