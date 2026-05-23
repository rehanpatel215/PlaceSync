import backend.utils.DBConnection;
import backend.utils.SecurityUtils;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SeedStudents {

    static class StudentData {
        String name;
        String username;
        String password;
        String branch;
        double cgpa;
        String resumePath;
        String status;

        StudentData(String name, String username, String password, String branch, double cgpa, String resumePath, String status) {
            this.name = name;
            this.username = username;
            this.password = password;
            this.branch = branch;
            this.cgpa = cgpa;
            this.resumePath = resumePath;
            this.status = status;
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Seeding 10 Student Records ---");

        List<StudentData> list = new ArrayList<>();
        list.add(new StudentData("Aarav Sharma", "aarav.sharma", "aarav@101", "CSE", 9.20, "/resumes/aarav_sharma.pdf", "Unplaced"));
        list.add(new StudentData("Vivaan Patel", "vivaan.patel", "vivaan@102", "ISE", 8.85, "/resumes/vivaan_patel.pdf", "Unplaced"));
        list.add(new StudentData("Aditya Verma", "aditya.verma", "aditya@103", "AIML", 9.10, "/resumes/aditya_verma.pdf", "Unplaced"));
        list.add(new StudentData("Vihaan Iyer", "vihaan.iyer", "vihaan@104", "ECE", 8.50, "/resumes/vihaan_iyer.pdf", "Unplaced"));
        list.add(new StudentData("Arjun Nair", "arjun.nair", "arjun@105", "CSE", 7.90, "/resumes/arjun_nair.pdf", "Unplaced"));
        list.add(new StudentData("Sai Srinivas", "sai.srinivas", "sai@106", "ISE", 8.25, "/resumes/sai_srinivas.pdf", "Unplaced"));
        list.add(new StudentData("Reyansh Reddy", "reyansh.reddy", "reyansh@107", "AIML", 9.45, "/resumes/reyansh_reddy.pdf", "Unplaced"));
        list.add(new StudentData("Ananya Hegde", "ananya.hegde", "ananya@108", "ECE", 8.70, "/resumes/ananya_hegde.pdf", "Unplaced"));
        list.add(new StudentData("Diya Kulkarni", "diya.kulkarni", "diya@109", "CSE", 9.05, "/resumes/diya_kulkarni.pdf", "Unplaced"));
        list.add(new StudentData("Ishaan Joshi", "ishaan.joshi", "ishaan@110", "ISE", 7.80, "/resumes/ishaan_joshi.pdf", "Unplaced"));

        StringBuilder markdownTable = new StringBuilder();
        markdownTable.append("| Student Name | Username (Login ID) | User ID | Student ID | Placement Status | Password  |\n");
        markdownTable.append("| ------------ | ------------------- | ------- | ---------- | ---------------- | --------- |\n");

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.err.println("❌ Could not connect to database!");
                return;
            }

            // Reset existing placements, interviews and applications to start clean
            try (Statement resetStmt = conn.createStatement()) {
                resetStmt.execute("DELETE FROM Placement_Details");
                resetStmt.execute("DELETE FROM Interviews");
                resetStmt.execute("DELETE FROM Applications");
                resetStmt.execute("UPDATE Students SET placement_status = 'Unplaced'");
                System.out.println("✅ Database placement/interview state reset successfully!");
            } catch (Exception e) {
                System.err.println("Note on state reset: " + e.getMessage());
            }

            for (StudentData sd : list) {
                // 1. Insert User
                String checkUser = "SELECT user_id FROM Users WHERE username = ?";
                int userId = -1;
                try (PreparedStatement checkPstmt = conn.prepareStatement(checkUser)) {
                    checkPstmt.setString(1, sd.username);
                    try (ResultSet rs = checkPstmt.executeQuery()) {
                        if (rs.next()) {
                            userId = rs.getInt("user_id");
                        }
                    }
                }

                if (userId == -1) {
                    String insertUser = "INSERT INTO Users (username, password, role) VALUES (?, ?, 'Student')";
                    try (PreparedStatement userPstmt = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                        userPstmt.setString(1, sd.username);
                        userPstmt.setString(2, SecurityUtils.hashPassword(sd.password));
                        userPstmt.executeUpdate();
                        try (ResultSet keys = userPstmt.getGeneratedKeys()) {
                            if (keys.next()) {
                                userId = keys.getInt(1);
                            }
                        }
                    }
                }

                if (userId == -1) {
                    System.err.println("❌ Failed to create/retrieve user for " + sd.name);
                    continue;
                }

                // 2. Insert/Update Student
                String checkStudent = "SELECT student_id FROM Students WHERE user_id = ?";
                int studentId = -1;
                try (PreparedStatement checkPstmt = conn.prepareStatement(checkStudent)) {
                    checkPstmt.setInt(1, userId);
                    try (ResultSet rs = checkPstmt.executeQuery()) {
                        if (rs.next()) {
                            studentId = rs.getInt("student_id");
                        }
                    }
                }

                if (studentId == -1) {
                    String insertStudent = "INSERT INTO Students (user_id, name, branch, cgpa, resume_path, placement_status) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement studentPstmt = conn.prepareStatement(insertStudent, Statement.RETURN_GENERATED_KEYS)) {
                        studentPstmt.setInt(1, userId);
                        studentPstmt.setString(2, sd.name);
                        studentPstmt.setString(3, sd.branch);
                        studentPstmt.setDouble(4, sd.cgpa);
                        studentPstmt.setString(5, sd.resumePath);
                        studentPstmt.setString(6, sd.status);
                        studentPstmt.executeUpdate();
                        try (ResultSet keys = studentPstmt.getGeneratedKeys()) {
                            if (keys.next()) {
                                studentId = keys.getInt(1);
                            }
                        }
                    }
                } else {
                    String updateStudent = "UPDATE Students SET name = ?, branch = ?, cgpa = ?, resume_path = ?, placement_status = ? WHERE student_id = ?";
                    try (PreparedStatement studentPstmt = conn.prepareStatement(updateStudent)) {
                        studentPstmt.setString(1, sd.name);
                        studentPstmt.setString(2, sd.branch);
                        studentPstmt.setDouble(3, sd.cgpa);
                        studentPstmt.setString(4, sd.resumePath);
                        studentPstmt.setString(5, sd.status);
                        studentPstmt.setInt(6, studentId);
                        studentPstmt.executeUpdate();
                    }
                }

                System.out.printf("✅ Seeded: %s (User ID: %d, Student ID: %d, Branch: %s, CGPA: %.2f, Status: %s)\n",
                        sd.name, userId, studentId, sd.branch, sd.cgpa, sd.status);

                markdownTable.append(String.format("| %-12s | %-19s | %-7d | %-10d | %-16s | %-9s |\n",
                        sd.name, sd.username, userId, studentId, sd.status, sd.password));
            }

            // Write markdown file
            try (FileWriter writer = new FileWriter("students.md")) {
                writer.write(markdownTable.toString());
                System.out.println("✅ Generated students.md file successfully!");
            }

        } catch (Exception e) {
            System.err.println("❌ Database error during seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
