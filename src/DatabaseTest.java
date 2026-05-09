import backend.utils.DBConnection;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        System.out.println("--- PlaceSync Database Verification ---");
        
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ SUCCESS: JDBC Connection established!");
                System.out.println("Database Product Name: " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Database Product Version: " + conn.getMetaData().getDatabaseProductVersion());
            } else {
                System.out.println("❌ FAILURE: Connection is null or closed.");
            }
        } catch (SQLException e) {
            System.out.println("❌ ERROR: Database connection failed!");
            System.err.println("SQL Error: " + e.getMessage());
        }
        
        System.out.println("---------------------------------------");
    }
}
