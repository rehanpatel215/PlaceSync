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
            if (rs.getInt(1) == 0) {
                String insertAdminSql = "INSERT INTO Users (username, password, role) VALUES ('admin', 'admin123', 'Admin')";
                stmt.executeUpdate(insertAdminSql, Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Admin (user_id) VALUES (?)");
                    pstmt.setInt(1, keys.getInt(1));
                    pstmt.executeUpdate();
                }
                System.out.println("✅ Admin created: admin / admin123");
            }

            // 3. Insert Student User
            rs = stmt.executeQuery("SELECT COUNT(*) FROM Users WHERE username = 'student'");
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertStudentSql = "INSERT INTO Users (username, password, role) VALUES ('student', 'student123', 'Student')";
                stmt.executeUpdate(insertStudentSql, Statement.RETURN_GENERATED_KEYS);
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    int userId = keys.getInt(1);
                    PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO Students (user_id, name, branch, cgpa, placement_status) VALUES (?, ?, ?, ?, 'Unplaced')"
                    );
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, "Test Student");
                    pstmt.setString(3, "Computer Science");
                    pstmt.setDouble(4, 8.5);
                    pstmt.executeUpdate();
                }
                System.out.println("✅ Student created: student / student123");
            }
        } catch (Exception e) {
            System.err.println("❌ Error seeding database:");
            e.printStackTrace();
        }
        System.out.println("---------------------------------");
    }
}
