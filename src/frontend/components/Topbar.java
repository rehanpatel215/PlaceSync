package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class Topbar extends JPanel {
    private JLabel nameLabel;

    public Topbar(String titleText) {
        setPreferredSize(new Dimension(0, 60));
        setBackground(Theme.CARD_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
        
        // Brand Section
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        leftPanel.setOpaque(false);
        
        JLabel brand = new JLabel("⊕ PlaceSync");
        brand.setFont(new Font("SansSerif", Font.BOLD, 16));
        brand.setForeground(Theme.PRIMARY_TEAL);
        leftPanel.add(brand);

        // Context Title
        JLabel contextTitle = new JLabel(" | " + titleText);
        contextTitle.setFont(Theme.FONT_SM);
        contextTitle.setForeground(Theme.TEXT_SECONDARY);
        leftPanel.add(contextTitle);
        
        add(leftPanel, BorderLayout.WEST);

        // Right side (Search & Notifications & Profile)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        rightPanel.setOpaque(false);

        // Styled Search Field
        StyledTextField searchField = new StyledTextField("Search jobs, events...");
        searchField.setPreferredSize(new Dimension(200, 32));
        searchField.setBackground(new Color(0xF0F5F2));
        rightPanel.add(searchField);

        // Notification Bell
        JPanel bellContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                int count = new backend.queries.NotificationDAO().getUnreadCount(
                    backend.auth.AuthManager.getInstance().getCurrentUser() != null ? 
                    backend.auth.AuthManager.getInstance().getCurrentUser().getUserId() : -1
                );
                
                if (count > 0) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0xE24B4A)); // RED
                    g2.fillOval(getWidth() - 10, 0, 10, 10);
                }
            }
        };
        bellContainer.setOpaque(false);
        bellContainer.setPreferredSize(new Dimension(24, 24));
        bellContainer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel bellIcon = new JLabel("🔔");
        bellIcon.setFont(new Font("Serif", Font.PLAIN, 18));
        bellContainer.add(bellIcon);
        bellContainer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { showNotifications(bellContainer); }
        });
        rightPanel.add(bellContainer);

        // Profile Section
        nameLabel = new JLabel("Guest");
        updateUser();
        nameLabel.setFont(Theme.FONT_SM);
        nameLabel.setForeground(Theme.TEXT_SECONDARY);
        rightPanel.add(nameLabel);

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_TEAL);
                g2.fillOval(0, 0, 30, 30);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                FontMetrics fm = g2.getFontMetrics();
                String initial = nameLabel.getText().substring(0, 1).toUpperCase();
                g2.drawString(initial, (30-fm.stringWidth(initial))/2, 19);
            }
        };
        avatar.setPreferredSize(new Dimension(30, 30));
        avatar.setOpaque(false);
        rightPanel.add(avatar);
        
        add(rightPanel, BorderLayout.EAST);
    }

    private void showNotifications(Component invoker) {
        backend.models.User user = backend.auth.AuthManager.getInstance().getCurrentUser();
        if (user == null) return;

        backend.queries.NotificationDAO nDao = new backend.queries.NotificationDAO();
        java.util.List<java.util.Map<String, Object>> notes = nDao.getUnreadNotifications(user.getUserId());
        
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(Color.WHITE);
        popup.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR));

        if (notes.isEmpty()) {
            popup.add(new JMenuItem("No new notifications")).setEnabled(false);
        } else {
            for (java.util.Map<String, Object> note : notes) {
                String msg = (String) note.get("message");
                // Wrap text if too long
                if (msg.length() > 40) msg = "<html>" + msg.substring(0, 37) + "...</html>";
                JMenuItem item = new JMenuItem(msg);
                item.setFont(Theme.FONT_SMALL);
                popup.add(item);
            }
            popup.addSeparator();
            JMenuItem markAll = new JMenuItem("Mark all as read");
            markAll.setForeground(Theme.PRIMARY_TEAL);
            markAll.addActionListener(e -> {
                nDao.markAsRead(user.getUserId());
                repaint();
            });
            popup.add(markAll);
        }
        
        popup.show(invoker, 0, invoker.getHeight());
    }

    public void updateUser() {
        backend.models.User user = backend.auth.AuthManager.getInstance().getCurrentUser();
        if (user != null) {
            nameLabel.setText(user.getUsername());
            repaint(); // Trigger badge recount
        } else {
            nameLabel.setText("Guest");
        }
    }
}
