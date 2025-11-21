package com.patienttriage.service;

import com.patienttriage.entity.Appointment;

public interface AppointmentService {

  /**
   * create a new appointment
   * @param appointment
   */
  void createAppointment(Appointment appointment);

  /**
   * change the appointment status to CANCELLED
   * @param appointment
   */
  void cancelAppointment(Appointment appointment);

  /**
   * update the appointment
   * @param appointment
   */
  void updateAppointment(Appointment appointment);

  /**
   * check the exist appointment
   * @param appointment
   * @return the all info of the appointment
   */
  //TODO: check the return type
  Appointment checkAppointment(Appointment appointment);

}
