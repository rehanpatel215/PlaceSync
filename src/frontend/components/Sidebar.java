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
        setPreferredSize(new Dimension(72, 0)); // Wider for labels
        setBackground(Theme.BG_DARK_GREEN);
        setLayout(new BorderLayout());
        
        // Logo Section
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_TEAL);
                g2.fillRoundRect(16, 20, 40, 40, 12, 12);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Inter", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("P", 16 + (40 - fm.stringWidth("P")) / 2, 47);
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(72, 80));
        add(logoPanel, BorderLayout.NORTH);

        // Navigation Items
        navPanel = new JPanel();
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        navPanel.setOpaque(false);
        add(navPanel, BorderLayout.CENTER);

        refreshMenu(role);

        // Logout Section
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        logoutPanel.setOpaque(false);
        
        JButton logoutBtn = createSidebarBtn("🚪", "Exit", e -> {
            int confirm = JOptionPane.showConfirmDialog(null, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                backend.auth.AuthManager.getInstance().logout();
                frontend.NavigationManager.showLogin();
            }
        }, false);
        logoutPanel.add(logoutBtn);
        add(logoutPanel, BorderLayout.SOUTH);
    }

    public final void refreshMenu(String role) {
        navPanel.removeAll();
        
        String activeRole = role;
        if (activeRole == null) {
            backend.auth.AuthManager auth = backend.auth.AuthManager.getInstance();
            if (auth.isAdmin()) activeRole = "Admin";
            else if (auth.isStudent()) activeRole = "Student";
        }

        if ("Admin".equals(activeRole)) {
            navPanel.add(createSidebarBtn("▦", "Home", e -> frontend.NavigationManager.showAdminDashboard(), true));
            navPanel.add(createSidebarBtn("🏢", "Cos", e -> frontend.NavigationManager.showCompanyMgmt(), false));
            navPanel.add(createSidebarBtn("📋", "Jobs", e -> frontend.NavigationManager.showJobMgmt(), false));
            navPanel.add(createSidebarBtn("👥", "Apps", e -> frontend.NavigationManager.showAppMgmt(), false));
        } else if ("Student".equals(activeRole)) {
            navPanel.add(createSidebarBtn("▦", "Home", e -> frontend.NavigationManager.showStudentDashboard(), true));
            navPanel.add(createSidebarBtn("🔍", "Jobs", e -> frontend.NavigationManager.showJobDiscovery(), false));
            navPanel.add(createSidebarBtn("👤", "Profile", e -> frontend.NavigationManager.showProfile(), false));
        }
        
        navPanel.add(createSidebarBtn("⚙", "Settings", e -> frontend.NavigationManager.showSettings(), false));
        
        navPanel.revalidate();
        navPanel.repaint();
    }

    private JButton createSidebarBtn(String icon, String labelText, java.awt.event.ActionListener action, boolean active) {
        JButton btn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (active) {
                    g2.setColor(new Color(255, 255, 255, 30));
                    g2.fillRoundRect(8, 2, getWidth()-16, getHeight()-4, 12, 12);
                }
                
                // Draw Icon
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 18));
                g2.setColor(active ? Color.WHITE : Theme.SIDEBAR_TXT);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(icon, (getWidth()-fm.stringWidth(icon))/2, 24);
                
                // Draw Label
                g2.setFont(Theme.FONT_XS);
                g2.setColor(active ? Color.WHITE : Theme.TURQUOISE);
                fm = g2.getFontMetrics();
                g2.drawString(labelText, (getWidth()-fm.stringWidth(labelText))/2, 40);
                
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(64, 48));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (action != null) btn.addActionListener(action);
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { if (!active) btn.repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { if (!active) btn.repaint(); }
        });
        return btn;
    }
}
