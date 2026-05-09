package frontend.panels;

import frontend.components.RoundedButton;
import frontend.components.StyledPasswordField;
import frontend.theme.Theme;
import backend.auth.AuthManager;
import backend.queries.UserDAO;
import backend.models.User;
import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends BaseDashboardPanel {
    private final UserDAO userDAO = new UserDAO();
    private final StyledPasswordField currentPassField;
    private final StyledPasswordField newPassField;
    private final StyledPasswordField confirmPassField;

    public SettingsPanel() {
        super("System Settings", AuthManager.getInstance().isAdmin() ? "Admin" : "Student");
        
        contentArea.setLayout(new GridBagLayout());
        
        // Main Container Card
        JPanel card = new JPanel(new BorderLayout(0, 30));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(40, 50, 40, 50)
        ));
        card.setPreferredSize(new Dimension(500, 600));

        // Header Section
        JPanel header = new JPanel(new BorderLayout(0, 10));
        header.setOpaque(false);
        
        JLabel iconLabel = new JLabel("🛡️", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        header.add(iconLabel, BorderLayout.NORTH);

        JLabel title = new JLabel("Security Settings", SwingConstants.CENTER);
        title.setFont(Theme.FONT_LG);
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.CENTER);

        JLabel subtitle = new JLabel("Update your password to keep your account secure", SwingConstants.CENTER);
        subtitle.setFont(Theme.FONT_SM);
        subtitle.setForeground(Theme.TEXT_SECONDARY);
        header.add(subtitle, BorderLayout.SOUTH);

        card.add(header, BorderLayout.NORTH);

        // Form Section
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.weightx = 1.0;
        fgbc.gridx = 0;

        // Fields with explicit gridy
        fgbc.gridy = 0;
        form.add(createLabel("Current Password"), fgbc);
        fgbc.gridy = 1;
        currentPassField = new StyledPasswordField("********");
        currentPassField.setPreferredSize(new Dimension(0, 40));
        currentPassField.setBackground(new Color(0xF0F5F2));
        form.add(currentPassField, fgbc);

        fgbc.gridy = 2;
        form.add(createLabel("New Password"), fgbc);
        fgbc.gridy = 3;
        newPassField = new StyledPasswordField("********");
        newPassField.setPreferredSize(new Dimension(0, 40));
        newPassField.setBackground(new Color(0xF0F5F2));
        form.add(newPassField, fgbc);

        fgbc.gridy = 4;
        form.add(createLabel("Confirm New Password"), fgbc);
        fgbc.gridy = 5;
        confirmPassField = new StyledPasswordField("********");
        confirmPassField.setPreferredSize(new Dimension(0, 40));
        confirmPassField.setBackground(new Color(0xF0F5F2));
        form.add(confirmPassField, fgbc);

        card.add(form, BorderLayout.CENTER);

        // Action Button
        RoundedButton saveBtn = new RoundedButton("Update Security Credentials");
        saveBtn.setBackground(Theme.PRIMARY_TEAL);
        saveBtn.setPreferredSize(new Dimension(0, 45));
        saveBtn.setFont(Theme.FONT_BOLD);
        saveBtn.addActionListener(e -> handlePasswordUpdate());
        card.add(saveBtn, BorderLayout.SOUTH);

        contentArea.add(card);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_PRIMARY);
        l.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        return l;
    }

    private void handlePasswordUpdate() {
        User currentUser = AuthManager.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String current = new String(currentPassField.getPassword());
        String newPass = new String(newPassField.getPassword());
        String confirm = new String(confirmPassField.getPassword());

        if (current.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        if (!newPass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match!");
            return;
        }

        // Verify current password
        if (!backend.utils.SecurityUtils.checkPassword(current, currentUser.getPassword())) {
            JOptionPane.showMessageDialog(this, "Current password incorrect!");
            return;
        }

        if (userDAO.updatePassword(currentUser.getUserId(), newPass)) {
            JOptionPane.showMessageDialog(this, "Password updated successfully!");
            currentPassField.setText("");
            newPassField.setText("");
            confirmPassField.setText("");
            
            // Update the session user object's password as well
            currentUser.setPassword(backend.utils.SecurityUtils.hashPassword(newPass));
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update password.");
        }
    }
}
