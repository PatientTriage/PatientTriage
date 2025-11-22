
-- ============================================
-- 1. DROP tables + types (ONLY FOR DEVELOPMENT)
-- delete this step in the future to save data
-- ============================================
DROP TABLE IF EXISTS admin_profile CASCADE;
DROP TABLE IF EXISTS doctor_profile CASCADE;
DROP TABLE IF EXISTS patient_profile CASCADE;
DROP TABLE IF EXISTS users CASCADE;

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