package frontend.panels;

import frontend.components.Sidebar;
import frontend.components.Topbar;
import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboardPanel extends JPanel {
    protected JPanel contentArea;
    protected Topbar topbar;
    protected Sidebar sidebar;
    
    public BaseDashboardPanel(String title) {
        this(title, null);
    }

    public BaseDashboardPanel(String title, String role) {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Sidebar (Left)
        sidebar = new Sidebar(role);
        add(sidebar, BorderLayout.WEST);

        // Main Container (Center)
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        
        // Topbar
        topbar = new Topbar(title);
        mainContainer.add(topbar, BorderLayout.NORTH);

        // Dynamic Content Area
        contentArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        contentArea.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);
    }
    public void refreshSidebar() {
        if (sidebar != null) sidebar.refreshMenu(null);
    }

    public void refreshUser() {
        if (topbar != null) topbar.updateUser();
    }
}
