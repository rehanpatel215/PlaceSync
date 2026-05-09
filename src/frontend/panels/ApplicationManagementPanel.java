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
    private AdminDAO adminDAO;
    private JTable appTable;
    private DefaultTableModel tableModel;

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

        String[] statuses = {"All Status", "Pending", "Selected", "Rejected"};
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

        String[] columns = {"App ID", "Student Name", "Company", "Role", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        appTable = new JTable(tableModel);
        appTable.setRowHeight(40);
        appTable.setFont(Theme.FONT_REGULAR);
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
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;

        JLabel actionTitle = new JLabel("Manage Selection");
        actionTitle.setFont(Theme.FONT_BOLD);
        gbc.gridy = 0;
        actionPanel.add(actionTitle, gbc);

        RoundedButton interviewBtn = new RoundedButton("Schedule Interview");
        gbc.gridy = 1;
        actionPanel.add(interviewBtn, gbc);

        RoundedButton selectBtn = new RoundedButton("Mark as Selected");
        selectBtn.setBackground(Theme.ACCENT_EMERALD);
        gbc.gridy = 2;
        actionPanel.add(selectBtn, gbc);

        RoundedButton rejectBtn = new RoundedButton("Mark as Rejected");
        rejectBtn.setBackground(new Color(239, 68, 68));
        gbc.gridy = 3;
        actionPanel.add(rejectBtn, gbc);

        // --- Listeners ---
        interviewBtn.addActionListener(e -> showInterviewDialog());
        
        selectBtn.addActionListener(e -> updateStatus("Selected"));
        rejectBtn.addActionListener(e -> updateStatus("Rejected"));

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
        dialog.setSize(350, 300);
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

        RoundedButton confirmBtn = new RoundedButton("Schedule Now");
        gbc.gridy = 2;
        dialog.add(confirmBtn, gbc);

        confirmBtn.addActionListener(e -> {
            if (adminDAO.scheduleInterview(id, dateField.getText(), timeField.getText())) {
                JOptionPane.showMessageDialog(dialog, "Interview Scheduled!");
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void handleExport() {
        try (java.io.FileWriter writer = new java.io.FileWriter("applications_report.csv")) {
            writer.write("App ID,Student,Company,Role,Status\n");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                writer.write(
                    tableModel.getValueAt(i, 0) + "," +
                    tableModel.getValueAt(i, 1) + "," +
                    tableModel.getValueAt(i, 2) + "," +
                    tableModel.getValueAt(i, 3) + "," +
                    tableModel.getValueAt(i, 4) + "\n"
                );
            }
            JOptionPane.showMessageDialog(this, "Report exported to applications_report.csv");
        } catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(this, "Export failed: " + e.getMessage());
        }
    }

    private void refreshTable() {
        refreshTable("");
    }

    private void refreshTable(String statusFilter) {
        tableModel.setRowCount(0);
        List<Map<String, Object>> apps = adminDAO.getAllApplications(statusFilter);
        for (Map<String, Object> app : apps) {
            tableModel.addRow(new Object[]{
                app.get("id"),
                app.get("student"),
                app.get("company"),
                app.get("role"),
                app.get("status")
            });
        }
    }
}
