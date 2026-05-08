package backend.queries;

import backend.models.Student;
import backend.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}


