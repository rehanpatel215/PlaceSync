package frontend.panels;

import frontend.components.Sidebar;
import frontend.components.Topbar;
import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public abstract class BaseDashboardPanel extends JPanel {
    protected JPanel contentArea;
    
    public BaseDashboardPanel(String title) {
        setLayout(new BorderLayout());
        setBackground(Theme.BACKGROUND);

        // Sidebar (Left)
        add(new Sidebar(), BorderLayout.WEST);

        // Main Container (Center)
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        
        // Topbar
        mainContainer.add(new Topbar(title), BorderLayout.NORTH);

        // Dynamic Content Area
        contentArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 30));
        contentArea.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(contentArea);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainContainer.add(scrollPane, BorderLayout.CENTER);
        add(mainContainer, BorderLayout.CENTER);
    }
}
