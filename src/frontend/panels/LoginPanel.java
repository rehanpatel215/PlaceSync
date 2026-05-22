package frontend.panels;

import frontend.NavigationManager;
import frontend.theme.Theme;
import frontend.components.RoundedButton;
import frontend.components.ShadowPanel;
import frontend.components.StyledTextField;
import frontend.components.StyledPasswordField;
import backend.auth.AuthManager;
import backend.queries.UserDAO;
import backend.models.User;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final StyledTextField userField;
    private final StyledPasswordField passField;
    private final RoundedButton loginBtn;
    private String selectedRole = "Admin";

    public LoginPanel() {
        setBackground(Theme.BG_DARK_GREEN);
        setLayout(new GridBagLayout());
        
        // 1. Create the Login Card Content
        JPanel cardContent = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.ROUNDING, Theme.ROUNDING);
                g2.dispose();
            }
        };
        cardContent.setOpaque(false);
        cardContent.setPreferredSize(new Dimension(380, 520));
        
        GridBagConstraints cgbc = new GridBagConstraints();
        cgbc.insets = new Insets(10, 30, 10, 30);
        cgbc.fill = GridBagConstraints.HORIZONTAL;
        cgbc.weightx = 1.0;

        // Branding: Logo Row
        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        logoRow.setOpaque(false);
        
        JPanel logoBox = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY_TEAL);
                g2.fillRoundRect(0, 0, 32, 32, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Inter", Font.BOLD, 18));
                g2.drawString("P", 10, 23);
            }
        };
        logoBox.setPreferredSize(new Dimension(32, 32));
        logoBox.setOpaque(false);
        logoRow.add(logoBox);
        
        JLabel title = new JLabel("PlaceSync");
        title.setFont(new Font("Inter", Font.BOLD, 22));
        title.setForeground(Theme.BG_DARK_GREEN);
        logoRow.add(title);
        
        cgbc.gridy = 0;
        cgbc.insets = new Insets(30, 0, 5, 0);
        cardContent.add(logoRow, cgbc);

        JLabel subtitle = new JLabel("Career placement platform", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_XS);
        subtitle.setForeground(Theme.TEXT_TERTIARY);
        cgbc.gridy = 1;
        cgbc.insets = new Insets(0, 0, 25, 0);
        cardContent.add(subtitle, cgbc);

        // 2. Pill Toggle (Modern Style)
        JPanel pillToggle = new JPanel(new GridLayout(1, 2, 4, 4));
        pillToggle.setBackground(new Color(0xF0F4F2));
        pillToggle.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        pillToggle.setPreferredSize(new Dimension(0, 44));
        
        RoundedButton adminToggle = createPillBtn("Admin", true);
        RoundedButton studentToggle = createPillBtn("Student", false);
        
        adminToggle.addActionListener(e -> {
            selectedRole = "Admin";
            updatePillUI(adminToggle, studentToggle);
        });
        
        studentToggle.addActionListener(e -> {
            selectedRole = "Student";
            updatePillUI(studentToggle, adminToggle);
        });

        pillToggle.add(adminToggle);
        pillToggle.add(studentToggle);
        
        cgbc.gridy = 2;
        cgbc.insets = new Insets(0, 30, 25, 30);
        cardContent.add(pillToggle, cgbc);

        // 3. Inputs
        cgbc.insets = new Insets(5, 30, 5, 30);
        
        userField = new StyledTextField("Username");
        userField.setPreferredSize(new Dimension(0, 45));
        cgbc.gridy = 4;
        cardContent.add(userField, cgbc);

        passField = new StyledPasswordField("pass");
        passField.setPreferredSize(new Dimension(0, 45));
        cgbc.gridy = 5;
        cgbc.insets = new Insets(10, 30, 5, 30);
        cardContent.add(passField, cgbc);

        // 4. Login Button
        loginBtn = new RoundedButton("Sign in →");
        loginBtn.setPreferredSize(new Dimension(0, 50));
        cgbc.gridy = 6;
        cgbc.insets = new Insets(30, 30, 15, 30);
        cardContent.add(loginBtn, cgbc);
        
        JLabel forgot = new JLabel("Forgot password?", SwingConstants.CENTER);
        forgot.setFont(Theme.FONT_XS);
        forgot.setForeground(Theme.TEXT_TERTIARY);
        forgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cgbc.gridy = 7;
        cgbc.insets = new Insets(0, 0, 30, 0);
        cardContent.add(forgot, cgbc);

        // Wrap in ShadowPanel
        ShadowPanel shadowCard = new ShadowPanel(cardContent, Theme.ROUNDING);
        add(shadowCard);
        
        loginBtn.addActionListener(e -> handleLogin());
    }

    private RoundedButton createPillBtn(String text, boolean active) {
        RoundedButton btn = new RoundedButton(text);
        btn.setFont(Theme.FONT_SM);
        stylePillBtn(btn, active);
        return btn;
    }

    private void stylePillBtn(RoundedButton btn, boolean active) {
        if (active) {
            btn.setBackground(Color.WHITE);
            btn.setForeground(Theme.BG_DARK_GREEN);
        } else {
            btn.setBackground(new Color(0, 0, 0, 0));
            btn.setForeground(Theme.TEXT_SECONDARY);
        }
    }

    private void updatePillUI(RoundedButton active, RoundedButton inactive) {
        stylePillBtn(active, true);
        stylePillBtn(inactive, false);
        active.repaint();
        inactive.repaint();
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
