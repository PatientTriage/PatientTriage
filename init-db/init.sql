--
-- -- ============================================
-- -- 1. DROP tables + types (ONLY FOR DEVELOPMENT)
-- -- delete this step in the future to save data
-- -- ============================================
-- DROP TABLE IF EXISTS admin_profile CASCADE;
-- DROP TABLE IF EXISTS doctor_profile CASCADE;
-- DROP TABLE IF EXISTS patient_profile CASCADE;
-- DROP TABLE IF EXISTS users CASCADE;

-- ============================================
-- 2. Create main "users" table
-- ============================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role varchar(20) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- ============================================
-- 3. Create patient_profile (1:1 with users)
-- ============================================
CREATE TABLE patient_profile (
    patient_id BIGINT PRIMARY KEY,

    first_name VARCHAR(100),
    last_name VARCHAR(100),
    age INT,
    gender VARCHAR(20),
    symptom TEXT,
    medical_history TEXT,
    allergies TEXT,
    current_medications TEXT,
    triage_priority  VARCHAR(20),

    CONSTRAINT fk_patient_user
    -- the patient_id should always as same as id in users table
    -- foreign key: a rule that connects two tables together and ensures that data between them is
    -- valid
     FOREIGN KEY (patient_id)
         REFERENCES users(id)
         ON DELETE CASCADE
);

