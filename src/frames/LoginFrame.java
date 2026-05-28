package frames;

import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Feedback Review System — Login");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.BG_DARK);

        // Left panel — branding
        JPanel left = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UITheme.BG_SIDEBAR, 0, getHeight(), new Color(10, 15, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // decorative circles
                g2.setColor(new Color(59, 130, 246, 30));
                g2.fillOval(-40, -40, 200, 200);
                g2.setColor(new Color(20, 184, 166, 20));
                g2.fillOval(80, 280, 160, 160);
            }
        };
        left.setPreferredSize(new Dimension(320, 500));
        left.setLayout(new GridBagLayout());

        JPanel brandPanel = new JPanel();
        brandPanel.setOpaque(false);
        brandPanel.setLayout(new BoxLayout(brandPanel, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel("📋");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("FeedbackHub");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("School Feedback System");
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel("<html><center>Collect · Review · Improve</center></html>");
        tagline.setFont(UITheme.FONT_SMALL);
        tagline.setForeground(UITheme.ACCENT_TEAL);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandPanel.add(icon);
        brandPanel.add(Box.createVerticalStrut(10));
        brandPanel.add(title);
        brandPanel.add(Box.createVerticalStrut(4));
        brandPanel.add(sub);
        brandPanel.add(Box.createVerticalStrut(12));
        brandPanel.add(tagline);

        left.add(brandPanel);

        // Right panel — login form
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(UITheme.BG_DARK);
        right.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(UITheme.FONT_TITLE);
        loginTitle.setForeground(UITheme.TEXT_PRIMARY);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        right.add(loginTitle, gbc);

        JLabel loginSub = new JLabel("Enter your credentials to continue");
        loginSub.setFont(UITheme.FONT_SMALL);
        loginSub.setForeground(UITheme.TEXT_MUTED);
        gbc.gridy = 1;
        right.add(loginSub, gbc);

        gbc.gridy = 2;
        right.add(Box.createVerticalStrut(10), gbc);

        gbc.gridy = 3;
        right.add(UITheme.label("Username"), gbc);

        usernameField = UITheme.styledField();
        usernameField.setPreferredSize(new Dimension(300, 36));
        gbc.gridy = 4;
        right.add(usernameField, gbc);

        gbc.gridy = 5;
        right.add(UITheme.label("Password"), gbc);

        passwordField = UITheme.styledPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 36));
        gbc.gridy = 6;
        right.add(passwordField, gbc);

        gbc.gridy = 7;
        right.add(Box.createVerticalStrut(6), gbc);

        loginButton = UITheme.primaryButton("Sign In");
        loginButton.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 8;
        right.add(loginButton, gbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(UITheme.BORDER_COLOR);
        gbc.gridy = 9;
        right.add(sep, gbc);

        JPanel regRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
        regRow.setBackground(UITheme.BG_DARK);
        JLabel regLbl = new JLabel("New student?");
        regLbl.setFont(UITheme.FONT_SMALL);
        regLbl.setForeground(UITheme.TEXT_MUTED);
        registerButton = new JButton("Create Account");
        registerButton.setFont(UITheme.FONT_SMALL);
        registerButton.setForeground(UITheme.ACCENT_BLUE);
        registerButton.setBackground(UITheme.BG_DARK);
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        regRow.add(regLbl);
        regRow.add(registerButton);
        gbc.gridy = 10;
        right.add(regRow, gbc);

        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);
        setContentPane(root);

        // Events
        loginButton.addActionListener(e -> doLogin());
        passwordField.addActionListener(e -> doLogin());
        registerButton.addActionListener(e -> openRegister());
    }

    private void doLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password.");
            return;
        }

        User user = RepoManager.getInstance().getUserRepository()
                .findByUsernameAndPassword(username, password);

        if (user == null) {
            showError("Invalid username or password.");
            passwordField.setText("");
            return;
        }

        dispose();
        if ("ADMIN".equals(user.getRole())) {
            new AdminDashboardFrame(user).setVisible(true);
        } else {
            new StudentDashboardFrame(user).setVisible(true);
        }
    }

    private void openRegister() {
        new RegisterFrame(this).setVisible(true);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Login Error", JOptionPane.ERROR_MESSAGE);
    }
}
