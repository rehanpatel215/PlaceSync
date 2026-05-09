package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sidebar extends JPanel {
    public Sidebar() {
        this(null); // Default to checking AuthManager if no role provided
    }

    private JPanel navPanel;

    public Sidebar(String role) {
        setPreferredSize(new Dimension(60, 0)); // Slim sidebar (Upstream style)
        setBackground(Theme.SIDEBAR_BG);
        setLayout(new BorderLayout());
        
        // Logo Section
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_TEAL);
                g2.fillRoundRect(8, 10, 44, 44, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("P", (getWidth() - fm.stringWidth("P")) / 2, 40);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(60, 80));
        add(logoPanel, BorderLayout.NORTH);

        // Navigation Items
        navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
        navPanel.setOpaque(false);
        add(navPanel, BorderLayout.CENTER);

        refreshMenu(role);

        // Logout Section (Minimalist for Upstream)
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        logoutPanel.setOpaque(false);
        
        JButton logoutBtn = createSidebarBtn("🚪", "Logout", e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                backend.auth.AuthManager.getInstance().logout();
                frontend.NavigationManager.showLogin();
            }
        }, false);
        logoutPanel.add(logoutBtn);
        add(logoutPanel, BorderLayout.SOUTH);
    }

    public void refreshMenu(String role) {
        navPanel.removeAll();
        
        String activeRole = role;
        if (activeRole == null) {
            backend.auth.AuthManager auth = backend.auth.AuthManager.getInstance();
            if (auth.isAdmin()) activeRole = "Admin";
            else if (auth.isStudent()) activeRole = "Student";
        }

        if ("Admin".equals(activeRole)) {
            navPanel.add(createSidebarBtn("▦", "Dashboard", e -> frontend.NavigationManager.showAdminDashboard(), true));
            navPanel.add(createSidebarBtn("🏢", "Companies", e -> frontend.NavigationManager.showCompanyMgmt(), false));
            navPanel.add(createSidebarBtn("📋", "Jobs", e -> frontend.NavigationManager.showJobMgmt(), false));
            navPanel.add(createSidebarBtn("👥", "Applications", e -> frontend.NavigationManager.showAppMgmt(), false));
        } else if ("Student".equals(activeRole)) {
            navPanel.add(createSidebarBtn("▦", "Dashboard", e -> frontend.NavigationManager.showStudentDashboard(), true));
            navPanel.add(createSidebarBtn("🔍", "Browse Jobs", e -> frontend.NavigationManager.showJobDiscovery(), false));
            navPanel.add(createSidebarBtn("👤", "My Profile", e -> frontend.NavigationManager.showProfile(), false));
        }
        
        navPanel.add(createSidebarBtn("⚙", "Settings", e -> frontend.NavigationManager.showSettings(), false));
        
        navPanel.revalidate();
        navPanel.repaint();
    }

    private JButton createSidebarBtn(String icon, String tooltip, java.awt.event.ActionListener action, boolean active) {
        JButton btn = new JButton(icon) {
            @Override
            protected void paintComponent(Graphics g) {
                if (active) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Theme.PRIMARY_TEAL);
                    g2.fillRoundRect(4, 2, getWidth()-8, getHeight()-4, 10, 10);
                }
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(48, 40));
        btn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
        btn.setForeground(active ? Color.WHITE : Theme.SIDEBAR_TXT);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setToolTipText(tooltip);
        if (action != null) btn.addActionListener(action);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (!active) btn.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e) { if (!active) btn.setForeground(Theme.SIDEBAR_TXT); }
        });
        return btn;
    }
}
