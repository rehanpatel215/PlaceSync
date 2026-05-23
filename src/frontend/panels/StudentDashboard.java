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
import java.util.Map;

public class StudentDashboard extends BaseDashboardPanel {
    private final StudentDAO studentDAO = new StudentDAO();
    private DefaultTableModel tableModel;
    private Card cgpaCard, appCard, interviewCard, statusCard;
    
    // Placement details UI components
    private JPanel placementPanel;
    private JLabel placementTitle, compLabel, roleLabel, pkgLabel;
    
    // Interview details UI components
    private JPanel interviewDetailsPanel;
    private JLabel interviewTitle, intCompLabel, intRoleLabel, intDateTimeLabel, intModeLabel, intLocationLabel;

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

        // Metric Cards (Row 0 - 4 cards side-by-side)
        cgpaCard = new Card("Your CGPA", "0.0", Theme.PRIMARY_TEAL);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        contentArea.add(cgpaCard, gbc);
        
        appCard = new Card("Applications", "0", Theme.TURQUOISE);
        gbc.gridx = 1;
        contentArea.add(appCard, gbc);
        
        interviewCard = new Card("Scheduled Interviews", "0", Theme.ACCENT_EMERALD);
        gbc.gridx = 2;
        contentArea.add(interviewCard, gbc);
        
        statusCard = new Card("Placement Status", "Checking...", Theme.AMBER);
        gbc.gridx = 3;
        contentArea.add(statusCard, gbc);
        
        // Row 1: Split Details Panel (Placement Details vs Upcoming Interview)
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        
        // Left: Placement Details
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        setupPlacementPanel();
        contentArea.add(placementPanel, gbc);
        
        // Right: Interview Details
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        setupInterviewDetailsPanel();
        contentArea.add(interviewDetailsPanel, gbc);
        
        // Row 2: "My Applications" Section
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 4;
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
        
        // Status Badge Renderer
        table.getColumnModel().getColumn(2).setCellRenderer(new frontend.components.StatusCellRenderer());
        
        appSection.add(new JScrollPane(table), BorderLayout.CENTER);
        
        contentArea.add(appSection, gbc);
    }

    private void setupPlacementPanel() {
        placementPanel = new JPanel(new GridBagLayout());
        placementPanel.setBackground(Color.WHITE);
        placementPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridx = 0;
        g.insets = new Insets(4, 0, 4, 0);
        
        placementTitle = new JLabel("🏆 Placement Information");
        placementTitle.setFont(Theme.FONT_BOLD);
        placementTitle.setForeground(Theme.PRIMARY_TEAL);
        g.gridy = 0;
        placementPanel.add(placementTitle, g);
        
        compLabel = new JLabel("Company: N/A");
        compLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 1;
        placementPanel.add(compLabel, g);
        
        roleLabel = new JLabel("Role: N/A");
        roleLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 2;
        placementPanel.add(roleLabel, g);
        
        pkgLabel = new JLabel("Package: N/A");
        pkgLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 3;
        placementPanel.add(pkgLabel, g);
    }

    private void setupInterviewDetailsPanel() {
        interviewDetailsPanel = new JPanel(new GridBagLayout());
        interviewDetailsPanel.setBackground(Color.WHITE);
        interviewDetailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.gridx = 0;
        g.insets = new Insets(4, 0, 4, 0);
        
        interviewTitle = new JLabel("📅 Next Scheduled Interview");
        interviewTitle.setFont(Theme.FONT_BOLD);
        interviewTitle.setForeground(Theme.TURQUOISE);
        g.gridy = 0;
        interviewDetailsPanel.add(interviewTitle, g);
        
        intCompLabel = new JLabel("Company: N/A");
        intCompLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 1;
        interviewDetailsPanel.add(intCompLabel, g);
        
        intRoleLabel = new JLabel("Role: N/A");
        intRoleLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 2;
        interviewDetailsPanel.add(intRoleLabel, g);
        
        intDateTimeLabel = new JLabel("Date & Time: N/A");
        intDateTimeLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 3;
        interviewDetailsPanel.add(intDateTimeLabel, g);
        
        intModeLabel = new JLabel("Mode: N/A");
        intModeLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 4;
        interviewDetailsPanel.add(intModeLabel, g);
        
        intLocationLabel = new JLabel("Location/Link: N/A");
        intLocationLabel.setFont(Theme.FONT_REGULAR);
        g.gridy = 5;
        interviewDetailsPanel.add(intLocationLabel, g);
    }

    public final void refreshData() {
        User currentUser = AuthManager.getInstance().getCurrentUser();
        if (currentUser == null) return;

        Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
        if (student == null) return;

        // Update Cards
        cgpaCard.setValue(String.valueOf(student.getCgpa()));
        cgpaCard.setTrend("Current Academic CGPA", true);
        
        statusCard.setValue(student.getPlacementStatus());
        statusCard.setTrend("Updated in real-time", true);

        List<JobApplication> apps = studentDAO.getApplicationsByStudentId(student.getStudentId());
        appCard.setValue(String.valueOf(apps.size()));
        appCard.setTrend("Total job applications", true);
        
        List<Map<String, Object>> interviews = studentDAO.getStudentInterviews(student.getStudentId());
        interviewCard.setValue(String.valueOf(interviews.size()));
        interviewCard.setTrend("Upcoming interviews scheduled", true);

        // Update Placement details panel
        if ("Placed".equalsIgnoreCase(student.getPlacementStatus())) {
            Map<String, Object> placement = studentDAO.getPlacementDetails(student.getStudentId());
            if (placement != null) {
                compLabel.setText("Company: " + placement.get("company_name"));
                roleLabel.setText("Role: " + placement.get("role"));
                double pkg = (double) placement.get("package");
                pkgLabel.setText("Package: " + (pkg > 0 ? pkg + " LPA" : "TBD"));
            } else {
                compLabel.setText("Company: TBD");
                roleLabel.setText("Role: TBD");
                pkgLabel.setText("Package: TBD");
            }
        } else {
            compLabel.setText("No placements recorded yet.");
            roleLabel.setText("Keep applying!");
            pkgLabel.setText("");
        }

        // Update Interview details panel
        if (!interviews.isEmpty()) {
            Map<String, Object> nextInt = interviews.get(0); // Show next/first interview
            intCompLabel.setText("Company: " + nextInt.get("company"));
            intRoleLabel.setText("Role: " + nextInt.get("role"));
            intDateTimeLabel.setText("Date & Time: " + nextInt.get("interview_date") + " " + nextInt.get("interview_time"));
            intModeLabel.setText("Mode: " + nextInt.get("interview_mode"));
            intLocationLabel.setText("Location/Link: " + nextInt.get("interview_location"));
        } else {
            intCompLabel.setText("No scheduled interviews.");
            intRoleLabel.setText("");
            intDateTimeLabel.setText("");
            intModeLabel.setText("");
            intLocationLabel.setText("");
        }

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
