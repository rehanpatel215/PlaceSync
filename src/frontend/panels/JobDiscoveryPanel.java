package frontend.panels;

import backend.auth.AuthManager;
import backend.models.Job;
import backend.models.Student;
import backend.models.User;
import backend.queries.StudentDAO;
import frontend.theme.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JobDiscoveryPanel extends BaseDashboardPanel {
    private final StudentDAO studentDAO = new StudentDAO();
    private DefaultTableModel tableModel;
    private JTable jobTable;
    private JButton applyButton;

    public JobDiscoveryPanel() {
        super("Available Opportunities", "Student");
        setupUI();
        refreshJobs("", "", 0);
    }

    private void setupUI() {
        contentArea.setLayout(new BorderLayout(20, 20));
        
        // Header info
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel subtitle = new JLabel("Browse and apply for jobs that match your criteria.");
        subtitle.setFont(Theme.FONT_REGULAR);
        header.add(subtitle, BorderLayout.WEST);
        
        contentArea.add(header, BorderLayout.NORTH);

        // Filter Bar
        frontend.components.FilterBar filterBar = new frontend.components.FilterBar(data -> {
            refreshJobs(data.company, data.role, data.minPackage);
        });
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(filterBar, BorderLayout.NORTH);

        // Job Table
        String[] columns = {"ID", "Company", "Role", "Package (LPA)", "Min. CGPA"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(40);
        jobTable.setFont(Theme.FONT_REGULAR);
        jobTable.getTableHeader().setFont(Theme.FONT_BOLD);
        
        // Hide ID column
        jobTable.getColumnModel().getColumn(0).setMinWidth(0);
        jobTable.getColumnModel().getColumn(0).setMaxWidth(0);
        
        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        contentArea.add(centerPanel, BorderLayout.CENTER);

        // Footer Actions
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setOpaque(false);
        
        applyButton = new frontend.components.RoundedButton("Apply Now");
        applyButton.setPreferredSize(new Dimension(160, 45));
        applyButton.addActionListener(e -> handleApply());
        
        footer.add(applyButton);
        contentArea.add(footer, BorderLayout.SOUTH);
    }

    public final void refreshJobs() {
        refreshJobs("", "", 0);
    }

    public final void refreshJobs(String company, String role, double minPkg) {
        tableModel.setRowCount(0);
        List<Job> jobs = studentDAO.searchJobs(company, role, minPkg);
        for (Job job : jobs) {
            tableModel.addRow(new Object[]{
                job.getId(),
                job.getCompanyName(),
                job.getRole(),
                job.getPackageLPA(),
                job.getEligibilityCGPA()
            });
        }
    }

    private void handleApply() {
        int selectedRow = jobTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a job to apply.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int jobId = (int) tableModel.getValueAt(selectedRow, 0);
        double minCgpa = (double) tableModel.getValueAt(selectedRow, 4);
        
        User currentUser = AuthManager.getInstance().getCurrentUser();
        Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
        
        if (student.getCgpa() < minCgpa) {
            JOptionPane.showMessageDialog(this, "You do not meet the minimum CGPA requirement for this job.", "Eligibility Check Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (studentDAO.applyForJob(student.getStudentId(), jobId)) {
            JOptionPane.showMessageDialog(this, "Application submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "You have already applied for this company.", "Application Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
