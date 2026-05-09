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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return jobs;
    }

    // --- Application & Interview Management ---
    public List<Map<String, Object>> getAllApplications() {
        return getAllApplications("");
    }

    public List<Map<String, Object>> getAllApplications(String filterStatus) {
        List<Map<String, Object>> apps = new ArrayList<>();
        String sql = "SELECT a.application_id, s.name as student_name, c.name as company_name, j.role, a.status " +
                    "FROM Applications a " +
                    "JOIN Students s ON a.student_id = s.student_id " +
                    "JOIN Jobs j ON a.job_id = j.job_id " +
                    "JOIN Companies c ON j.company_id = c.company_id ";
        
        if (filterStatus != null && !filterStatus.isEmpty()) {
            sql += "WHERE a.status = ?";
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            if (filterStatus != null && !filterStatus.isEmpty()) {
                pstmt.setString(1, filterStatus);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> app = new HashMap<>();
                app.put("id", rs.getInt("application_id"));
                app.put("student", rs.getString("student_name"));
                app.put("company", rs.getString("company_name"));
                app.put("role", rs.getString("role"));
                app.put("status", rs.getString("status"));
                apps.add(app);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return apps;
    }

    public boolean updateApplicationStatus(int appId, String status) {
        String sql = "UPDATE Applications SET status = ? WHERE application_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, appId);
            
            boolean success = pstmt.executeUpdate() > 0;
            if (success) {
                // Send notification to student
                sendAppStatusNotification(appId, status);
            }
            return success;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendAppStatusNotification(int appId, String status) {
        String query = "SELECT s.user_id, c.name as company, j.role FROM Applications a " +
                      "JOIN Students s ON a.student_id = s.student_id " +
                      "JOIN Jobs j ON a.job_id = j.job_id " +
                      "JOIN Companies c ON j.company_id = c.company_id " +
                      "WHERE a.application_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, appId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("user_id");
                String company = rs.getString("company");
                String role = rs.getString("role");
                String message = "Your application for " + role + " at " + company + " has been marked as " + status + ".";
                new NotificationDAO().sendNotification(userId, message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean scheduleInterview(int appId, String date, String time) {
        String sql = "INSERT INTO Interviews (application_id, interview_date, interview_time) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, appId);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
