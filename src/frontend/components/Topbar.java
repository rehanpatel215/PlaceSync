package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class Topbar extends JPanel {
    public Topbar(String titleText) {
        setPreferredSize(new Dimension(0, 80));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR));
        
        // Page Title & Context
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 25));
        leftPanel.setOpaque(false);
        
        JLabel title = new JLabel(titleText);
        title.setFont(Theme.FONT_BOLD);
        title.setForeground(Theme.TEXT_PRIMARY);
        leftPanel.add(title);
        add(leftPanel, BorderLayout.WEST);

        // Right side (Search & Profile)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        rightPanel.setOpaque(false);

        // Modern Search Field (using StyledTextField for proper placeholder)
        StyledTextField searchField = new StyledTextField("Search...");
        searchField.setPreferredSize(new Dimension(250, 40));
        rightPanel.add(searchField);

        // Profile Section
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        profilePanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel("Admin User");
        nameLabel.setFont(Theme.FONT_SMALL);
        nameLabel.setForeground(Theme.TEXT_PRIMARY);
        profilePanel.add(nameLabel);

        JPanel avatar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_TEAL);
                g2.fillOval(0, 0, 32, 32);
            }
        };
        avatar.setPreferredSize(new Dimension(32, 32));
        avatar.setOpaque(false);
        profilePanel.add(avatar);
        
        rightPanel.add(profilePanel);
        add(rightPanel, BorderLayout.EAST);
    }
}
