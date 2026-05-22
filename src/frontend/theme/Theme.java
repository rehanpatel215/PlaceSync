package frontend.theme;

import java.awt.*;
import javax.swing.*;

public class Theme {
    // Upstream Brand Palette
    public static final Color PRIMARY_TEAL = new Color(0x1D9E75);
    public static final Color SIDEBAR_BG  = new Color(0x0F3D30);
    public static final Color SIDEBAR_TXT = new Color(0x9FE1CB);
    public static final Color BACKGROUND  = new Color(0xF4F7F5);
    public static final Color CARD_BG     = Color.WHITE;
    public static final Color BORDER_COLOR = new Color(0xDFEAE4);
    
    public static final Color TEXT_PRIMARY = new Color(0x1A2E25);
    public static final Color TEXT_SECONDARY = new Color(0x6B8A7A);
    
    public static final Color ACCENT_AMBER = new Color(0xEF9F27);
    public static final Color ACCENT_BLUE  = new Color(0x378ADD);
    public static final Color ACCENT_EMERALD = new Color(0x639922);
    public static final Color TURQUOISE = new Color(0x5DCAA5);
    public static final Color AMBER = ACCENT_AMBER;
    public static final Color BG_DARK_GREEN = new Color(0x0A2E22);
    public static final Color TEXT_TERTIARY = new Color(0x6B8A7A);
    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 30);

    // Upstream Typography
    public static final Font FONT_REG  = new Font("Inter", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Inter", Font.BOLD, 15);
    public static final Font FONT_SM   = new Font("Inter", Font.PLAIN, 11);
    public static final Font FONT_XS   = new Font("Inter", Font.PLAIN, 9);
    public static final Font FONT_LG   = new Font("Inter", Font.BOLD, 22);

    // Compatibility Aliases
    public static final Font FONT_REGULAR = FONT_REG;
    public static final Font FONT_SMALL = FONT_SM;
    public static final Font FONT_TITLE = FONT_LG;
    public static final Color BG_COLOR = BACKGROUND;
    public static final int ROUNDING = 16;
    public static final int INPUT_ROUNDING = 12;

    public static void applyGlobalTheme() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Label.font", FONT_REG);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("Button.background", PRIMARY_TEAL);
        UIManager.put("Button.foreground", Color.WHITE);
        
        // Table styling
        UIManager.put("Table.font", FONT_SM);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.selectionBackground", new Color(0xEAF3DE));
        UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", new Color(0xF0F0F0));
        UIManager.put("Table.rowHeight", 40);
        UIManager.put("TableHeader.font", FONT_SM);
        UIManager.put("TableHeader.background", new Color(0xF8FAF9));
        UIManager.put("TableHeader.foreground", TEXT_SECONDARY);
        
        // Input styling
        UIManager.put("TextField.font", FONT_REG);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // ScrollBar & ScrollPane
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("ScrollBar.track", BACKGROUND);
        UIManager.put("ScrollBar.thumb", new Color(0xB4B2A9));
    }
}