-- ============================================
-- 4. Create doctor_profile (1:1 with users)
-- ============================================
CREATE TABLE doctor_profile (
    doctor_id BIGINT PRIMARY KEY,

    first_name VARCHAR(100),
    last_name VARCHAR(100),
    specialty VARCHAR(100),
    license_number VARCHAR(50),
    work_time VARCHAR(100),   -- e.g., "Mon-Fri 9:00-17:00",

    CONSTRAINT fk_doctor_user -- make it easy to join with the users table,
    -- the doctor_id should always as same as id in users table
        FOREIGN KEY (doctor_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);

-- ============================================
-- 5. Create admin_profile (1:1 with users)
-- ============================================
CREATE TABLE admin_profile (
    admin_id BIGINT PRIMARY KEY,

    first_name VARCHAR(100),
    last_name VARCHAR(100),
    audit_logs VARCHAR(100),
    permissions VARCHAR(100),

    CONSTRAINT fk_admin_user -- make it easy to join with the users table,
    -- the doctor_id should always as same as id in users table
        FOREIGN KEY (admin_id)
           REFERENCES users(id)
           ON DELETE CASCADE
);

-- ===========================================
-- APPOINTMENTS TABLE
-- ===========================================
CREATE TABLE appointments (
      id BIGSERIAL PRIMARY KEY,
      patient_id BIGINT NOT NULL,
      doctor_id BIGINT NOT NULL,
      appointment_time TIMESTAMP NOT NULL,
      reason TEXT,
      status VARCHAR(20) DEFAULT 'SCHEDULED',
      created_at TIMESTAMP DEFAULT NOW(),

      CONSTRAINT fk_appt_patient
          FOREIGN KEY(patient_id)
              REFERENCES users(id)
              ON DELETE CASCADE,

      CONSTRAINT fk_appt_doctor
          FOREIGN KEY(doctor_id)
              REFERENCES users(id)
              ON DELETE CASCADE
);

-- ===========================================
-- SEED DATA FOR TESTING
-- ===========================================
-- NOTE: Users should be created via POST /api/users/register API
-- This ensures correct BCrypt password hashing.
-- After creating users via API, the profiles and appointments below will work.
-- 
-- Example users to create:
-- - admin@test.com / admin123 (ADMIN)
-- - patient1@test.com through patient5@test.com / patient123 (PATIENT)  
-- - doctor1@test.com through doctor3@test.com / doctor123 (DOCTOR)

-- Insert Patient Profiles (only if users exist)
INSERT INTO patient_profile (patient_id, first_name, last_name, age, gender, symptom, medical_history, allergies, current_medications, triage_priority) 
SELECT id, 'John', 'Doe', 35, 'Male', 'Headache and fever', 'Hypertension', 'Peanuts', 'Aspirin', 'Medium' FROM users WHERE username = 'patient1@test.com'
ON CONFLICT (patient_id) DO NOTHING;
INSERT INTO patient_profile (patient_id, first_name, last_name, age, gender, symptom, medical_history, allergies, current_medications, triage_priority) 
SELECT id, 'Jane', 'Smith', 28, 'Female', 'Chest pain', 'None', 'None', 'None', 'High' FROM users WHERE username = 'patient2@test.com'
ON CONFLICT (patient_id) DO NOTHING;
INSERT INTO patient_profile (patient_id, first_name, last_name, age, gender, symptom, medical_history, allergies, current_medications, triage_priority) 
SELECT id, 'Bob', 'Johnson', 45, 'Male', 'Back pain', 'Diabetes', 'Penicillin', 'Metformin', 'Low' FROM users WHERE username = 'patient3@test.com'
ON CONFLICT (patient_id) DO NOTHING;
INSERT INTO patient_profile (patient_id, first_name, last_name, age, gender, symptom, medical_history, allergies, current_medications, triage_priority) 
SELECT id, 'Alice', 'Williams', 32, 'Female', 'Sore throat', 'Asthma', 'Dust', 'Inhaler', 'Medium' FROM users WHERE username = 'patient4@test.com'
ON CONFLICT (patient_id) DO NOTHING;
INSERT INTO patient_profile (patient_id, first_name, last_name, age, gender, symptom, medical_history, allergies, current_medications, triage_priority) 
SELECT id, 'Charlie', 'Brown', 50, 'Male', 'Joint pain', 'Arthritis', 'None', 'Ibuprofen', 'Low' FROM users WHERE username = 'patient5@test.com'
ON CONFLICT (patient_id) DO NOTHING;

-- Insert Doctor Profiles (only if users exist)
INSERT INTO doctor_profile (doctor_id, first_name, last_name, specialty, license_number, work_time) 
SELECT id, 'Sarah', 'Chen', 'Cardiology', 'MD-12345', 'Mon-Fri 9:00-17:00' FROM users WHERE username = 'doctor1@test.com'
ON CONFLICT (doctor_id) DO NOTHING;
INSERT INTO doctor_profile (doctor_id, first_name, last_name, specialty, license_number, work_time) 
SELECT id, 'Michael', 'Rodriguez', 'Pediatrics', 'MD-23456', 'Mon-Fri 8:00-16:00' FROM users WHERE username = 'doctor2@test.com'
ON CONFLICT (doctor_id) DO NOTHING;
INSERT INTO doctor_profile (doctor_id, first_name, last_name, specialty, license_number, work_time) 
SELECT id, 'Emily', 'Watson', 'General Practice', 'MD-34567', 'Mon-Fri 10:00-18:00' FROM users WHERE username = 'doctor3@test.com'
ON CONFLICT (doctor_id) DO NOTHING;

-- Insert Admin Profile (only if user exists)
INSERT INTO admin_profile (admin_id, first_name, last_name, audit_logs, permissions) 
SELECT id, 'Admin', 'User', 'System logs', 'All permissions' FROM users WHERE username = 'admin@test.com'
ON CONFLICT (admin_id) DO NOTHING;

-- Insert Sample Appointments
-- Future appointments
INSERT INTO appointments (patient_id, doctor_id, appointment_time, reason, status, created_at) VALUES
((SELECT id FROM users WHERE username = 'patient1@test.com'), (SELECT id FROM users WHERE username = 'doctor1@test.com'), NOW() + INTERVAL '1 day' + INTERVAL '10 hours', 'Regular checkup', 'SCHEDULED', NOW()),
((SELECT id FROM users WHERE username = 'patient2@test.com'), (SELECT id FROM users WHERE username = 'doctor1@test.com'), NOW() + INTERVAL '2 days' + INTERVAL '14 hours' + INTERVAL '30 minutes', 'Chest pain evaluation', 'SCHEDULED', NOW()),
((SELECT id FROM users WHERE username = 'patient3@test.com'), (SELECT id FROM users WHERE username = 'doctor2@test.com'), NOW() + INTERVAL '3 days' + INTERVAL '9 hours', 'Back pain consultation', 'SCHEDULED', NOW()),
((SELECT id FROM users WHERE username = 'patient4@test.com'), (SELECT id FROM users WHERE username = 'doctor3@test.com'), NOW() + INTERVAL '4 days' + INTERVAL '11 hours', 'Sore throat examination', 'SCHEDULED', NOW()),
((SELECT id FROM users WHERE username = 'patient5@test.com'), (SELECT id FROM users WHERE username = 'doctor1@test.com'), NOW() + INTERVAL '5 days' + INTERVAL '15 hours', 'Joint pain assessment', 'SCHEDULED', NOW()),
((SELECT id FROM users WHERE username = 'patient1@test.com'), (SELECT id FROM users WHERE username = 'doctor2@test.com'), NOW() + INTERVAL '6 days' + INTERVAL '10 hours' + INTERVAL '30 minutes', 'Follow-up appointment', 'SCHEDULED', NOW()),
((SELECT id FROM users WHERE username = 'patient2@test.com'), (SELECT id FROM users WHERE username = 'doctor3@test.com'), NOW() + INTERVAL '7 days' + INTERVAL '13 hours', 'General consultation', 'SCHEDULED', NOW()),
-- Past appointments (completed)
((SELECT id FROM users WHERE username = 'patient3@test.com'), (SELECT id FROM users WHERE username = 'doctor1@test.com'), NOW() - INTERVAL '5 days' + INTERVAL '10 hours', 'Previous consultation', 'COMPLETED', NOW()),
((SELECT id FROM users WHERE username = 'patient4@test.com'), (SELECT id FROM users WHERE username = 'doctor2@test.com'), NOW() - INTERVAL '3 days' + INTERVAL '14 hours', 'Previous checkup', 'COMPLETED', NOW()),
-- Cancelled appointment
((SELECT id FROM users WHERE username = 'patient5@test.com'), (SELECT id FROM users WHERE username = 'doctor3@test.com'), NOW() + INTERVAL '8 days' + INTERVAL '9 hours', 'Cancelled appointment', 'CANCELLED', NOW());