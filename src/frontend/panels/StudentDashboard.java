package frontend.panels;

import frontend.components.Card;
import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends BaseDashboardPanel {
    public StudentDashboard() {
        super("My Career Overview");
        
        // Use a structured grid layout for cards
        contentArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        // Metric Cards (Row 1)
        gbc.gridx = 0; gbc.gridy = 0;
        contentArea.add(new Card("Your CGPA", "8.9", Theme.PRIMARY_TEAL), gbc);
        
        gbc.gridx = 1;
        contentArea.add(new Card("Applications", "12", Theme.TURQUOISE), gbc);
        
        gbc.gridx = 2;
        contentArea.add(new Card("Interviews", "3", Theme.ACCENT_EMERALD), gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        contentArea.add(new Card("Eligibility Status", "Eligible", Theme.AMBER), gbc);
        
        // Add a "My Applications" Section
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 1.0;
        
        JPanel appSection = new JPanel(new BorderLayout());
        appSection.setBackground(Color.WHITE);
        appSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel sectionTitle = new JLabel("My Job Applications");
        sectionTitle.setFont(Theme.FONT_BOLD);
        appSection.add(sectionTitle, BorderLayout.NORTH);
        
        String[] columns = {"Company", "Role", "Applied Date", "Status"};
        Object[][] data = {
            {"Google", "SDE-1", "2024-05-01", "Applied"},
            {"Microsoft", "Data Analyst", "2024-05-05", "Interview Scheduled"},
            {"Amazon", "SDE Intern", "2024-04-28", "Rejected"}
        };
        JTable table = new JTable(data, columns);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setFont(Theme.FONT_REGULAR);
        appSection.add(new JScrollPane(table), BorderLayout.CENTER);
        
        contentArea.add(appSection, gbc);
    }
}


