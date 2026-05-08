-- Database Schema for PlaceSync
CREATE DATABASE IF NOT EXISTS placesync;
USE placesync;

-- Tables based on implementation plan
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Student') NOT NULL
);

CREATE TABLE Students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    name VARCHAR(100) NOT NULL,
    branch VARCHAR(50),
    cgpa DECIMAL(3,2),
    resume_path VARCHAR(255),
    placement_status ENUM('Placed', 'Unplaced') DEFAULT 'Unplaced',
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

CREATE TABLE Companies (
    company_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    location VARCHAR(100)
);

CREATE TABLE Jobs (
    job_id INT PRIMARY KEY AUTO_INCREMENT,
    company_id INT,
    role VARCHAR(100) NOT NULL,
    package DECIMAL(10,2),
    eligibility_cgpa DECIMAL(3,2),
    FOREIGN KEY (company_id) REFERENCES Companies(company_id)
);

CREATE TABLE Applications (
    application_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    job_id INT,
    status ENUM('Pending', 'Selected', 'Rejected') DEFAULT 'Pending',
    FOREIGN KEY (student_id) REFERENCES Students(student_id),
    FOREIGN KEY (job_id) REFERENCES Jobs(job_id)
);

CREATE TABLE Interviews (
    interview_id INT PRIMARY KEY AUTO_INCREMENT,
    application_id INT,
    interview_date DATE,
    interview_time TIME,
    FOREIGN KEY (application_id) REFERENCES Applications(application_id)
);

-- Trigger to update placement status
DELIMITER //
CREATE TRIGGER update_placement_status
AFTER UPDATE ON Applications
FOR EACH ROW
BEGIN
    IF NEW.status = 'Selected' THEN
        UPDATE Students SET placement_status = 'Placed' WHERE student_id = NEW.student_id;
    END IF;
END //

-- Procedure to get students eligible for a specific job
CREATE PROCEDURE GetEligibleStudents(IN input_job_id INT)
BEGIN
    SELECT s.* 
    FROM Students s
    JOIN Jobs j ON s.cgpa >= j.eligibility_cgpa
    WHERE j.job_id = input_job_id;
END //
DELIMITER ;
