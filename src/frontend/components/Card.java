package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class Card extends JPanel {
    private final JLabel valueLabel;
    private final JLabel trendLabel;

    public Card(String title, String value, Color accentColor) {
        setPreferredSize(new Dimension(280, 150));
        setBackground(Color.WHITE);
        setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 20, 4, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // 1. Color Indicator Dot
        JPanel indicator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accentColor);
                g2.fillOval(0, 0, 8, 8);
            }
        };
        indicator.setPreferredSize(new Dimension(8, 8));
        indicator.setOpaque(false);
        gbc.gridy = 0;
        add(indicator, gbc);

        // 2. Title (Uppercase, Tertiary Color)
        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(Theme.FONT_XS);
        titleLabel.setForeground(Theme.TEXT_TERTIARY);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 20, 2, 20);
        add(titleLabel, gbc);

        // 3. Value
        valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 32));
        valueLabel.setForeground(Theme.TEXT_PRIMARY);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 20, 2, 20);
        add(valueLabel, gbc);
        
        // 4. Trend
        trendLabel = new JLabel("");
        trendLabel.setFont(Theme.FONT_XS);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 20, 15, 20);
        add(trendLabel, gbc);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public void setTrend(String text, boolean positive) {
        trendLabel.setText(text);
        trendLabel.setForeground(positive ? Theme.ACCENT_EMERALD : new Color(0xA32D2D));
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
