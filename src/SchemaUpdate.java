import backend.utils.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class SchemaUpdate {
    public static void main(String[] args) {
        String sql = "CREATE TABLE IF NOT EXISTS Notifications (" +
                     "notification_id INT PRIMARY KEY AUTO_INCREMENT, " +
                     "user_id INT, " +
                     "message TEXT NOT NULL, " +
                     "is_read BOOLEAN DEFAULT FALSE, " +
                     "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (user_id) REFERENCES Users(user_id)" +
                     ")";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("✅ Notifications table created successfully!");
            
            // 2. Create Placement_Details Table
            String placementSql = "CREATE TABLE IF NOT EXISTS Placement_Details (" +
                                  "placement_id INT PRIMARY KEY AUTO_INCREMENT, " +
                                  "student_id INT UNIQUE, " +
                                  "company_name VARCHAR(100) NOT NULL, " +
                                  "role VARCHAR(100) NOT NULL, " +
                                  "package DECIMAL(10,2), " +
                                  "FOREIGN KEY (student_id) REFERENCES Students(student_id) ON DELETE CASCADE" +
                                  ")";
            stmt.execute(placementSql);
            System.out.println("✅ Placement_Details table verified/created!");

            // 3. Update status ENUM in Applications table
            try {
                stmt.execute("UPDATE Applications SET status = 'Applied' WHERE status = 'Pending'");
            } catch (SQLException e) {}
            try {
                stmt.execute("ALTER TABLE Applications MODIFY COLUMN status ENUM('Applied', 'Under Review', 'Shortlisted', 'Rejected', 'Selected for Interview', 'Interview Scheduled', 'Placed') DEFAULT 'Applied'");
                System.out.println("✅ Updated Applications status ENUM constraints!");
            } catch (SQLException e) {
                System.err.println("Note on Applications ENUM update: " + e.getMessage());
            }

            // Check if created_at column exists in Applications table
            boolean hasCreatedAt = false;
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM Applications LIMIT 1")) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                for (int i = 1; i <= colCount; i++) {
                    if ("created_at".equalsIgnoreCase(meta.getColumnName(i))) {
                        hasCreatedAt = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Applications table check skipped or table does not exist yet: " + e.getMessage());
            }

            if (!hasCreatedAt) {
                try {
                    stmt.execute("ALTER TABLE Applications ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
                    System.out.println("✅ Added created_at column to Applications table!");
                } catch (SQLException e) {
                    System.err.println("❌ Error adding created_at column: " + e.getMessage());
                }
            }

            // 4. Add company_id and unique key constraint to Applications table
            boolean hasCompanyId = false;
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM Applications LIMIT 1")) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                for (int i = 1; i <= colCount; i++) {
                    if ("company_id".equalsIgnoreCase(meta.getColumnName(i))) {
                        hasCompanyId = true;
                        break;
                    }
                }
            } catch (SQLException e) {}

            if (!hasCompanyId) {
                try {
                    stmt.execute("ALTER TABLE Applications ADD COLUMN company_id INT");
                    System.out.println("✅ Added company_id column to Applications table!");
                } catch (SQLException e) {
                    System.err.println("❌ Error adding company_id column: " + e.getMessage());
                }
            }

            try {
                stmt.execute("UPDATE Applications a SET a.company_id = (SELECT j.company_id FROM Jobs j WHERE j.job_id = a.job_id) WHERE a.company_id IS NULL");
                stmt.execute("DELETE FROM Applications WHERE application_id NOT IN (SELECT min_id FROM (SELECT MIN(application_id) as min_id FROM Applications GROUP BY student_id, company_id) as t)");
                stmt.execute("ALTER TABLE Applications ADD UNIQUE KEY unique_student_company (student_id, company_id)");
                System.out.println("✅ Cleaned duplicates and created UNIQUE constraint on Applications!");
            } catch (SQLException e) {
                System.out.println("Note on Applications UNIQUE index: " + e.getMessage());
            }

            // 5. Add interview_mode and interview_location to Interviews table
            boolean hasMode = false;
            boolean hasLoc = false;
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM Interviews LIMIT 1")) {
                ResultSetMetaData meta = rs.getMetaData();
                int colCount = meta.getColumnCount();
                for (int i = 1; i <= colCount; i++) {
                    if ("interview_mode".equalsIgnoreCase(meta.getColumnName(i))) hasMode = true;
                    if ("interview_location".equalsIgnoreCase(meta.getColumnName(i))) hasLoc = true;
                }
            } catch (SQLException e) {}

            if (!hasMode) {
                try {
                    stmt.execute("ALTER TABLE Interviews ADD COLUMN interview_mode VARCHAR(50) DEFAULT 'Online'");
                    System.out.println("✅ Added interview_mode column to Interviews table!");
                } catch (SQLException e) {
                    System.err.println("❌ Error adding interview_mode: " + e.getMessage());
                }
            }
            if (!hasLoc) {
                try {
                    stmt.execute("ALTER TABLE Interviews ADD COLUMN interview_location VARCHAR(255) DEFAULT 'Zoom Link'");
                    System.out.println("✅ Added interview_location column to Interviews table!");
                } catch (SQLException e) {
                    System.err.println("❌ Error adding interview_location: " + e.getMessage());
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating schema: " + e.getMessage());
        }
    }
}
