
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import frontend.MainFrame;

public class Main {
    public static void main(String[] args) {
        // 1. Update Database Schema (Create missing tables)
        SchemaUpdate.main(args);

        // 2. Apply modern Look and Feel
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
            frontend.theme.Theme.applyGlobalTheme();
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
