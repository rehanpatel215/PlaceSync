package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sidebar extends JPanel {
    public Sidebar() {
        setPreferredSize(new Dimension(80, 0)); // Slim sidebar
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER_COLOR));
        
        // Logo Section
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(80, 80));
        JLabel logo = new JLabel("P", SwingConstants.CENTER); // Minimalist logo
        logo.setFont(new Font("Inter", Font.BOLD, 28));
        logo.setForeground(Theme.PRIMARY_TEAL);
        logoPanel.add(logo);
        add(logoPanel, BorderLayout.NORTH);

        // Navigation Items
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        navPanel.setOpaque(false);

        addIconItem(navPanel, "⊞", "Dashboard", e -> frontend.NavigationManager.showAdminDashboard());
        addIconItem(navPanel, "🏢", "Companies", e -> frontend.NavigationManager.showCompanyMgmt());
        addIconItem(navPanel, "📋", "Jobs", e -> frontend.NavigationManager.showJobMgmt());
        addIconItem(navPanel, "👥", "Applications", e -> frontend.NavigationManager.showAppMgmt());
        addIconItem(navPanel, "⚙", "Settings", null);

        add(navPanel, BorderLayout.CENTER);

        // Logout Section
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        logoutPanel.setOpaque(false);
        
        JButton logoutBtn = new JButton("🚪");
        logoutBtn.setPreferredSize(new Dimension(50, 50));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setContentAreaFilled(false);
        logoutBtn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        logoutBtn.setForeground(new Color(239, 68, 68)); // Red for logout
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setToolTipText("Logout");
        
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                SwingUtilities.getWindowAncestor(this), 
                "Are you sure you want to logout?", 
                "Logout", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION) {
                backend.auth.AuthManager.getInstance().logout();
                frontend.NavigationManager.showLogin();
            }
        });
        
        logoutPanel.add(logoutBtn);
        add(logoutPanel, BorderLayout.SOUTH);
    }

    private void addIconItem(JPanel panel, String icon, String tooltip, java.awt.event.ActionListener action) {
        JButton btn = new JButton(icon);
        btn.setPreferredSize(new Dimension(50, 50));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20)); // For icons
        btn.setForeground(Theme.TEXT_SECONDARY);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        
        if (action != null) btn.addActionListener(action);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setForeground(Theme.PRIMARY_TEAL);
            }
            public void mouseExited(MouseEvent evt) {
                btn.setForeground(Theme.TEXT_SECONDARY);
            }
        });

        panel.add(btn);
    }
}
