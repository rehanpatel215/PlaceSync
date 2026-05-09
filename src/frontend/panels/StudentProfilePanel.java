package frontend.panels;

import backend.auth.AuthManager;
import backend.models.Student;
import backend.models.User;
import backend.queries.StudentDAO;
import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class StudentProfilePanel extends BaseDashboardPanel {
    private final StudentDAO studentDAO = new StudentDAO();
    private JTextField nameField, branchField, cgpaField, resumePathField;
    private JButton uploadButton;

    public StudentProfilePanel() {
        super("My Profile", "Student");
        setupUI();
        loadProfileData();
    }

    private void setupUI() {
        contentArea.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(10, 10, 10, 10);
        fgbc.anchor = GridBagConstraints.WEST;

        // Name
        fgbc.gridx = 0; fgbc.gridy = 0;
        formPanel.add(new JLabel("Full Name:"), fgbc);
        nameField = new JTextField(20);
        nameField.setEditable(false);
        fgbc.gridx = 1;
        formPanel.add(nameField, fgbc);

        // Branch
        fgbc.gridx = 0; fgbc.gridy = 1;
        formPanel.add(new JLabel("Branch:"), fgbc);
        branchField = new JTextField(20);
        branchField.setEditable(false);
        fgbc.gridx = 1;
        formPanel.add(branchField, fgbc);

        // CGPA
        fgbc.gridx = 0; fgbc.gridy = 2;
        formPanel.add(new JLabel("Current CGPA:"), fgbc);
        cgpaField = new JTextField(20);
        cgpaField.setEditable(false);
        fgbc.gridx = 1;
        formPanel.add(cgpaField, fgbc);

        // Resume Path
        fgbc.gridx = 0; fgbc.gridy = 3;
        formPanel.add(new JLabel("Resume Path:"), fgbc);
        resumePathField = new JTextField(20);
        resumePathField.setEditable(false);
        fgbc.gridx = 1;
        formPanel.add(resumePathField, fgbc);

        // Upload Button
        uploadButton = new JButton("Update Resume Path");
        uploadButton.setBackground(Theme.TURQUOISE);
        uploadButton.setForeground(Color.WHITE);
        uploadButton.addActionListener(e -> handleResumeUpload());
        fgbc.gridx = 1; fgbc.gridy = 4;
        formPanel.add(uploadButton, fgbc);

        gbc.gridx = 0; gbc.gridy = 0;
        contentArea.add(formPanel, gbc);
    }

    public final void loadProfileData() {
        User currentUser = AuthManager.getInstance().getCurrentUser();
        if (currentUser == null) return;
        Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
        if (student != null) {
            nameField.setText(student.getName());
            branchField.setText(student.getBranch());
            cgpaField.setText(String.valueOf(student.getCgpa()));
            resumePathField.setText(student.getResumePath() != null ? student.getResumePath() : "No resume uploaded");
        }
    }

    private void handleResumeUpload() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            
            User currentUser = AuthManager.getInstance().getCurrentUser();
            Student student = studentDAO.getStudentByUserId(currentUser.getUserId());
            
            if (studentDAO.updateResumePath(student.getStudentId(), path)) {
                resumePathField.setText(path);
                JOptionPane.showMessageDialog(this, "Resume path updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update resume path.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
