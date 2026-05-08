package frontend.panels;

import frontend.components.Card;
import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class AdminDashboard extends BaseDashboardPanel {
    private backend.queries.AdminDAO adminDAO;

    public AdminDashboard() {
        super("Placement Analytics");
        adminDAO = new backend.queries.AdminDAO();
        Map<String, Integer> stats = adminDAO.getDashboardStats();
        
        int totalStudents = stats.getOrDefault("total_students", 0);
        int totalCompanies = stats.getOrDefault("total_companies", 0);
        int placedCount = stats.getOrDefault("placed_students", 0);
        double ratio = totalStudents > 0 ? (placedCount * 100.0 / totalStudents) : 0;

        // Use a more structured layout for cards
        contentArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        // Metric Cards (Row 1)
        gbc.gridx = 0; gbc.gridy = 0;
        contentArea.add(new Card("Total Students", String.valueOf(totalStudents), Theme.PRIMARY_TEAL), gbc);
        
        gbc.gridx = 1;
        contentArea.add(new Card("Placed Ratio", String.format("%.1f%%", ratio), Theme.ACCENT_EMERALD), gbc);
        
        gbc.gridx = 2;
        contentArea.add(new Card("Active Companies", String.valueOf(totalCompanies), Theme.TURQUOISE), gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentArea.add(new Card("Pending Apps", "128", Theme.AMBER), gbc);
        
        gbc.gridx = 1;
        contentArea.add(new Card("Upcoming Interviews", "32", Theme.PRIMARY_TEAL), gbc);
        
        gbc.gridx = 2;
        contentArea.add(new Card("Avg. Package", "₹12.4 LPA", Theme.ACCENT_EMERALD), gbc);
        
        // Add a "Recent Activities" Section
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        
        JPanel recentActivity = new JPanel(new BorderLayout());
        recentActivity.setBackground(Color.WHITE);
        recentActivity.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel sectionTitle = new JLabel("Recent Placement Activities");
        sectionTitle.setFont(Theme.FONT_BOLD);
        recentActivity.add(sectionTitle, BorderLayout.NORTH);
        
        // Mock Table/List
        String[] columns = {"Student", "Company", "Role", "Status"};
        Object[][] data = {
            {"Rahul Sharma", "Google", "SDE-1", "Selected"},
            {"Priya Patel", "Microsoft", "Data Analyst", "Interviewing"},
            {"Amit Kumar", "Amazon", "SDE Intern", "Applied"},
            {"Sneha Reddy", "Adobe", "UX Designer", "Rejected"}
        };
        JTable table = new JTable(data, columns);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setFont(Theme.FONT_REGULAR);
        recentActivity.add(new JScrollPane(table), BorderLayout.CENTER);
        
        contentArea.add(recentActivity, gbc);
    }
}


