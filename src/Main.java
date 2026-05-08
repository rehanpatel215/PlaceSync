
import com.formdev.flatlaf.FlatDarkLaf;
import frontend.MainFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Apply modern Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // Start application
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
