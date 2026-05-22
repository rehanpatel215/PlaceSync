package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StyledTextField extends JTextField {
    private final String placeholder;
    private final Color activeColor = Theme.PRIMARY_TEAL;
    private final Color idleColor = Theme.BORDER_COLOR;
    private boolean isFocused = false;

    public StyledTextField(String placeholder) {
        this.placeholder = placeholder;
        setOpaque(false);
        setFont(Theme.FONT_REG);
        setForeground(Theme.TEXT_PRIMARY);
        setCaretColor(Theme.PRIMARY_TEAL);
        setBorder(new EmptyBorder(10, 15, 10, 15));

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Background
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Theme.INPUT_ROUNDING, Theme.INPUT_ROUNDING);
        
        // Border
        g2.setColor(isFocused ? activeColor : idleColor);
        g2.setStroke(new BasicStroke(isFocused ? 2f : 1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Theme.INPUT_ROUNDING, Theme.INPUT_ROUNDING);
        
        g2.dispose();
        super.paintComponent(g);

        // Paint Placeholder
        if (getText().isEmpty() && !isFocused) {
            Graphics2D gPlaceholder = (Graphics2D) g.create();
            gPlaceholder.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            gPlaceholder.setColor(Theme.TEXT_SECONDARY);
            gPlaceholder.setFont(getFont());
            FontMetrics fm = gPlaceholder.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            gPlaceholder.drawString(placeholder, 15, y);
            gPlaceholder.dispose();
        }
    }
}
