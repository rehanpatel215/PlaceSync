import backend.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseSeeder {
    public static void main(String[] args) {
        System.out.println("--- PlaceSync Database Seeder ---");
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("❌ Could not connect to database!");
                return;
            }

            // 1. Check if admin exists
            String checkSql = "SELECT COUNT(*) FROM Users WHERE username = 'admin'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(checkSql);
            rs.next();
            
            // 2. Insert Admin User
            // 2. Insert/Update Admin User
            String adminHash = backend.utils.SecurityUtils.hashPassword("admin123");
            if (rs.getInt(1) == 0) {
                String insertAdminSql = "INSERT INTO Users (username, password, role) VALUES ('admin', ?, 'Admin')";
                PreparedStatement pstmt = conn.prepareStatement(insertAdminSql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, adminHash);
                pstmt.executeUpdate();
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    PreparedStatement pstmtAdmin = conn.prepareStatement("INSERT INTO Admin (user_id) VALUES (?)");
                    pstmtAdmin.setInt(1, keys.getInt(1));
                    pstmtAdmin.executeUpdate();
                }
                System.out.println("✅ Admin created: admin / admin123 (hashed)");
            } else {
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET password = ? WHERE username = 'admin'");
                pstmt.setString(1, adminHash);
                pstmt.executeUpdate();
                System.out.println("✅ Admin password updated to hash.");
            }

            // 3. Insert/Update Student User
            rs = stmt.executeQuery("SELECT COUNT(*) FROM Users WHERE username = 'student'");
            rs.next();
            String studentHash = backend.utils.SecurityUtils.hashPassword("student123");
            if (rs.getInt(1) == 0) {
                String insertStudentSql = "INSERT INTO Users (username, password, role) VALUES ('student', ?, 'Student')";
                PreparedStatement pstmt = conn.prepareStatement(insertStudentSql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, studentHash);
                pstmt.executeUpdate();
                ResultSet keys = pstmt.getGeneratedKeys();
                if (keys.next()) {
                    int userId = keys.getInt(1);
                    PreparedStatement pstmtStud = conn.prepareStatement(
                        "INSERT INTO Students (user_id, name, branch, cgpa, placement_status) VALUES (?, ?, ?, ?, 'Unplaced')"
                    );
                    pstmtStud.setInt(1, userId);
                    pstmtStud.setString(2, "Test Student");
                    pstmtStud.setString(3, "Computer Science");
                    pstmtStud.setDouble(4, 8.5);
                    pstmtStud.executeUpdate();
                }
                System.out.println("✅ Student created: student / student123 (hashed)");
            } else {
                PreparedStatement pstmt = conn.prepareStatement("UPDATE Users SET password = ? WHERE username = 'student'");
                pstmt.setString(1, studentHash);
                pstmt.executeUpdate();
                System.out.println("✅ Student password updated to hash.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error seeding database:");
            System.err.println("  Details: " + e.getMessage());
        }
        System.out.println("---------------------------------");
    }
}
