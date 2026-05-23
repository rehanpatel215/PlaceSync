package frontend.panels;

import frontend.components.Card;
import frontend.theme.Theme;
import java.awt.*;
import frontend.components.StatChart;
import java.util.Map;

public class AdminDashboard extends BaseDashboardPanel {
    private final backend.queries.AdminDAO adminDAO;

    public AdminDashboard() {
        super("Placement Analytics", "Admin");
        adminDAO = new backend.queries.AdminDAO();
        refreshData();
    }

    public void refreshData() {
        contentArea.removeAll();
        Map<String, Integer> stats = adminDAO.getDashboardStats();
        Map<String, Integer> appStats = adminDAO.getApplicationStatistics();
        Map<String, Integer> branchStats = adminDAO.getPlacementRatioByBranch();
        
        int totalStudents = stats.getOrDefault("total_students", 0);
        int totalCompanies = stats.getOrDefault("total_companies", 0);
        int placedCount = stats.getOrDefault("placed_students", 0);
        double ratio = totalStudents > 0 ? (placedCount * 100.0 / totalStudents) : 0;

        contentArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        // Metric Cards (Row 1)
        gbc.gridx = 0; gbc.gridy = 0;
        Card studentCard = new Card("Total Students", String.valueOf(totalStudents), Theme.PRIMARY_TEAL);
        studentCard.setTrend("↑ 8 this semester", true);
        contentArea.add(studentCard, gbc);
        
        gbc.gridx = 1;
        Card ratioCard = new Card("Placed Ratio", String.format("%.1f%%", ratio), Theme.ACCENT_EMERALD);
        ratioCard.setTrend("↑ 5% vs last year", true);
        contentArea.add(ratioCard, gbc);
        
        gbc.gridx = 2;
        Card companyCard = new Card("Active Companies", String.valueOf(totalCompanies), Theme.ACCENT_BLUE);
        companyCard.setTrend("3 added this month", true);
        contentArea.add(companyCard, gbc);
        
        // Row 2: Charts Section
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;
        
        StatChart statusChart = new StatChart("Application Status", appStats);
        contentArea.add(statusChart, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        StatChart branchChart = new StatChart("Placed Students by Branch", branchStats);
        contentArea.add(branchChart, gbc);

        contentArea.revalidate();
        contentArea.repaint();
    }
}
