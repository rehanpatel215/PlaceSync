package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StyledPasswordField extends JPasswordField {
    private String placeholder;
    private Color activeColor = Theme.PRIMARY_TEAL;
    private Color idleColor = Theme.BORDER_COLOR;
    private boolean isFocused = false;

    public StyledPasswordField(String placeholder) {
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
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        
        // Border
        g2.setColor(isFocused ? activeColor : idleColor);
        g2.setStroke(new BasicStroke(isFocused ? 2f : 1f));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        
        g2.dispose();
        super.paintComponent(g);

        // Paint Placeholder
        if (getPassword().length == 0 && !isFocused) {
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
