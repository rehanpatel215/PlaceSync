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

        // Add Panels
        mainPanel.add(new LoginPanel(), "LOGIN");
        mainPanel.add(adminDashboard, "ADMIN_DASHBOARD");
        mainPanel.add(studentDashboard, "STUDENT_DASHBOARD");
        mainPanel.add(new frontend.panels.CompanyManagementPanel(), "COMPANY_MGMT");
        mainPanel.add(new frontend.panels.JobManagementPanel(), "JOB_MGMT");
        mainPanel.add(new frontend.panels.ApplicationManagementPanel(), "APP_MGMT");
        mainPanel.add(jobDiscoveryPanel, "JOB_DISCOVERY");
        mainPanel.add(studentProfilePanel, "STUDENT_PROFILE");
        mainPanel.add(settingsPanel, "SETTINGS");

        add(mainPanel);
        
        // Register Navigation Manager
        NavigationManager.setMainFrame(this);
        
        // Show Login by default
        showPanel("LOGIN");
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
            adminDashboard.refreshUser();
            adminDashboard.refreshSidebar();
        }
        if (settingsPanel != null) {
            settingsPanel.refreshUser();
            settingsPanel.refreshSidebar();
        }
    }
}
