package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.RoundRectangle2D;

/**
 * A reusable panel that adds a soft drop shadow to its content.
 */
public class ShadowPanel extends JPanel {
    private final int shadowSize = 20;
    private final int cornerRadius;
    private final JComponent content;

    public ShadowPanel(JComponent content, int cornerRadius) {
        this.content = content;
        this.cornerRadius = cornerRadius;
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // Add padding for the shadow
        setBorder(BorderFactory.createEmptyBorder(shadowSize, shadowSize, shadowSize, shadowSize));
        add(content);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int x = shadowSize;
        int y = shadowSize;
        int w = getWidth() - 2 * shadowSize;
        int h = getHeight() - 2 * shadowSize;

        // Paint multiple layers for a soft shadow effect
        for (int i = 0; i < shadowSize; i++) {
            float alpha = (float) (shadowSize - i) / (shadowSize * 15f); // Very subtle
            g2.setColor(new Color(0, 0, 0, alpha));
            g2.fill(new RoundRectangle2D.Double(x - i, y - i + 2, w + 2 * i, h + 2 * i, cornerRadius + i, cornerRadius + i));
        }

        g2.dispose();
        super.paintComponent(g);
    }
}
