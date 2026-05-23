package frontend;

import frontend.panels.AdminDashboard;
import frontend.panels.LoginPanel;
import frontend.panels.StudentDashboard;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final CardLayout cardLayout;
    private final JPanel mainPanel;

    // Panels that need data refresh
    private final AdminDashboard adminDashboard;
    private final StudentDashboard studentDashboard;
    private final frontend.panels.StudentProfilePanel studentProfilePanel;
    private final frontend.panels.JobDiscoveryPanel jobDiscoveryPanel;
    private final frontend.panels.SettingsPanel settingsPanel;
    private final frontend.panels.ApplicationManagementPanel applicationManagementPanel;
    private final frontend.panels.CompanyManagementPanel companyManagementPanel;
    private final frontend.panels.JobManagementPanel jobManagementPanel;

    public MainFrame() {
        setTitle("PlaceSync - College Placement Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);


        // Instantiate Panels
        adminDashboard = new AdminDashboard();
        studentDashboard = new StudentDashboard();
        studentProfilePanel = new frontend.panels.StudentProfilePanel();
        jobDiscoveryPanel = new frontend.panels.JobDiscoveryPanel();
        settingsPanel = new frontend.panels.SettingsPanel();
        applicationManagementPanel = new frontend.panels.ApplicationManagementPanel();
        companyManagementPanel = new frontend.panels.CompanyManagementPanel();
        jobManagementPanel = new frontend.panels.JobManagementPanel();

        // Add Panels
        mainPanel.add(new LoginPanel(), "LOGIN");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        mainPanel.add(studentDashboard, "STUDENT_DASHBOARD");
        mainPanel.add(companyManagementPanel, "COMPANY_MGMT");
        mainPanel.add(jobManagementPanel, "JOB_MGMT");
        mainPanel.add(applicationManagementPanel, "APP_MGMT");
        mainPanel.add(jobDiscoveryPanel, "JOB_DISCOVERY");
        mainPanel.add(studentProfilePanel, "STUDENT_PROFILE");
        mainPanel.add(settingsPanel, "SETTINGS");

        add(mainPanel);
        
        // Show Login by default
        showPanel("LOGIN");

        // Register Navigation Manager (Deferred to avoid 'this' leakage during construction)
        SwingUtilities.invokeLater(() -> NavigationManager.setMainFrame(this));
    }

    public final void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }

    public void refreshStudentPanels() {
        if (studentDashboard != null) {
            studentDashboard.refreshData();
            studentDashboard.refreshUser();
            studentDashboard.refreshSidebar();
        }
        if (studentProfilePanel != null) {
            studentProfilePanel.loadProfileData();
            studentProfilePanel.refreshUser();
            studentProfilePanel.refreshSidebar();
        }
        if (jobDiscoveryPanel != null) {
            jobDiscoveryPanel.refreshJobs();
            jobDiscoveryPanel.refreshUser();
            jobDiscoveryPanel.refreshSidebar();
        }
        if (settingsPanel != null) {
            settingsPanel.refreshUser();
            settingsPanel.refreshSidebar();
        }
    }

    public void refreshAdminPanels() {
        if (adminDashboard != null) {
            adminDashboard.refreshData();
            adminDashboard.refreshUser();
            adminDashboard.refreshSidebar();
        }
        if (applicationManagementPanel != null) {
            applicationManagementPanel.refreshData();
            applicationManagementPanel.refreshUser();
            applicationManagementPanel.refreshSidebar();
        }
        if (companyManagementPanel != null) {
            companyManagementPanel.refreshData();
            companyManagementPanel.refreshUser();
            companyManagementPanel.refreshSidebar();
        }
        if (jobManagementPanel != null) {
            jobManagementPanel.refreshData();
            jobManagementPanel.refreshUser();
            jobManagementPanel.refreshSidebar();
        }
        if (settingsPanel != null) {
            settingsPanel.refreshUser();
            settingsPanel.refreshSidebar();
        }
    }
}
