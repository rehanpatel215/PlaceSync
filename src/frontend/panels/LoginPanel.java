package frontend.panels;

import frontend.NavigationManager;
import frontend.theme.Theme;
import frontend.components.RoundedButton;
import frontend.components.StyledTextField;
import frontend.components.StyledPasswordField;
import backend.auth.AuthManager;
import backend.queries.UserDAO;
import backend.models.User;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private StyledTextField userField;
    private StyledPasswordField passField;
    private RoundedButton loginBtn;
    private String selectedRole = "Admin";

    public LoginPanel() {
        setBackground(Theme.BACKGROUND);
        setLayout(new GridBagLayout());
        
        // Main Login Card
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(420, 580));
        card.setBorder(BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1, true));
        
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(10, 40, 10, 40);
        cgbc.fill = GridBagConstraints.HORIZONTAL;
        cgbc.weightx = 1.0;

        // 1. Title & Subtitle
        JLabel title = new JLabel("PlaceSync", SwingConstants.CENTER);
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.PRIMARY_TEAL);
        cgbc.gridy = 0;
        cgbc.insets = new Insets(30, 0, 5, 0);
        card.add(title, cgbc);

        JLabel subtitle = new JLabel("Sustainability in Career Growth", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_SMALL);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        cgbc.gridy = 1;
        cgbc.insets = new Insets(0, 0, 30, 0);
        card.add(subtitle, cgbc);

        // 2. Role Toggle (Minimalist Upstream Style)
        JPanel togglePanel = new JPanel(new GridLayout(1, 2, 0, 0));
        togglePanel.setBackground(Theme.BACKGROUND);
        togglePanel.setPreferredSize(new Dimension(0, 45));
        
        JButton adminToggle = createToggleButton("Admin Login", true);
        JButton studentToggle = createToggleButton("Student Login", false);
        
        adminToggle.addActionListener(e -> {
            selectedRole = "Admin";
            updateToggleUI(adminToggle, studentToggle);
        });
        
        studentToggle.addActionListener(e -> {
            selectedRole = "Student";
            updateToggleUI(studentToggle, adminToggle);
        });

        togglePanel.add(adminToggle);
        togglePanel.add(studentToggle);
        
        cgbc.gridy = 2;
        cgbc.insets = new Insets(0, 40, 30, 40);
        card.add(togglePanel, cgbc);

        // 3. Inputs
        cgbc.insets = new Insets(5, 40, 5, 40);
        
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.FONT_SMALL);
        cgbc.gridy = 3;
        card.add(userLabel, cgbc);

        userField = new StyledTextField("admin");
        userField.setPreferredSize(new Dimension(0, 45));
        cgbc.gridy = 4;
        card.add(userField, cgbc);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.FONT_SMALL);
        cgbc.gridy = 5;
        cgbc.insets = new Insets(15, 40, 5, 40);
        card.add(passLabel, cgbc);

        passField = new StyledPasswordField("pass");
        passField.setPreferredSize(new Dimension(0, 45));
        cgbc.gridy = 6;
        card.add(passField, cgbc);

        // 4. Login Button
        loginBtn = new RoundedButton("Get Started");
        loginBtn.setPreferredSize(new Dimension(0, 50));
        cgbc.gridy = 7;
        cgbc.insets = new Insets(40, 40, 40, 40);
        card.add(loginBtn, cgbc);

        add(card);
        loginBtn.addActionListener(e -> handleLogin());
    }

    private JButton createToggleButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(Theme.FONT_BOLD);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        styleButton(btn, active);
        return btn;
    }

    private void styleButton(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(Theme.PRIMARY_TEAL);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(new Color(243, 244, 246));
            btn.setForeground(Theme.TEXT_SECONDARY);
        }
    }

    private void updateToggleUI(JButton active, JButton inactive) {
        styleButton(active, true);
        styleButton(inactive, false);
    }

    private void handleLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        UserDAO userDAO = new UserDAO();
        User user = userDAO.validateLogin(username, password);

        if (user != null) {
            if (!user.getRole().equalsIgnoreCase(selectedRole)) {
                JOptionPane.showMessageDialog(this, "This account is not registered as " + selectedRole, "Role Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AuthManager.getInstance().login(user);
            if (AuthManager.getInstance().isAdmin()) {
                NavigationManager.showAdminDashboard();
            } else {
                NavigationManager.showStudentDashboard();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
