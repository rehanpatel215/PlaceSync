package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setOpaque(true);
        panel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());

        if (value == null) return panel;

        String status = value.toString();
        JLabel label = new JLabel(status);
        label.setFont(Theme.FONT_XS);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        
        styleBadge(label, status);
        
        panel.add(label);
        return panel;
    }

    private void styleBadge(JLabel label, String status) {
        switch (status.toLowerCase()) {
            case "pending":
                label.setBackground(new Color(0xFAEEDA));
                label.setForeground(new Color(0x854F0B));
                break;
            case "selected":
            case "placed":
            case "approved":
                label.setBackground(new Color(0xEAF3DE));
                label.setForeground(new Color(0x3B6D11));
                break;
            case "rejected":
            case "declined":
                label.setBackground(new Color(0xFCEBEB));
                label.setForeground(new Color(0xA32D2D));
                break;
            default:
                label.setBackground(new Color(0xF3F4F6));
                label.setForeground(new Color(0x6B8A7A));
                break;
        }
        
        // Rounding the badge
        label.setUI(new javax.swing.plaf.basic.BasicLabelUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }
}
