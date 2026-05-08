package frontend.theme;

import java.awt.*;
import javax.swing.*;

public class Theme {
    // Upstream Inspired Palette
    public static final Color BACKGROUND = new Color(243, 244, 246); // Very light gray/blue
    public static final Color SIDEBAR_BG = new Color(255, 255, 255);
    public static final Color CARD_BG = Color.WHITE;
    
    // Vibrant Accents
    public static final Color PRIMARY_TEAL = new Color(13, 148, 136);    // Upstream Teal
    public static final Color ACCENT_EMERALD = new Color(16, 185, 129); // Success Emerald
    public static final Color TURQUOISE = new Color(6, 182, 212);
    public static final Color AMBER = new Color(245, 158, 11);          // Warning Amber
    
    // Text Colors
    public static final Color TEXT_PRIMARY = new Color(17, 24, 39);     // Darkest Gray
    public static final Color TEXT_SECONDARY = new Color(107, 114, 128); // Muted
    
    // Fonts (Using Inter-like sizing)
    public static final Font FONT_TITLE = new Font("Inter", Font.BOLD, 28);
    public static final Font FONT_BOLD = new Font("Inter", Font.BOLD, 16);
    public static final Font FONT_REGULAR = new Font("Inter", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Inter", Font.PLAIN, 12);
    
    // UI Effects
    public static final int ROUNDING = 16;
    public static final Color BORDER_COLOR = new Color(229, 231, 235);
    
    public static void applyGlobalTheme() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Label.foreground", TEXT_PRIMARY);
        UIManager.put("Button.arc", 12);
        UIManager.put("Component.arc", 12);
        UIManager.put("TextComponent.arc", 12);
        
        // Customizing ScrollBars for a cleaner look
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("ScrollBar.track", BACKGROUND);
        UIManager.put("ScrollBar.thumb", new Color(209, 213, 219));
    }
}


