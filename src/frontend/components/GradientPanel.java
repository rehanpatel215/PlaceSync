package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
    private Color colorStart;
    private Color colorEnd;

    public GradientPanel(Color start, Color end) {
        this.colorStart = start;
        this.colorEnd = end;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gradient = new GradientPaint(
            0, 0, colorStart,
            getWidth(), getHeight(), colorEnd
        );
        
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.ROUNDING, Theme.ROUNDING);
        g2.dispose();
        
        super.paintComponent(g);
    }
}
