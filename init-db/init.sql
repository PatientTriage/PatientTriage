-- ============================================================
-- Drop existing tables (for resetting dev environment)
-- ============================================================
DROP TABLE IF EXISTS records CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Create patients table
-- ============================================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    age INT,
    gender VARCHAR(20),
    address VARCHAR(255),
    phone VARCHAR(50),
    triage_priority VARCHAR(20),
    created_at TIMESTAMP DEFAULT NOW()
);


-- ============================================================
-- Create records table
-- ============================================================
CREATE TABLE records (
    id BIGSERIAL PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    symptom TEXT,
    medical_history TEXT,
    allergies TEXT,
    current_medications TEXT,
    created_at TIMESTAMP DEFAULT NOW(),

    -- if patient are delete, we will also delete the records
    CONSTRAINT fk_patient
        FOREIGN KEY (patient_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);
