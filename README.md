# PlaceSync: College Placement Management System

PlaceSync is a 2-tier application built with Java Swing and MySQL, designed to streamline the recruitment process in colleges. It features role-based access for Admins and Students, automated eligibility checks, and real-time placement status tracking.

## 🚀 Features
- **Role-based Access Control**: Secure login for Students and Administrators.
- **Eligibility Engine**: Automatic filtering based on CGPA and other criteria.
- **Placement Tracking**: Real-time updates on application and interview status.
- **Admin Dashboard**: Comprehensive tools for managing companies, jobs, and student data.

## 📁 Project Structure
- `src/`: All Java source files (Auth, DAO, Model, UI, Util).
- `res/`: Icons, images, and configuration files.
- `sql/`: Database schema and scripts.
- `docs/`: Technical documentation.

## 🛠️ Tech Stack
- **Frontend**: Java Swing (FlatLaf for modern UI)
- **Backend**: MySQL 8.x
- **Driver**: MySQL Connector/J
- **Build Tool**: Maven (Recommended)

## 🚦 Getting Started
1. Clone the repository.
2. Import the database schema from `sql/schema.sql`.
3. Configure database credentials in `src/main/resources/config.properties`.
4. Run `Main.java` or use `mvn exec:java`.
