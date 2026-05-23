package frontend.panels;

import frontend.components.RoundedButton;
import frontend.components.StyledTextField;
import frontend.theme.Theme;
import backend.queries.AdminDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ApplicationManagementPanel extends BaseDashboardPanel {
    private final AdminDAO adminDAO;
    private final JTable appTable;
    private final DefaultTableModel tableModel;

    public ApplicationManagementPanel() {
        super("Application & Interview Control", "Admin");
        adminDAO = new AdminDAO();
        
        contentArea.setLayout(new BorderLayout(20, 20));

        // --- Application List ---
        JPanel listPanel = new JPanel(new BorderLayout(0, 10));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel listTitle = new JLabel("Student Applications");
        listTitle.setFont(Theme.FONT_BOLD);
        headerPanel.add(listTitle, BorderLayout.WEST);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controls.setOpaque(false);

        String[] statuses = {"All Status", "Applied", "Under Review", "Shortlisted", "Rejected", "Selected for Interview", "Interview Scheduled", "Placed"};
        JComboBox<String> statusFilter = new JComboBox<>(statuses);
        statusFilter.addActionListener(e -> {
            String selected = (String) statusFilter.getSelectedItem();
            refreshTable(selected.equals("All Status") ? "" : selected);
        });
        controls.add(new JLabel("Filter:"));
        controls.add(statusFilter);

        JButton exportBtn = new JButton("Export CSV");
        exportBtn.setFont(Theme.FONT_SMALL);
        exportBtn.addActionListener(e -> handleExport());
        controls.add(exportBtn);

        headerPanel.add(controls, BorderLayout.EAST);
        listPanel.add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"App ID", "Student ID", "Student Name", "Company", "Role", "Applied Date/Time", "Status", "Interview Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appTable = new JTable(tableModel);
        appTable.setRowHeight(40);
        appTable.setFont(Theme.FONT_REGULAR);
        
        // Status Badge Renderer
        appTable.getColumnModel().getColumn(6).setCellRenderer(new frontend.components.StatusCellRenderer());
        
        listPanel.add(new JScrollPane(appTable), BorderLayout.CENTER);

        // --- Action Sidebar ---
        JPanel actionPanel = new JPanel(new GridBagLayout());
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setPreferredSize(new Dimension(300, 0));
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        JLabel actionTitle = new JLabel("Manage Selection");
        actionTitle.setFont(Theme.FONT_BOLD);
        gbc.gridy = 0;
        actionPanel.add(actionTitle, gbc);

        String[] allStatuses = {"Applied", "Under Review", "Shortlisted", "Rejected", "Selected for Interview", "Interview Scheduled", "Placed"};
        JComboBox<String> statusDropdown = new JComboBox<>(allStatuses);
        gbc.gridy = 1;
        actionPanel.add(statusDropdown, gbc);

        RoundedButton updateStatusBtn = new RoundedButton("Update Status");
        updateStatusBtn.setBackground(Theme.PRIMARY_TEAL);
        gbc.gridy = 2;
        actionPanel.add(updateStatusBtn, gbc);

        RoundedButton interviewBtn = new RoundedButton("Schedule Interview");
        gbc.gridy = 3;
        actionPanel.add(interviewBtn, gbc);

        // Separator/Spacer
        JSeparator sep = new JSeparator();
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 0, 15, 0);
        actionPanel.add(sep, gbc);
        gbc.insets = new Insets(8, 0, 8, 0);

        JLabel placementTitle = new JLabel("Manual Placement");
        placementTitle.setFont(Theme.FONT_BOLD);
        gbc.gridy = 5;
        actionPanel.add(placementTitle, gbc);

        RoundedButton manualPlaceBtn = new RoundedButton("Assign Placement");
        manualPlaceBtn.setBackground(Theme.ACCENT_EMERALD);
        gbc.gridy = 6;
        actionPanel.add(manualPlaceBtn, gbc);

        // --- Listeners ---
        updateStatusBtn.addActionListener(e -> {
            String selectedStatus = (String) statusDropdown.getSelectedItem();
            updateStatus(selectedStatus);
        });

        interviewBtn.addActionListener(e -> showInterviewDialog());
        manualPlaceBtn.addActionListener(e -> showManualPlacementDialog());

        contentArea.add(listPanel, BorderLayout.CENTER);
        contentArea.add(actionPanel, BorderLayout.EAST);

        refreshTable();
    }

    private void updateStatus(String status) {
        int row = appTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application first!");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        if (adminDAO.updateApplicationStatus(id, status)) {
            JOptionPane.showMessageDialog(this, "Status updated to " + status);
            refreshTable();
        }
    }

    private void showInterviewDialog() {
        int row = appTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application first!");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Schedule Interview", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(380, 360);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        StyledTextField dateField = new StyledTextField("Date (YYYY-MM-DD)");
        gbc.gridy = 0;
        dialog.add(dateField, gbc);

        StyledTextField timeField = new StyledTextField("Time (HH:MM)");
        gbc.gridy = 1;
        dialog.add(timeField, gbc);

        JComboBox<String> modeDropdown = new JComboBox<>(new String[]{"Online", "Offline"});
        gbc.gridy = 2;
        dialog.add(modeDropdown, gbc);

        StyledTextField locationField = new StyledTextField("Location or Meeting Link");
        gbc.gridy = 3;
        dialog.add(locationField, gbc);

        RoundedButton confirmBtn = new RoundedButton("Schedule Now");
        gbc.gridy = 4;
        dialog.add(confirmBtn, gbc);

        confirmBtn.addActionListener(e -> {
            String date = dateField.getText();
            String time = timeField.getText();
            String mode = (String) modeDropdown.getSelectedItem();
            String loc = locationField.getText();
            if (adminDAO.scheduleInterview(id, date, time, mode, loc)) {
                JOptionPane.showMessageDialog(dialog, "Interview Scheduled!");
                dialog.dispose();
                refreshTable();
            }
        });

        dialog.setVisible(true);
    }

    private void showManualPlacementDialog() {
        int row = appTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application first!");
            return;
        }
        int studentId = (int) tableModel.getValueAt(row, 1);
        String studentName = (String) tableModel.getValueAt(row, 2);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Assign Placement", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(380, 360);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JLabel titleLabel = new JLabel("Manual Placement for " + studentName);
        titleLabel.setFont(Theme.FONT_BOLD);
        gbc.gridy = 0;
        dialog.add(titleLabel, gbc);

        StyledTextField companyField = new StyledTextField("Company Name");
        gbc.gridy = 1;
        dialog.add(companyField, gbc);

        StyledTextField roleField = new StyledTextField("Job Role");
        gbc.gridy = 2;
        dialog.add(roleField, gbc);

        StyledTextField pkgField = new StyledTextField("Package (LPA)");
        gbc.gridy = 3;
        dialog.add(pkgField, gbc);

        RoundedButton saveBtn = new RoundedButton("Save Placement");
        gbc.gridy = 4;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            String company = companyField.getText();
            String role = roleField.getText();
            double pkg = 0.0;
            try {
                pkg = Double.parseDouble(pkgField.getText());
            } catch (NumberFormatException ex) {
                // Keep it 0.0 if not parsed
            }
            
            if (company.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Company Name and Job Role are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (adminDAO.assignPlacement(studentId, company, role, pkg)) {
                JOptionPane.showMessageDialog(dialog, "Placement Assigned successfully!");
                dialog.dispose();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to assign placement.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void handleExport() {
        try (java.io.FileWriter writer = new java.io.FileWriter("applications_report.csv")) {
            writer.write("App ID,Student ID,Student Name,Company,Role,Applied Date/Time,Status,Interview Status\n");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(
                    tableModel.getValueAt(i, 0) + "," +
                    tableModel.getValueAt(i, 1) + "," +
                    tableModel.getValueAt(i, 2) + "," +
                    tableModel.getValueAt(i, 3) + "," +
                    tableModel.getValueAt(i, 4) + "," +
                    tableModel.getValueAt(i, 5) + "," +
                    tableModel.getValueAt(i, 6) + "," +
                    tableModel.getValueAt(i, 7) + "\n"
                );
            }
            JOptionPane.showMessageDialog(this, "Report exported to applications_report.csv");
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
        }
    }

    public void refreshData() {
        refreshTable();
    }

    private void refreshTable() {
        refreshTable("");
    }

    private void refreshTable(String statusFilter) {
        tableModel.setRowCount(0);
        List<Map<String, Object>> apps = adminDAO.getAllApplications(statusFilter);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Map<String, Object> app : apps) {
            Object createdAtObj = app.get("created_at");
            String dateStr = "";
            if (createdAtObj instanceof java.util.Date) {
                dateStr = sdf.format((java.util.Date) createdAtObj);
            } else if (createdAtObj != null) {
                dateStr = createdAtObj.toString();
            }
            tableModel.addRow(new Object[]{
                app.get("id"),
                app.get("student_id"),
                app.get("student"),
                app.get("company"),
                app.get("role"),
                dateStr,
                app.get("status"),
                app.get("interview_status")
            });
        }
    }
}
