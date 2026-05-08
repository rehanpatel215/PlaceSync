# Implementation Plan: College Placement Management System

This document outlines the systematic approach to building the **College Placement Management System** using Java Swing, JDBC, and MySQL.

## 🎯 Project Overview
A 2-tier application designed to streamline the recruitment process in colleges. It features role-based access for Admins and Students, automated eligibility checks, and real-time placement status tracking.

---

## 🏗️ Phase 1: Database Architecture & Backend Setup
**Goal:** Establish a robust data layer with automated business logic.

- [ ] **Schema Design:**
    - `Users`: `user_id` (PK), `username`, `password`, `role` (Admin/Student).
    - `Students`: `student_id` (PK), `user_id` (FK), `name`, `branch`, `cgpa`, `resume_path`, `placement_status`.
    - `Admin`: `admin_id` (PK), `user_id` (FK).
    - `Companies`: `company_id` (PK), `name`, `location`.
    - `Jobs`: `job_id` (PK), `company_id` (FK), `role`, `package`, `eligibility_cgpa`.
    - `Applications`: `application_id` (PK), `student_id` (FK), `job_id` (FK), `status` (Pending/Selected/Rejected).
    - `Interviews`: `interview_id` (PK), `application_id` (FK), `interview_date`, `interview_time`.
- [ ] **Triggers:**
    - Create `update_placement_status`: Automatically set `Students.placement_status = 'Placed'` when `Applications.status` is updated to `'Selected'`.
- [ ] **Stored Procedures:**
    - Create `GetEligibleStudents`: A procedure that takes `job_id` and returns a list of students whose CGPA meets the requirement.

---

## ⚙️ Phase 2: Core Java Infrastructure (JDBC & Auth)
**Goal:** Create a reliable connection between the UI and the Database.

- [ ] **Connection Management:**
    - Implement a Singleton `DBConnection` class to manage MySQL connectivity via JDBC.
- [ ] **DAO (Data Access Object) Layer:**
    - `UserDAO`: Handle login verification and role retrieval.
    - `StudentDAO` / `AdminDAO`: Basic profile fetching logic.
- [ ] **Security:**
    - Password hashing (optional but recommended).
    - Session tracking (store current `UserID` and `Role` globally).

---

## 🎨 Phase 3: Frontend Architecture & UI Design
**Goal:** Create a modern, responsive-feeling UI using Java Swing.

- [ ] **Base Framework:**
    - Implement a `MainFrame` using `CardLayout` to switch between modules (Login, Admin, Student).
    - Create a `NavigationManager` to handle screen transitions.
- [ ] **Custom Component Library:**
    - Design `RoundedButton`, `StyledTextField`, and `GradientPanel` for a premium look.
    - Setup a `Theme` class for consistent colors (Primary, Secondary, Background).
- [ ] **Layout System:**
    - Use `GridBagLayout` and `MigLayout` (or `BorderLayout` nesting) for responsive-like behavior.

---

## 👨‍💼 Phase 4: Admin Module Development
**Goal:** Provide full administrative control over the placement lifecycle.

- [ ] **Admin Dashboard:**
    - Display statistics: Total Students, Total Companies, Placed vs. Unplaced ratio.
- [ ] **Company & Job Management:**
    - Forms to Add/Update/Delete Company details.
    - Interface to post Job openings with `eligibility_cgpa` and `package`.
- [ ] **Application & Interview Management:**
    - View all applicants for a specific job.
    - "Schedule Interview" utility (updates the `Interviews` table).
    - "Update Result" utility (triggers the placement status update).

---

## 🎓 Phase 5: Student Module Development
**Goal:** Enable students to discover and apply for opportunities.

- [ ] **Student Dashboard:**
    - View personal profile and placement status.
- [ ] **Job Listings & Eligibility:**
    - Display available jobs.
    - Implement logic to check CGPA before allowing the "Apply" action (using the stored procedure).
- [ ] **Application Tracker:**
    - A view for students to see the status of their applications (Pending, Interview Scheduled, Selected, Rejected).
- [ ] **Resume Upload:**
    - File picker to store the local path of the resume in the database.

---

## 📊 Phase 6: Advanced Features & Filtering
**Goal:** Enhance usability and data accessibility.

- [ ] **Filtering System:**
    - Admin tool to filter student lists by Branch, CGPA range, and Placement Status.
- [ ] **Dashboard Visualization:**
    - Simple bar charts or tables representing placement trends across different branches.
- [ ] **Report Generation:**
    - (Optional) Export list of placed students to CSV or PDF.

---

## 🧪 Phase 7: Testing & Final Polish
**Goal:** Ensure stability and user-friendly experience.

- [ ] **Unit Testing:**
    - Test JDBC connections and SQL queries.
    - Validate the Trigger logic by simulating a 'Selected' application status.
- [ ] **UI/UX Polishing:**
    - Apply a modern LookAndFeel (e.g., FlatLaf).
    - Add error handling for SQL exceptions (e.g., duplicate usernames).
- [ ] **Documentation:**
    - Finalize README and Database dump (`.sql` file).

---

## 🛠️ Tech Stack Recap
- **Frontend:** Java Swing
- **Backend:** MySQL 8.x
- **Driver:** MySQL Connector/J (JDBC)
- **Architecture:** 2-Tier Client-Server
