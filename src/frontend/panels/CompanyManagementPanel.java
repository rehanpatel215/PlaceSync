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

public class CompanyManagementPanel extends BaseDashboardPanel {
    private AdminDAO adminDAO;
    private JTable companyTable;
    private DefaultTableModel tableModel;

    public CompanyManagementPanel() {
        super("Company Management");
        adminDAO = new AdminDAO();
        
        contentArea.setLayout(new BorderLayout(20, 20));

        // --- Left Side: Company List ---
        JPanel listPanel = new JPanel(new BorderLayout(0, 10));
        listPanel.setBackground(Color.WHITE);
        listPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel listTitle = new JLabel("Registered Companies");
        listTitle.setFont(Theme.FONT_BOLD);
        listPanel.add(listTitle, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Location"};
        tableModel = new DefaultTableModel(columns, 0);
        companyTable = new JTable(tableModel);
        companyTable.setRowHeight(35);
        companyTable.setFont(Theme.FONT_REGULAR);
        listPanel.add(new JScrollPane(companyTable), BorderLayout.CENTER);

        // --- Right Side: Add Company Form ---
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

        JLabel formTitle = new JLabel("Add New Company");
        formTitle.setFont(Theme.FONT_BOLD);
        gbc.gridy = 0;
        formPanel.add(formTitle, gbc);

        StyledTextField nameField = new StyledTextField("Company Name");
        gbc.gridy = 1;
        formPanel.add(nameField, gbc);

        StyledTextField locationField = new StyledTextField("Location (e.g. Bangalore, Remote)");
        gbc.gridy = 2;
        formPanel.add(locationField, gbc);

        RoundedButton addBtn = new RoundedButton("Register Company");
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        formPanel.add(addBtn, gbc);

        addBtn.addActionListener(e -> {
            boolean success = adminDAO.addCompany(
                nameField.getText(), 
                locationField.getText()
            );
            if (success) {
                JOptionPane.showMessageDialog(this, "Company added successfully!");
                refreshTable();
                nameField.setText("");
                locationField.setText("");
            }
        });

        // Add Delete Button below the form
        RoundedButton deleteBtn = new RoundedButton("Delete Selected");
        deleteBtn.setBackground(new Color(239, 68, 68)); // Red
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(deleteBtn, gbc);

        deleteBtn.addActionListener(e -> {
            int row = companyTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a company to delete!");
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this company?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (adminDAO.deleteCompany(id)) {
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot delete company (it might have active jobs)!");
                }
            }
        });

        contentArea.add(listPanel, BorderLayout.CENTER);
        contentArea.add(formPanel, BorderLayout.EAST);

        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Company> companies = adminDAO.getAllCompanies();
        for (Company c : companies) {
            tableModel.addRow(new Object[]{c.getId(), c.getName(), c.getIndustry()}); // Reusing industry field for location in model for now
        }
    }
}
