package frontend.panels;

import backend.auth.AuthManager;
import backend.models.JobApplication;
import backend.models.Student;
import backend.models.User;
import backend.queries.StudentDAO;
import frontend.components.Card;
import frontend.theme.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends BaseDashboardPanel {
    private final StudentDAO studentDAO = new StudentDAO();
    private DefaultTableModel tableModel;
    private Card cgpaCard, appCard, interviewCard, statusCard;

    public StudentDashboard() {
        super("My Career Overview", "Student");
        setupUI();
        refreshData();
    }

    private void setupUI() {
        contentArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;

        // Metric Cards (Row 1)
        cgpaCard = new Card("Your CGPA", "0.0", Theme.PRIMARY_TEAL);
        gbc.gridx = 0; gbc.gridy = 0;
        contentArea.add(cgpaCard, gbc);
        
        appCard = new Card("Applications", "0", Theme.TURQUOISE);
        gbc.gridx = 1;
        contentArea.add(appCard, gbc);
        
        interviewCard = new Card("Interviews", "0", Theme.ACCENT_EMERALD);
        gbc.gridx = 2;
        contentArea.add(interviewCard, gbc);
        
        // Row 2
        statusCard = new Card("Eligibility Status", "Checking...", Theme.AMBER);
        gbc.gridx = 0; gbc.gridy = 1;
        contentArea.add(statusCard, gbc);
        
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
        
        String[] columns = {"Company", "Role", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setFont(Theme.FONT_REGULAR);
        appSection.add(new JScrollPane(table), BorderLayout.CENTER);
        
        contentArea.add(appSection, gbc);
    }

    public final void refreshData() {
        User currentUser = AuthManager.getInstance().getCurrentUser();
        if (currentUser == null) return;

        Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
        if (student == null) return;

        // Update Cards
        cgpaCard.setValue(String.valueOf(student.getCgpa()));
        statusCard.setValue(student.getPlacementStatus());

        List<JobApplication> apps = studentDAO.getApplicationsByStudentId(student.getStudentId());
        appCard.setValue(String.valueOf(apps.size()));
        
        // Update Table
        tableModel.setRowCount(0);
        for (JobApplication app : apps) {
            tableModel.addRow(new Object[]{
                app.getCompanyName(),
                app.getJobRole(),
                app.getStatus()
            });
        }
    }
}


