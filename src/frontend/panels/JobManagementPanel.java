package frontend.panels;

import frontend.components.RoundedButton;
import frontend.components.StyledTextField;
import frontend.theme.Theme;
import backend.queries.AdminDAO;
import backend.models.Company;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class JobManagementPanel extends BaseDashboardPanel {
    private AdminDAO adminDAO;
    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JComboBox<CompanyWrapper> companyDropdown;

    public JobManagementPanel() {
        super("Job Openings & Postings", "Admin");
        adminDAO = new AdminDAO();
        
        contentArea.setLayout(new BorderLayout(20, 20));

        // --- Left Side: Job List ---
        JPanel listPanel = new JPanel(new BorderLayout(0, 10));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel listTitle = new JLabel("Current Job Postings");
        listTitle.setFont(Theme.FONT_BOLD);
        listPanel.add(listTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Company", "Role", "Package (LPA)", "Min. CGPA"};
        tableModel = new DefaultTableModel(columns, 0);
        jobTable = new JTable(tableModel);
        jobTable.setRowHeight(35);
        jobTable.setFont(Theme.FONT_REGULAR);
        listPanel.add(new JScrollPane(jobTable), BorderLayout.CENTER);

        // --- Right Side: Add Job Form ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(350, 0));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 15, 0);
        gbc.weightx = 1.0;

        JLabel formTitle = new JLabel("Post New Job Opening");
        formTitle.setFont(Theme.FONT_BOLD);
        gbc.gridy = 0;
        formPanel.add(formTitle, gbc);

        // Company Selection
        JLabel compLabel = new JLabel("Select Company");
        compLabel.setFont(Theme.FONT_SMALL);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 5, 0);
        formPanel.add(compLabel, gbc);

        companyDropdown = new JComboBox<>();
        companyDropdown.setPreferredSize(new Dimension(0, 40));
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        formPanel.add(companyDropdown, gbc);

        StyledTextField roleField = new StyledTextField("Job Role (e.g. SDE-1)");
        gbc.gridy = 3;
        formPanel.add(roleField, gbc);

        StyledTextField pkgField = new StyledTextField("Package in LPA (e.g. 12.5)");
        gbc.gridy = 4;
        formPanel.add(pkgField, gbc);

        StyledTextField cgpaField = new StyledTextField("Min CGPA (e.g. 8.0)");
        gbc.gridy = 5;
        formPanel.add(cgpaField, gbc);

        RoundedButton postBtn = new RoundedButton("Post Job Opening");
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(postBtn, gbc);

        postBtn.addActionListener(e -> {
            CompanyWrapper selected = (CompanyWrapper) companyDropdown.getSelectedItem();
            if (selected == null) {
                JOptionPane.showMessageDialog(this, "Please select a company!");
                return;
            }
            
            try {
                boolean success = adminDAO.addJob(
                    selected.id,
                    roleField.getText(),
                    Double.parseDouble(pkgField.getText()),
                    Double.parseDouble(cgpaField.getText())
                );
                if (success) {
                    JOptionPane.showMessageDialog(this, "Job posted successfully!");
                    refreshTable();
                    roleField.setText("");
                    pkgField.setText("");
                    cgpaField.setText("");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid package or CGPA format!");
            }
        });

        contentArea.add(listPanel, BorderLayout.CENTER);
        contentArea.add(formPanel, BorderLayout.EAST);

        refreshCompanies();
        refreshTable();
    }

    private void refreshCompanies() {
        companyDropdown.removeAllItems();
        List<Company> companies = adminDAO.getAllCompanies();
        for (Company c : companies) {
            companyDropdown.addItem(new CompanyWrapper(c.getId(), c.getName()));
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Map<String, Object>> jobs = adminDAO.getAllJobs();
        for (Map<String, Object> j : jobs) {
            tableModel.addRow(new Object[]{
                j.get("job_id"),
                j.get("company_name"),
                j.get("role"),
                j.get("package"),
                j.get("cgpa")
            });
        }
    }

    // Helper class for the dropdown
    private class CompanyWrapper {
        int id;
        String name;
        CompanyWrapper(int id, String name) { this.id = id; this.name = name; }
        @Override public String toString() { return name; }
    }
}
