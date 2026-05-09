import backend.utils.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
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
            
        } catch (SQLException e) {
            System.err.println("❌ Error updating schema: " + e.getMessage());
        }
    }
}
