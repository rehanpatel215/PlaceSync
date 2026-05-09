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

    // Upstream Typography
    public static final Font FONT_REG  = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 15);
    public static final Font FONT_SM   = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_LG   = new Font("SansSerif", Font.BOLD, 22);

    // Compatibility Aliases
    public static final Font FONT_REGULAR = FONT_REG;
    public static final Font FONT_SMALL = FONT_SM;
    public static final Font FONT_TITLE = FONT_LG;
    public static final Color BG_COLOR = BACKGROUND;
    public static final int ROUNDING = 12;

    public static void applyGlobalTheme() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Button.font", FONT_REG);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("ScrollBar.track", BACKGROUND);
        UIManager.put("ScrollBar.thumb", new Color(0xB4B2A9));
    }
}
