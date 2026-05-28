package frames;

import model.Feedback;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class SubmitFeedbackFrame extends JFrame {

    private final User student;
    private final StudentDashboardFrame parent;

    private JComboBox<String> categoryCombo;
    private JTextField subjectField;
    private JTextArea messageArea;
    private JComboBox<String> ratingCombo;
    private ButtonGroup ratingGroup;
    private JRadioButton[] ratingBtns;

    public SubmitFeedbackFrame(User student, StudentDashboardFrame parent) {
        this.student = student;
        this.parent = parent;
        initComponents();
    }

    private void initComponents() {
        setTitle("Submit Feedback");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);

        // Header bar
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(UITheme.BG_SIDEBAR);
        headerBar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("📝  Submit New Feedback");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        JLabel sub = new JLabel("Share your experience with us");
        sub.setFont(UITheme.FONT_SMALL);
        sub.setForeground(UITheme.TEXT_MUTED);
        JPanel hInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        hInfo.setOpaque(false);
        hInfo.add(title);
        hInfo.add(sub);
        headerBar.add(hInfo, BorderLayout.WEST);
        root.add(headerBar, BorderLayout.NORTH);

        // Form area
        JPanel formWrapper = new JPanel(new GridBagLayout());
        formWrapper.setBackground(UITheme.BG_DARK);
        formWrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Left column
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.45;
        formWrapper.add(UITheme.label("Feedback Category *"), gbc);

        String[] categories = {"TEACHER_RATING", "COMPLAINT", "SUGGESTION", "SERVICE_COMMENT"};
        String[] catDisplay  = {"Teacher Rating", "Complaint", "Suggestion", "Service Comment"};
        categoryCombo = UITheme.styledCombo(catDisplay);
        gbc.gridy = 1;
        formWrapper.add(categoryCombo, gbc);

        gbc.gridy = 2;
        formWrapper.add(UITheme.label("Subject / Title *"), gbc);

        subjectField = UITheme.styledField();
        subjectField.setToolTipText("Brief subject of your feedback");
        gbc.gridy = 3;
        formWrapper.add(subjectField, gbc);

        gbc.gridy = 4;
        formWrapper.add(UITheme.label("Rating (1–5 stars) *"), gbc);

        // Star rating buttons
        JPanel starPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        starPanel.setBackground(UITheme.BG_DARK);
        ratingGroup = new ButtonGroup();
        ratingBtns = new JRadioButton[5];
        String[] stars = {"★ 1", "★★ 2", "★★★ 3", "★★★★ 4", "★★★★★ 5"};
        for (int i = 0; i < 5; i++) {
            ratingBtns[i] = new JRadioButton(stars[i]);
            ratingBtns[i].setFont(UITheme.FONT_SMALL);
            ratingBtns[i].setForeground(UITheme.ACCENT_YELLOW);
            ratingBtns[i].setBackground(UITheme.BG_DARK);
            ratingBtns[i].setFocusPainted(false);
            ratingGroup.add(ratingBtns[i]);
            starPanel.add(ratingBtns[i]);
        }
        ratingBtns[2].setSelected(true); // default 3
        gbc.gridy = 5;
        formWrapper.add(starPanel, gbc);

        // Right column — message
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.55;
        formWrapper.add(UITheme.label("Your Message / Feedback *"), gbc);

        messageArea = UITheme.styledTextArea();
        JScrollPane msgScroll = UITheme.styledScrollPane(messageArea);
        msgScroll.setPreferredSize(new Dimension(340, 220));
        gbc.gridy = 1; gbc.gridheight = 5;
        formWrapper.add(msgScroll, gbc);

        root.add(formWrapper, BorderLayout.CENTER);

        // Bottom buttons
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        btnBar.setBackground(UITheme.BG_SIDEBAR);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        JButton clearBtn = UITheme.secondaryButton("Clear");
        JButton submitBtn = UITheme.primaryButton("Submit Feedback");

        clearBtn.addActionListener(e -> clearForm());
        submitBtn.addActionListener(e -> doSubmit(categories));

        btnBar.add(clearBtn);
        btnBar.add(submitBtn);
        root.add(btnBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void clearForm() {
        subjectField.setText("");
        messageArea.setText("");
        ratingBtns[2].setSelected(true);
        categoryCombo.setSelectedIndex(0);
    }

    private void doSubmit(String[] categoryKeys) {
        String subject = subjectField.getText().trim();
        String message = messageArea.getText().trim();
        int catIndex   = categoryCombo.getSelectedIndex();

        if (subject.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Subject and message are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int rating = 3;
        for (int i = 0; i < 5; i++) {
            if (ratingBtns[i].isSelected()) { rating = i + 1; break; }
        }

        Feedback f = new Feedback();
        f.setStudentId(student.getId());
        f.setCategory(categoryKeys[catIndex]);
        f.setSubject(subject);
        f.setMessage(message);
        f.setRating(rating);

        boolean ok = RepoManager.getInstance().getFeedbackRepository().submit(f);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                "Your feedback has been submitted successfully!\nThank you for helping us improve.",
                "Submitted", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            if (parent != null) parent.loadData();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to submit feedback. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
