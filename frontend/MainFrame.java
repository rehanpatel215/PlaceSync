package frontend;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("PlaceSync - College Placement Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panels will be added here
        // mainPanel.add(new LoginPanel(this), "LOGIN");

        add(mainPanel);
    }

    public void showPanel(String name) {
        cardLayout.show(mainPanel, name);
    }
}
