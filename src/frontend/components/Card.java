package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {
    public Card(String title, String value, Color accentColor) {
        setPreferredSize(new Dimension(280, 140));
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1, true));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 20, 5, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Top Row: Title + Color Indicator
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.FONT_SMALL);
        titleLabel.setForeground(Theme.TEXT_SECONDARY);
        topRow.add(titleLabel, BorderLayout.WEST);

        // Circle Indicator
        JPanel indicator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 0, 10, 10);
            }
        };
        indicator.setPreferredSize(new Dimension(10, 10));
        indicator.setOpaque(false);
        topRow.add(indicator, BorderLayout.EAST);

        gbc.gridy = 0;
        add(topRow, gbc);

        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 28));
        valueLabel.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 20, 10, 20);
        add(valueLabel, gbc);
        
        // Context/Trend (Mock)
        JLabel trend = new JLabel("+12% from last month");
        trend.setFont(Theme.FONT_SMALL);
        trend.setForeground(Theme.ACCENT_EMERALD);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 15, 20);
        add(trend, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.ROUNDING, Theme.ROUNDING);
        super.paintComponent(g);
    }
}
