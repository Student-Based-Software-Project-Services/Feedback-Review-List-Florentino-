package frames;

import model.Feedback;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboardFrame extends JFrame {

    private final User currentUser;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JLabel pendingLbl, reviewedLbl, resolvedLbl, totalLbl;

    public StudentDashboardFrame(User user) {
        this.currentUser = user;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("FeedbackHub — Student Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);

        // Sidebar
        JPanel sidebar = buildSidebar();
        root.add(sidebar, BorderLayout.WEST);

        // Main
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(UITheme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.BG_DARK);
        JLabel title = UITheme.heading("My Feedback");
        title.setFont(UITheme.FONT_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        JButton submitBtn = UITheme.primaryButton("+ Submit Feedback");
        submitBtn.addActionListener(e -> openSubmitForm());
        header.add(title, BorderLayout.WEST);
        header.add(submitBtn, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        // Stats
        JPanel stats = buildStatsPanel();
        main.add(stats, BorderLayout.CENTER);

        // Table
        JPanel tablePanel = buildTablePanel();
        main.add(tablePanel, BorderLayout.SOUTH);

        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, UITheme.BG_SIDEBAR, 0, getHeight(), new Color(10, 15, 30));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        sidebar.setPreferredSize(new Dimension(190, 500));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 14, 24, 14));

        JLabel logo = new JLabel("📋 FeedbackHub");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logo.setForeground(UITheme.TEXT_PRIMARY);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(6));

        JLabel roleTag = new JLabel("  STUDENT  ");
        roleTag.setFont(UITheme.FONT_SMALL);
        roleTag.setForeground(UITheme.BG_DARK);
        roleTag.setBackground(UITheme.ACCENT_TEAL);
        roleTag.setOpaque(true);
        roleTag.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(roleTag);
        sidebar.add(Box.createVerticalStrut(20));

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(UITheme.BORDER_COLOR);
        sep1.setMaximumSize(new Dimension(160, 1));
        sidebar.add(sep1);
        sidebar.add(Box.createVerticalStrut(16));

        JLabel userLbl = new JLabel("Logged in as:");
        userLbl.setFont(UITheme.FONT_SMALL);
        userLbl.setForeground(UITheme.TEXT_MUTED);
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(userLbl);

        JLabel nameLbl = new JLabel(currentUser.getFullName());
        nameLbl.setFont(UITheme.FONT_BOLD);
        nameLbl.setForeground(UITheme.TEXT_PRIMARY);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(nameLbl);

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = UITheme.secondaryButton("Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(160, 34));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JPanel buildStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 10, 0));
        stats.setBackground(UITheme.BG_DARK);
        stats.setPreferredSize(new Dimension(580, 80));

        totalLbl    = createStatCard("Total", "0", UITheme.ACCENT_BLUE);
        pendingLbl  = createStatCard("Pending", "0", UITheme.ACCENT_YELLOW);
        reviewedLbl = createStatCard("Reviewed", "0", UITheme.ACCENT_BLUE);
        resolvedLbl = createStatCard("Resolved", "0", UITheme.ACCENT_GREEN);

        stats.add(totalLbl.getParent());
        stats.add(pendingLbl.getParent());
        stats.add(reviewedLbl.getParent());
        stats.add(resolvedLbl.getParent());

        return stats;
    }

    private JLabel createStatCard(String label, String value, Color accent) {
        JPanel card = UITheme.card(new BorderLayout(0, 4));
        JLabel valLbl = new JLabel(value, SwingConstants.CENTER);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        valLbl.setForeground(accent);
        JLabel nameLbl = new JLabel(label, SwingConstants.CENTER);
        nameLbl.setFont(UITheme.FONT_SMALL);
        nameLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(valLbl, BorderLayout.CENTER);
        card.add(nameLbl, BorderLayout.SOUTH);
        return valLbl;
    }

    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UITheme.BG_DARK);

        String[] cols = {"#", "Category", "Subject", "Rating", "Status", "Submitted"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        feedbackTable = new JTable(tableModel);
        UITheme.styleTable(feedbackTable);

        feedbackTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        feedbackTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        feedbackTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        feedbackTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        feedbackTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        feedbackTable.getColumnModel().getColumn(5).setPreferredWidth(130);

        feedbackTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) viewSelectedFeedback();
            }
        });

        JScrollPane sp = UITheme.styledScrollPane(feedbackTable);
        sp.setPreferredSize(new Dimension(580, 240));

        JLabel hint = new JLabel("Double-click a row to view details");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_MUTED);

        panel.add(sp, BorderLayout.CENTER);
        panel.add(hint, BorderLayout.SOUTH);
        return panel;
    }

    private void viewSelectedFeedback() {
        int row = feedbackTable.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        Feedback f = RepoManager.getInstance().getFeedbackRepository().findById(id);
        if (f != null) new FeedbackDetailDialog(this, f, null).setVisible(true);
    }

    private void openSubmitForm() {
        SubmitFeedbackFrame form = new SubmitFeedbackFrame(currentUser, this);
        form.setVisible(true);
    }

    public void loadData() {
        tableModel.setRowCount(0);
        List<Feedback> list = RepoManager.getInstance().getFeedbackRepository()
                .findByStudent(currentUser.getId());

        int pending = 0, reviewed = 0, resolved = 0;
        for (Feedback f : list) {
            tableModel.addRow(new Object[]{
                f.getId(),
                f.getCategoryDisplay(),
                f.getSubject(),
                UITheme.starsText(f.getRating()),
                f.getStatus(),
                f.getSubmittedAt() != null ? f.getSubmittedAt().toString().substring(0, 16) : ""
            });
            switch (f.getStatus()) {
                case "PENDING":  pending++;  break;
                case "REVIEWED": reviewed++; break;
                case "RESOLVED": resolved++; break;
            }
        }
        totalLbl.setText(String.valueOf(list.size()));
        pendingLbl.setText(String.valueOf(pending));
        reviewedLbl.setText(String.valueOf(reviewed));
        resolvedLbl.setText(String.valueOf(resolved));
    }
}
