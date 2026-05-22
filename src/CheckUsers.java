import backend.utils.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckUsers {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users");
            System.out.println("--- Current Users ---");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("user_id") + 
                                   ", Username: " + rs.getString("username") + 
                                   ", Role: " + rs.getString("role"));
            }
            System.out.println("---------------------");
        } catch (Exception e) {
            System.err.println("Error in CheckUsers: " + e.getMessage());
        }
    }
}
