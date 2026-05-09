package backend.queries;

import backend.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class NotificationDAO {
    
    public boolean sendNotification(int userId, String message) {
        String sql = "INSERT INTO Notifications (user_id, message) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, message);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Map<String, Object>> getUnreadNotifications(int userId) {
        List<Map<String, Object>> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE user_id = ? AND is_read = FALSE ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> n = new HashMap<>();
                n.put("id", rs.getInt("notification_id"));
                n.put("message", rs.getString("message"));
                n.put("created_at", rs.getTimestamp("created_at"));
                notifications.add(n);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    public boolean markAsRead(int userId) {
        String sql = "UPDATE Notifications SET is_read = TRUE WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) FROM Notifications WHERE user_id = ? AND is_read = FALSE";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
