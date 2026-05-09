package backend.queries;

import backend.models.Student;
import backend.models.Job;
import backend.models.JobApplication;
import backend.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public Student getStudentByUserId(int userId) {
        String query = "SELECT * FROM Students WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("branch"),
                    rs.getDouble("cgpa"),
                    rs.getString("resume_path"),
                    rs.getString("placement_status")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<JobApplication> getApplicationsByStudentId(int studentId) {
        List<JobApplication> apps = new ArrayList<>();
        String query = "SELECT a.application_id, c.name as company_name, j.role, a.status " +
                       "FROM Applications a " +
                       "JOIN Jobs j ON a.job_id = j.job_id " +
                       "JOIN Companies c ON j.company_id = c.company_id " +
                       "WHERE a.student_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                apps.add(new JobApplication(
                    rs.getInt("application_id"),
                    rs.getString("company_name"),
                    rs.getString("role"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public List<Job> getAvailableJobs() {
        return searchJobs("", "", 0);
    }

    public List<Job> searchJobs(String company, String role, double minPackage) {
        List<Job> jobs = new ArrayList<>();
        StringBuilder query = new StringBuilder(
            "SELECT j.*, c.name as company_name FROM Jobs j " +
            "JOIN Companies c ON j.company_id = c.company_id WHERE 1=1 "
        );
        
        if (company != null && !company.isEmpty()) query.append("AND c.name LIKE ? ");
        if (role != null && !role.isEmpty()) query.append("AND j.role LIKE ? ");
        if (minPackage > 0) query.append("AND j.package >= ? ");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
            
            int paramIndex = 1;
            if (company != null && !company.isEmpty()) pstmt.setString(paramIndex++, "%" + company + "%");
            if (role != null && !role.isEmpty()) pstmt.setString(paramIndex++, "%" + role + "%");
            if (minPackage > 0) pstmt.setDouble(paramIndex++, minPackage);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobs.add(new Job(
                    rs.getInt("job_id"),
                    rs.getInt("company_id"),
                    rs.getString("company_name"),
                    rs.getString("role"),
                    "",
                    rs.getDouble("package"),
                    rs.getDouble("eligibility_cgpa"),
                    ""
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }

    public boolean applyForJob(int studentId, int jobId) {
        String query = "INSERT INTO Applications (student_id, job_id, status) VALUES (?, ?, 'Pending')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, jobId);
            boolean success = pstmt.executeUpdate() > 0;
            if (success) {
                notifyAdminsOfNewApplication(studentId, jobId);
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void notifyAdminsOfNewApplication(int studentId, int jobId) {
        String query = "SELECT s.name as student, c.name as company, j.role FROM Students s " +
                      "JOIN Jobs j ON j.job_id = ? " +
                      "JOIN Companies c ON j.company_id = c.company_id " +
                      "WHERE s.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String student = rs.getString("student");
                String company = rs.getString("company");
                String role = rs.getString("role");
                String message = "New Application: " + student + " applied for " + role + " at " + company + ".";
                
                // Notify all admins
                String adminQuery = "SELECT user_id FROM Users WHERE role = 'Admin'";
                try (Statement stmt = conn.createStatement();
                     ResultSet ars = stmt.executeQuery(adminQuery)) {
                    NotificationDAO nDao = new NotificationDAO();
                    while (ars.next()) {
                        nDao.sendNotification(ars.getInt("user_id"), message);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean updateResumePath(int studentId, String path) {
        String query = "UPDATE Students SET resume_path = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, path);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePlacementStatus(int studentId, String status) {
        String query = "UPDATE Students SET placement_status = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, status);
            pstmt.setInt(2, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("branch"),
                    rs.getDouble("cgpa"),
                    rs.getString("resume_path"),
                    rs.getString("placement_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
}


