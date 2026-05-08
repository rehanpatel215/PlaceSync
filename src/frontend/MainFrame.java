package frontend;

import frontend.panels.AdminDashboard;
import frontend.panels.LoginPanel;
import frontend.panels.StudentDashboard;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("PlaceSync - College Placement Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Register Navigation Manager
        NavigationManager.setMainFrame(this);

        // Add Panels
        mainPanel.add(new LoginPanel(), "LOGIN");
        mainPanel.add(new AdminDashboard(), "ADMIN_DASHBOARD");
        mainPanel.add(new StudentDashboard(), "STUDENT_DASHBOARD");
        mainPanel.add(new frontend.panels.CompanyManagementPanel(), "COMPANY_MGMT");
        mainPanel.add(new frontend.panels.JobManagementPanel(), "JOB_MGMT");
        mainPanel.add(new frontend.panels.ApplicationManagementPanel(), "APP_MGMT");

        add(mainPanel);
        
        // Show Login by default
        showPanel("LOGIN");
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
}
