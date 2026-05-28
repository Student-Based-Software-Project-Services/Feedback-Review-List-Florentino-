package frames;

import model.Feedback;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import java.awt.*;

public class FeedbackDetailDialog extends JDialog {

    private final Feedback feedback;
    private final User admin; // null if viewed by student
    private JComboBox<String> statusCombo;
    private JTextArea responseArea;

    public FeedbackDetailDialog(Frame parent, Feedback feedback, User admin) {
        super(parent, "Feedback Details", true);
        this.feedback = feedback;
        this.admin = admin;
        initComponents();
    }

    private void initComponents() {
        setSize(600, 540);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_SIDEBAR);
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel titleLbl = new JLabel("Feedback #" + feedback.getId());
        titleLbl.setFont(UITheme.FONT_TITLE);
        titleLbl.setForeground(UITheme.TEXT_PRIMARY);

        JLabel statusBadge = UITheme.statusBadge(feedback.getStatus());

        header.add(titleLbl, BorderLayout.WEST);
        header.add(statusBadge, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // Content
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(UITheme.BG_DARK);
        content.setBorder(BorderFactory.createEmptyBorder(16, 20, 10, 20));

        // Meta row
        JPanel meta = new JPanel(new GridLayout(2, 3, 10, 4));
        meta.setBackground(UITheme.BG_DARK);
        meta.setAlignmentX(Component.LEFT_ALIGNMENT);
        meta.setMaximumSize(new Dimension(520, 60));

        addMetaField(meta, "Student", feedback.getStudentName() != null ? feedback.getStudentName() : "—");
        addMetaField(meta, "Category", feedback.getCategoryDisplay());
        addMetaField(meta, "Rating", UITheme.starsText(feedback.getRating()) + " (" + feedback.getRating() + "/5)");
        addMetaField(meta, "Submitted", feedback.getSubmittedAt() != null ? feedback.getSubmittedAt().toString().substring(0, 16) : "—");
        addMetaField(meta, "Reviewed By", feedback.getReviewerName() != null ? feedback.getReviewerName() : "Not reviewed yet");
        addMetaField(meta, "Reviewed At", feedback.getReviewedAt() != null ? feedback.getReviewedAt().toString().substring(0, 16) : "—");

        content.add(meta);
        content.add(Box.createVerticalStrut(12));

        // Subject
        JLabel subjectLbl = UITheme.label("Subject");
        subjectLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subjectLbl);

        JLabel subjectVal = new JLabel(feedback.getSubject());
        subjectVal.setFont(UITheme.FONT_BOLD);
        subjectVal.setForeground(UITheme.TEXT_PRIMARY);
        subjectVal.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subjectVal);
        content.add(Box.createVerticalStrut(10));

        // Message
        JLabel msgLbl = UITheme.label("Student's Message");
        msgLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(msgLbl);

        JTextArea msgArea = UITheme.styledTextArea();
        msgArea.setText(feedback.getMessage());
        msgArea.setEditable(false);
        msgArea.setBackground(UITheme.BG_CARD);
        JScrollPane msgScroll = UITheme.styledScrollPane(msgArea);
        msgScroll.setPreferredSize(new Dimension(520, 80));
        msgScroll.setMaximumSize(new Dimension(520, 80));
        msgScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(msgScroll);
        content.add(Box.createVerticalStrut(10));

        // Admin response section
        if (admin != null) {
            // Status combo
            JLabel statusLbl = UITheme.label("Change Status");
            statusLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(statusLbl);

            statusCombo = UITheme.styledCombo(new String[]{"PENDING", "REVIEWED", "RESOLVED"});
            statusCombo.setSelectedItem(feedback.getStatus());
            statusCombo.setMaximumSize(new Dimension(200, 30));
            statusCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(statusCombo);
            content.add(Box.createVerticalStrut(8));

            JLabel respLbl = UITheme.label("Admin Response");
            respLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(respLbl);

            responseArea = UITheme.styledTextArea();
            responseArea.setText(feedback.getAdminResponse() != null ? feedback.getAdminResponse() : "");
            JScrollPane respScroll = UITheme.styledScrollPane(responseArea);
            respScroll.setPreferredSize(new Dimension(520, 70));
            respScroll.setMaximumSize(new Dimension(520, 70));
            respScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            content.add(respScroll);

        } else {
            // Student view: show admin response if any
            if (feedback.getAdminResponse() != null && !feedback.getAdminResponse().isEmpty()) {
                JLabel respTitle = UITheme.label("Admin Response");
                respTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
                content.add(respTitle);

                JTextArea respArea = UITheme.styledTextArea();
                respArea.setText(feedback.getAdminResponse());
                respArea.setEditable(false);
                respArea.setBackground(new Color(20, 50, 30));
                JScrollPane rsp = UITheme.styledScrollPane(respArea);
                rsp.setPreferredSize(new Dimension(520, 70));
                rsp.setMaximumSize(new Dimension(520, 70));
                rsp.setAlignmentX(Component.LEFT_ALIGNMENT);
                content.add(rsp);
            }
        }

        JScrollPane contentScroll = new JScrollPane(content);
        contentScroll.setBorder(null);
        contentScroll.setBackground(UITheme.BG_DARK);
        contentScroll.getViewport().setBackground(UITheme.BG_DARK);
        root.add(contentScroll, BorderLayout.CENTER);

        // Bottom buttons
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnBar.setBackground(UITheme.BG_SIDEBAR);
        btnBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UITheme.BORDER_COLOR));

        JButton closeBtn = UITheme.secondaryButton("Close");
        closeBtn.addActionListener(e -> dispose());
        btnBar.add(closeBtn);

        if (admin != null) {
            JButton saveBtn = UITheme.successButton("Save Response");
            saveBtn.addActionListener(e -> saveResponse());
            btnBar.add(saveBtn);
        }

        root.add(btnBar, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void addMetaField(JPanel parent, String label, String value) {
        JPanel field = new JPanel(new GridLayout(2, 1, 0, 2));
        field.setBackground(UITheme.BG_DARK);
        JLabel lbl = UITheme.label(label);
        JLabel val = new JLabel(value);
        val.setFont(UITheme.FONT_BODY);
        val.setForeground(UITheme.TEXT_PRIMARY);
        field.add(lbl);
        field.add(val);
        parent.add(field);
    }

    private void saveResponse() {
        String status   = (String) statusCombo.getSelectedItem();
        String response = responseArea.getText().trim();

        boolean ok = RepoManager.getInstance().getFeedbackRepository()
                .updateStatusAndResponse(feedback.getId(), status, response, admin.getId());

        if (ok) {
            String action = String.format("Changed status of Feedback #%d to [%s]%s",
                    feedback.getId(), status,
                    !response.isEmpty() ? " and added a response" : "");
            RepoManager.getInstance().getActivityLogRepository()
                    .log(admin.getId(), action, feedback.getId());

            JOptionPane.showMessageDialog(this, "Feedback updated successfully.", "Saved", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to save. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
