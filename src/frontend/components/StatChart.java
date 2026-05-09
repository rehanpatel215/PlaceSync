package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class StatChart extends JPanel {
    private Map<String, Integer> data;
    private String title;
    private final Color[] COLORS = {
        Theme.PRIMARY_TEAL,
        Theme.TURQUOISE,
        Theme.ACCENT_EMERALD,
        Theme.AMBER,
        new Color(239, 68, 68) // Red for Rejections
    };

    public StatChart(String title, Map<String, Integer> data) {
        this.title = title;
        this.data = data;
        setPreferredSize(new Dimension(300, 300));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height) - 100;
        int x = (width - size) / 2;
        int y = (height - size) / 2 + 20;

        // Draw Title
        g2.setFont(Theme.FONT_BOLD);
        g2.setColor(Theme.TEXT_PRIMARY);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, (width - fm.stringWidth(title)) / 2, 30);

        if (data == null || data.isEmpty()) {
            g2.setFont(Theme.FONT_REGULAR);
            String msg = "No data available";
            g2.drawString(msg, (width - fm.stringWidth(msg)) / 2, height / 2);
            return;
        }

        int total = data.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return;

        double startAngle = 90;
        int i = 0;
        
        // Draw Pie Segments
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            double extent = (double) entry.getValue() / total * 360;
            g2.setColor(COLORS[i % COLORS.length]);
            g2.fillArc(x, y, size, size, (int) startAngle, (int) -extent);
            
            // Draw Legend
            int ly = height - 40 + (i / 3) * 20;
            int lx = 20 + (i % 3) * 100;
            g2.fillRect(lx, ly, 12, 12);
            g2.setFont(Theme.FONT_SMALL);
            g2.setColor(Theme.TEXT_SECONDARY);
            g2.drawString(entry.getKey() + ": " + entry.getValue(), lx + 18, ly + 10);
            
            startAngle -= extent;
            i++;
        }
    }
}
