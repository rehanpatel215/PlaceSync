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
        String checkQuery = "SELECT COUNT(*) FROM Applications a " +
                            "JOIN Jobs j ON a.job_id = j.job_id " +
                            "WHERE a.student_id = ? AND j.company_id = (SELECT company_id FROM Jobs WHERE job_id = ?)";
        
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement checkPstmt = conn.prepareStatement(checkQuery)) {
                checkPstmt.setInt(1, studentId);
                checkPstmt.setInt(2, jobId);
                try (ResultSet rs = checkPstmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false;
                    }
                }
            }

            int companyId = -1;
            String compQuery = "SELECT company_id FROM Jobs WHERE job_id = ?";
            try (PreparedStatement compPstmt = conn.prepareStatement(compQuery)) {
                compPstmt.setInt(1, jobId);
                try (ResultSet rs = compPstmt.executeQuery()) {
                    if (rs.next()) {
                        companyId = rs.getInt("company_id");
                    }
                }
            }

            String query = "INSERT INTO Applications (student_id, job_id, company_id, status) VALUES (?, ?, ?, 'Applied')";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, studentId);
                pstmt.setInt(2, jobId);
                pstmt.setInt(3, companyId);
                boolean success = pstmt.executeUpdate() > 0;
                if (success) {
                    notifyAdminsOfNewApplication(studentId, jobId);
                }
                return success;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void notifyAdminsOfNewApplication(int studentId, int jobId) {
        String student = "";
        String company = "";
        String role = "";
        
        String query = "SELECT s.name as student, c.name as company, j.role FROM Students s " +
                      "JOIN Jobs j ON j.job_id = ? " +
                      "JOIN Companies c ON j.company_id = c.company_id " +
                      "WHERE s.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    student = rs.getString("student");
                    company = rs.getString("company");
                    role = rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (student.isEmpty()) return;

        String message = "New Application: " + student + " applied for " + role + " at " + company + ".";
        java.util.List<Integer> adminIds = new java.util.ArrayList<>();
        String adminQuery = "SELECT user_id FROM Users WHERE role = 'Admin'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(adminQuery)) {
            while (rs.next()) {
                adminIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NotificationDAO nDao = new NotificationDAO();
        for (int adminId : adminIds) {
            nDao.sendNotification(adminId, message);
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

    public java.util.Map<String, Object> getPlacementDetails(int studentId) {
        String query = "SELECT company_name, role, package FROM Placement_Details WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    java.util.Map<String, Object> details = new java.util.HashMap<>();
                    details.put("company_name", rs.getString("company_name"));
                    details.put("role", rs.getString("role"));
                    details.put("package", rs.getDouble("package"));
                    return details;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public java.util.List<java.util.Map<String, Object>> getStudentInterviews(int studentId) {
        java.util.List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        String query = "SELECT i.interview_date, i.interview_time, i.interview_mode, i.interview_location, c.name as company, j.role " +
                       "FROM Interviews i " +
                       "JOIN Applications a ON i.application_id = a.application_id " +
                       "JOIN Jobs j ON a.job_id = j.job_id " +
                       "JOIN Companies c ON j.company_id = c.company_id " +
                       "WHERE a.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, studentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    java.util.Map<String, Object> item = new java.util.HashMap<>();
                    item.put("interview_date", rs.getString("interview_date"));
                    item.put("interview_time", rs.getString("interview_time"));
                    item.put("interview_mode", rs.getString("interview_mode"));
                    item.put("interview_location", rs.getString("interview_location"));
                    item.put("company", rs.getString("company"));
                    item.put("role", rs.getString("role"));
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}


