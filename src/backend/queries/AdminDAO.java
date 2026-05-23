package backend.queries;

import backend.utils.DBConnection;
import backend.models.Company;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDAO {
    
    // --- Dashboard Statistics ---
    public Map<String, Integer> getDashboardStats() {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM Students");
            if (rs.next()) stats.put("total_students", rs.getInt(1));
            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM Companies");
            if (rs.next()) stats.put("total_companies", rs.getInt(1));
            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM Students WHERE placement_status = 'Placed'");
            if (rs.next()) stats.put("placed_students", rs.getInt(1));
        } catch (SQLException e) {
            System.err.println("Error fetching dashboard stats: " + e.getMessage());
        }
        return stats;
    }

    public Map<String, Integer> getApplicationStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM Applications GROUP BY status";
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                stats.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching application stats: " + e.getMessage());
        }
        return stats;
    }

    public Map<String, Integer> getPlacementRatioByBranch() {
        Map<String, Integer> branchStats = new HashMap<>();
        String sql = "SELECT branch, COUNT(*) as placed_count FROM Students WHERE placement_status = 'Placed' GROUP BY branch";
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                branchStats.put(rs.getString("branch"), rs.getInt("placed_count"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching placement ratio: " + e.getMessage());
        }
        return branchStats;
    }

    // --- Company Management ---
    public boolean addCompany(String name, String location) {
        String sql = "INSERT INTO Companies (name, location) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding company: " + e.getMessage());
            return false;
        }
    }

    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM Companies";
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                companies.add(new Company(
                    rs.getInt("company_id"),
                    rs.getString("name"),
                    rs.getString("location"),
                    "", ""
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all companies: " + e.getMessage());
        }
        return companies;
    }

    public boolean deleteCompany(int id) {
        String sql = "DELETE FROM Companies WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting company: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCompany(int id, String name, String location) {
        String sql = "UPDATE Companies SET name = ?, location = ? WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, location);
            pstmt.setInt(3, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating company: " + e.getMessage());
            return false;
        }
    }

    // --- Job Management ---
    public boolean addJob(int companyId, String role, double pkg, double cgpa) {
        String sql = "INSERT INTO Jobs (company_id, role, package, eligibility_cgpa) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            pstmt.setString(2, role);
            pstmt.setDouble(3, pkg);
            pstmt.setDouble(4, cgpa);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding job: " + e.getMessage());
            return false;
        }
    }

    public List<Map<String, Object>> getAllJobs() {
        List<Map<String, Object>> jobs = new ArrayList<>();
        String sql = "SELECT j.*, c.name as company_name FROM Jobs j JOIN Companies c ON j.company_id = c.company_id";
        try (Connection conn = DBConnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> job = new HashMap<>();
                job.put("job_id", rs.getInt("job_id"));
                job.put("company_name", rs.getString("company_name"));
                job.put("role", rs.getString("role"));
                job.put("package", rs.getDouble("package"));
                job.put("cgpa", rs.getDouble("eligibility_cgpa"));
                jobs.add(job);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all jobs: " + e.getMessage());
        }
        return jobs;
    }

    // --- Application & Interview Management ---
    public List<Map<String, Object>> getAllApplications() {
        return getAllApplications("");
    }

    public List<Map<String, Object>> getAllApplications(String filterStatus) {
        List<Map<String, Object>> apps = new ArrayList<>();
        String sql = "SELECT a.application_id, s.student_id, s.name as student_name, c.name as company_name, j.role, a.status, a.created_at, i.interview_date, i.interview_time " +
                    "FROM Applications a " +
                    "JOIN Students s ON a.student_id = s.student_id " +
                    "JOIN Jobs j ON a.job_id = j.job_id " +
                    "JOIN Companies c ON j.company_id = c.company_id " +
                    "LEFT JOIN Interviews i ON a.application_id = i.application_id ";
        
        if (filterStatus != null && !filterStatus.isEmpty()) {
            sql += "WHERE a.status = ? ";
        }
        sql += "ORDER BY a.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (filterStatus != null && !filterStatus.isEmpty()) {
                pstmt.setString(1, filterStatus);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> app = new HashMap<>();
                app.put("id", rs.getInt("application_id"));
                app.put("student_id", rs.getInt("student_id"));
                app.put("student", rs.getString("student_name"));
                app.put("company", rs.getString("company_name"));
                app.put("role", rs.getString("role"));
                app.put("status", rs.getString("status"));
                app.put("created_at", rs.getTimestamp("created_at"));
                
                String date = rs.getString("interview_date");
                String time = rs.getString("interview_time");
                if (date != null) {
                    app.put("interview_status", "Scheduled (" + date + " " + time + ")");
                } else {
                    app.put("interview_status", "Not Scheduled");
                }
                
                apps.add(app);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all applications: " + e.getMessage());
        }
        return apps;
    }

    public boolean updateApplicationStatus(int appId, String status) {
        String sql = "UPDATE Applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, status);
                pstmt.setInt(2, appId);
                boolean success = pstmt.executeUpdate() > 0;
                if (success) {
                    sendAppStatusNotification(conn, appId, status);
                    
                    if ("Selected".equalsIgnoreCase(status) || "Placed".equalsIgnoreCase(status)) {
                        createPlacementFromApplication(conn, appId);
                        updateStudentPlacementStatus(conn, appId, "Placed");
                    }
                }
                conn.commit();
                return success;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error updating application status: " + e.getMessage());
            return false;
        }
    }

    private void updateStudentPlacementStatus(Connection conn, int appId, String status) throws SQLException {
        String sql = "UPDATE Students SET placement_status = ? WHERE student_id = (SELECT student_id FROM Applications WHERE application_id = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, appId);
            pstmt.executeUpdate();
        }
    }

    private void createPlacementFromApplication(Connection conn, int appId) throws SQLException {
        String query = "SELECT a.student_id, c.name as company, j.role, j.package " +
                       "FROM Applications a " +
                       "JOIN Jobs j ON a.job_id = j.job_id " +
                       "JOIN Companies c ON j.company_id = c.company_id " +
                       "WHERE a.application_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, appId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    String company = rs.getString("company");
                    String role = rs.getString("role");
                    double pkg = rs.getDouble("package");
                    
                    savePlacementDetails(conn, studentId, company, role, pkg);
                }
            }
        }
    }

    private void savePlacementDetails(Connection conn, int studentId, String company, String role, double pkg) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM Placement_Details WHERE student_id = ?";
        boolean exists = false;
        try (PreparedStatement checkPstmt = conn.prepareStatement(checkQuery)) {
            checkPstmt.setInt(1, studentId);
            try (ResultSet checkRs = checkPstmt.executeQuery()) {
                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    exists = true;
                }
            }
        }
        
        if (exists) {
            String updateSql = "UPDATE Placement_Details SET company_name = ?, role = ?, package = ? WHERE student_id = ?";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setString(1, company);
                updatePstmt.setString(2, role);
                updatePstmt.setDouble(3, pkg);
                updatePstmt.setInt(4, studentId);
                updatePstmt.executeUpdate();
            }
        } else {
            String insertSql = "INSERT INTO Placement_Details (student_id, company_name, role, package) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                insertPstmt.setInt(1, studentId);
                insertPstmt.setString(2, company);
                insertPstmt.setString(3, role);
                insertPstmt.setDouble(4, pkg);
                insertPstmt.executeUpdate();
            }
        }
    }

    public boolean assignPlacement(int studentId, String company, String role, double pkg) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String studentSql = "UPDATE Students SET placement_status = 'Placed' WHERE student_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(studentSql)) {
                    pstmt.setInt(1, studentId);
                    pstmt.executeUpdate();
                }
                
                savePlacementDetails(conn, studentId, company, role, pkg);
                
                String userSql = "SELECT user_id FROM Students WHERE student_id = ?";
                int userId = -1;
                try (PreparedStatement pstmt = conn.prepareStatement(userSql)) {
                    pstmt.setInt(1, studentId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            userId = rs.getInt("user_id");
                        }
                    }
                }
                if (userId != -1) {
                    String msg = "Congratulations! You have been placed at " + company + " as " + role + ".";
                    new NotificationDAO().sendNotification(userId, msg);
                }
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error assigning manual placement: " + e.getMessage());
            return false;
        }
    }

    private void sendAppStatusNotification(Connection conn, int appId, String status) throws SQLException {
        String query = "SELECT s.user_id, c.name as company, j.role FROM Applications a " +
                       "JOIN Students s ON a.student_id = s.student_id " +
                       "JOIN Jobs j ON a.job_id = j.job_id " +
                       "JOIN Companies c ON j.company_id = c.company_id " +
                       "WHERE a.application_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, appId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String company = rs.getString("company");
                    String role = rs.getString("role");
                    
                    String message = "Your application for " + role + " at " + company + " has been marked as " + status + ".";
                    if ("Shortlisted".equalsIgnoreCase(status)) {
                        message = "You have been shortlisted for the " + role + " role at " + company + ".";
                    } else if ("Rejected".equalsIgnoreCase(status)) {
                        message = "Your application has been rejected.";
                    } else if ("Selected".equalsIgnoreCase(status) || "Placed".equalsIgnoreCase(status)) {
                        message = "Congratulations! You have been placed as " + role + " at " + company + ".";
                    }
                    
                    new NotificationDAO().sendNotification(userId, message);
                }
            }
        }
    }

    public boolean scheduleInterview(int appId, String date, String time, String mode, String location) {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                String checkSql = "SELECT interview_id FROM Interviews WHERE application_id = ?";
                int interviewId = -1;
                try (PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
                    pstmt.setInt(1, appId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            interviewId = rs.getInt("interview_id");
                        }
                    }
                }
                
                if (interviewId != -1) {
                    String updateSql = "UPDATE Interviews SET interview_date = ?, interview_time = ?, interview_mode = ?, interview_location = ? WHERE interview_id = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
                        pstmt.setString(1, date);
                        pstmt.setString(2, time);
                        pstmt.setString(3, mode);
                        pstmt.setString(4, location);
                        pstmt.setInt(5, interviewId);
                        pstmt.executeUpdate();
                    }
                } else {
                    String insertSql = "INSERT INTO Interviews (application_id, interview_date, interview_time, interview_mode, interview_location) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
                        pstmt.setInt(1, appId);
                        pstmt.setString(2, date);
                        pstmt.setString(3, time);
                        pstmt.setString(4, mode);
                        pstmt.setString(5, location);
                        pstmt.executeUpdate();
                    }
                }
                
                String updateAppSql = "UPDATE Applications SET status = 'Interview Scheduled' WHERE application_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateAppSql)) {
                    pstmt.setInt(1, appId);
                    pstmt.executeUpdate();
                }
                
                String userSql = "SELECT s.user_id FROM Applications a JOIN Students s ON a.student_id = s.student_id WHERE a.application_id = ?";
                int userId = -1;
                try (PreparedStatement pstmt = conn.prepareStatement(userSql)) {
                    pstmt.setInt(1, appId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            userId = rs.getInt("user_id");
                        }
                    }
                }
                if (userId != -1) {
                    String msg = "Your interview is scheduled on " + date + " at " + time + ".";
                    new NotificationDAO().sendNotification(userId, msg);
                }
                
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error scheduling interview: " + e.getMessage());
            return false;
        }
    }
}
