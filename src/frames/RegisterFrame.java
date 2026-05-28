package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JDialog {

    private final JFrame parent;
    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmField;

    public RegisterFrame(JFrame parent) {
        super(parent, "Create Student Account", true);
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        setSize(440, 480);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);
        root.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UITheme.BG_DARK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel title = new JLabel("Create Account");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridy = 0;
        form.add(title, gbc);

        JLabel sub = new JLabel("Register as a student to submit feedback");
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);
        gbc.gridy = 1;
        form.add(sub, gbc);

        gbc.gridy = 2;
        form.add(Box.createVerticalStrut(8), gbc);

        gbc.gridy = 3;
        form.add(UITheme.label("Full Name"), gbc);
        fullNameField = UITheme.styledField();
        gbc.gridy = 4;
        form.add(fullNameField, gbc);

        gbc.gridy = 5;
        form.add(UITheme.label("Username"), gbc);
        usernameField = UITheme.styledField();
        gbc.gridy = 6;
        form.add(usernameField, gbc);

        gbc.gridy = 7;
        form.add(UITheme.label("Password"), gbc);
        passwordField = UITheme.styledPasswordField();
        gbc.gridy = 8;
        form.add(passwordField, gbc);

        gbc.gridy = 9;
        form.add(UITheme.label("Confirm Password"), gbc);
        confirmField = UITheme.styledPasswordField();
        gbc.gridy = 10;
        form.add(confirmField, gbc);

        gbc.gridy = 11;
        form.add(Box.createVerticalStrut(8), gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setBackground(UITheme.BG_DARK);
        JButton cancel = UITheme.secondaryButton("Cancel");
        JButton register = UITheme.primaryButton("Register");
        btnRow.add(cancel);
        btnRow.add(register);
        gbc.gridy = 12;
        form.add(btnRow, gbc);

        root.add(form, BorderLayout.CENTER);
        setContentPane(root);

        cancel.addActionListener(e -> dispose());
        register.addActionListener(e -> doRegister());
    }

    private void doRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm  = new String(confirmField.getPassword());

        if (fullName.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (username.length() < 4) {
            JOptionPane.showMessageDialog(this, "Username must be at least 4 characters.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (RepoManager.getInstance().getUserRepository().usernameExists(username)) {
            JOptionPane.showMessageDialog(this, "Username already taken.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("STUDENT");

        boolean ok = RepoManager.getInstance().getUserRepository().register(user);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Account created! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
