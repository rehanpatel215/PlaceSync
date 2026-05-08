
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import frontend.MainFrame;

public class Main {
    public static void main(String[] args) {
        // Apply modern Look and Feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            frontend.theme.Theme.applyGlobalTheme();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
