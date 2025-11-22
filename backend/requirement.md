For the appointment functionality, I hope to have:

1. createAppointment

2. checkAppointment

3. updateAppointment

4. cancelAppoinment

But since I have three UserRole for the whole project, I want to give them constrain for the appointment:
1. createAppointment: all the roles can create appointment, but:
   1.1. patient only can make appointment for themselves 
   1.2. doctors only can make appointment for themselves

2. checkAppoinment: every role can checkAppointment, but:
    2.1. patient can checkAppointment all info(appointment table columns), also can check the related doctor's info, but limited to only doctor's first, last name and the specialty
    2.2. doctor can checkAppoinment all info and the related patient's all profile
    2.3. admin can check everything include doctor's profile and patient's profile

3. updateAppoinment: every role can update appoinment, but:
    3.1. for patients and doctors, they only can update the appointment that related to themselves
    3.2. admin can update any appointment
    The appointment can be updated only when it are not cancelled or completed

4. cancelAppointment: every role can cancel appointment, but:
    4.1. patients and doctors only can cancel their own appointment
    4.2. admin can cancel any appointment
    For appointment cancelling, just change the status to CANCELLED. 