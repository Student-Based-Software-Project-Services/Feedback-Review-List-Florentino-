package frames;

import model.ActivityLog;
import model.Feedback;
import model.User;
import repository.RepoManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {

    private final User admin;
    private JTable feedbackTable;
    private DefaultTableModel tableModel;
    private JLabel totalLbl, pendingLbl, reviewedLbl, resolvedLbl;

    // Filters
    private JComboBox<String> filterCategory;
    private JComboBox<String> filterStatus;
    private JComboBox<String> filterRating;
    private JTextField filterDateFrom, filterDateTo;

    // Logs tab
    private JTable logsTable;
    private DefaultTableModel logsModel;

    public AdminDashboardFrame(User admin) {
        this.admin = admin;
        initComponents();
        loadFeedback();
        loadLogs();
    }

    private void initComponents() {
        setTitle("FeedbackHub — Admin Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(UITheme.BG_DARK);

        // Sidebar
        root.add(buildSidebar(), BorderLayout.WEST);

        // Main content
        JPanel main = new JPanel(new BorderLayout(0, 0));
        main.setBackground(UITheme.BG_DARK);
        main.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Stats row
        main.add(buildStatsPanel(), BorderLayout.NORTH);

        // Tabbed pane
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(UITheme.BG_DARK);
        tabs.setForeground(UITheme.TEXT_MUTED);
        tabs.setFont(UITheme.FONT_BOLD);
        tabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override protected void paintTabBackground(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {
                g.setColor(isSelected ? UITheme.ACCENT_BLUE : UITheme.BG_CARD);
                g.fillRect(x, y, w, h);
            }
            @Override protected void paintTabBorder(Graphics g, int tabPlacement,
                    int tabIndex, int x, int y, int w, int h, boolean isSelected) {}
            @Override protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                g.setColor(UITheme.BORDER_COLOR);
                g.drawRect(0, calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight),
                        tabPane.getWidth() - 1, tabPane.getHeight() - calculateTabAreaHeight(tabPlacement, runCount, maxTabHeight) - 1);
            }
        });

        tabs.addTab("  Feedback Review  ", buildFeedbackTab());
        tabs.addTab("  Activity Logs  ", buildLogsTab());

        main.add(tabs, BorderLayout.CENTER);
        root.add(main, BorderLayout.CENTER);
        setContentPane(root);
    }

    // ─── Sidebar ─────────────────────────────────────────────────────────────

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
        sidebar.setPreferredSize(new Dimension(180, 500));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 14, 24, 14));

        JLabel logo = new JLabel("📋 FeedbackHub");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        logo.setForeground(UITheme.TEXT_PRIMARY);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(6));

        JLabel roleTag = new JLabel("  ADMIN  ");
        roleTag.setFont(UITheme.FONT_SMALL);
        roleTag.setForeground(UITheme.BG_DARK);
        roleTag.setBackground(UITheme.ACCENT_RED);
        roleTag.setOpaque(true);
        roleTag.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(roleTag);
        sidebar.add(Box.createVerticalStrut(20));

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(UITheme.BORDER_COLOR);
        sep1.setMaximumSize(new Dimension(150, 1));
        sidebar.add(sep1);
        sidebar.add(Box.createVerticalStrut(16));

        JLabel userLbl = new JLabel("Logged in as:");
        userLbl.setFont(UITheme.FONT_SMALL);
        userLbl.setForeground(UITheme.TEXT_MUTED);
        userLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(userLbl);

        JLabel nameLbl = new JLabel(admin.getFullName());
        nameLbl.setFont(UITheme.FONT_BOLD);
        nameLbl.setForeground(UITheme.TEXT_PRIMARY);
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(nameLbl);

        sidebar.add(Box.createVerticalStrut(20));
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(UITheme.BORDER_COLOR);
        sep2.setMaximumSize(new Dimension(150, 1));
        sidebar.add(sep2);
        sidebar.add(Box.createVerticalStrut(12));

        JLabel tipTitle = new JLabel("Quick Guide:");
        tipTitle.setFont(UITheme.FONT_SMALL);
        tipTitle.setForeground(UITheme.TEXT_MUTED);
        tipTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(tipTitle);
        sidebar.add(Box.createVerticalStrut(4));

        for (String tip : new String[]{"• Double-click to review", "• Use filters to search", "• Logs track all actions"}) {
            JLabel t = new JLabel(tip);
            t.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            t.setForeground(new Color(100, 116, 139));
            t.setAlignmentX(Component.LEFT_ALIGNMENT);
            sidebar.add(t);
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logoutBtn = UITheme.secondaryButton("Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(150, 34));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        sidebar.add(logoutBtn);

        return sidebar;
    }

    // ─── Stats ───────────────────────────────────────────────────────────────

    private JPanel buildStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 8, 0));
        stats.setBackground(UITheme.BG_DARK);
        stats.setPreferredSize(new Dimension(600, 75));
        stats.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        totalLbl    = createStatCard(stats, "Total",    "0", UITheme.ACCENT_BLUE);
        pendingLbl  = createStatCard(stats, "Pending",  "0", UITheme.ACCENT_YELLOW);
        reviewedLbl = createStatCard(stats, "Reviewed", "0", UITheme.ACCENT_BLUE);
        resolvedLbl = createStatCard(stats, "Resolved", "0", UITheme.ACCENT_GREEN);
        return stats;
    }

    private JLabel createStatCard(JPanel parent, String label, String value, Color accent) {
        JPanel card = UITheme.card(new BorderLayout(0, 2));
        JLabel valLbl = new JLabel(value, SwingConstants.CENTER);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        valLbl.setForeground(accent);
        JLabel nameLbl = new JLabel(label, SwingConstants.CENTER);
        nameLbl.setFont(UITheme.FONT_SMALL);
        nameLbl.setForeground(UITheme.TEXT_MUTED);
        card.add(valLbl, BorderLayout.CENTER);
        card.add(nameLbl, BorderLayout.SOUTH);
        parent.add(card);
        return valLbl;
    }

    // ─── Feedback Tab ─────────────────────────────────────────────────────────

    private JPanel buildFeedbackTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 8, 8, 8));

        // Filters
        JPanel filterBar = buildFilterBar();
        panel.add(filterBar, BorderLayout.NORTH);

        // Table
        String[] cols = {"ID", "Student", "Category", "Subject", "Rating", "Status", "Submitted"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        feedbackTable = new JTable(tableModel);
        UITheme.styleTable(feedbackTable);

        feedbackTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        feedbackTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        feedbackTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        feedbackTable.getColumnModel().getColumn(3).setPreferredWidth(160);
        feedbackTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        feedbackTable.getColumnModel().getColumn(5).setPreferredWidth(75);
        feedbackTable.getColumnModel().getColumn(6).setPreferredWidth(120);

        feedbackTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) openSelectedFeedback();
            }
        });

        JScrollPane sp = UITheme.styledScrollPane(feedbackTable);

        JPanel bottomHint = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 2));
        bottomHint.setBackground(UITheme.BG_DARK);
        JLabel hint = new JLabel("Double-click a row to review and respond");
        hint.setFont(UITheme.FONT_SMALL);
        hint.setForeground(UITheme.TEXT_MUTED);
        bottomHint.add(hint);

        panel.add(sp, BorderLayout.CENTER);
        panel.add(bottomHint, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildFilterBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        bar.setBackground(UITheme.BG_DARK);

        JLabel filterLbl = UITheme.heading("Filters:");
        filterLbl.setFont(UITheme.FONT_BODY);
        bar.add(filterLbl);

        filterCategory = UITheme.styledCombo(new String[]{"ALL", "Teacher Rating", "Complaint", "Suggestion", "Service Comment"});
        filterCategory.setPreferredSize(new Dimension(130, 28));
        bar.add(UITheme.label("Category:"));
        bar.add(filterCategory);

        filterStatus = UITheme.styledCombo(new String[]{"ALL", "PENDING", "REVIEWED", "RESOLVED"});
        filterStatus.setPreferredSize(new Dimension(100, 28));
        bar.add(UITheme.label("Status:"));
        bar.add(filterStatus);

        filterRating = UITheme.styledCombo(new String[]{"ALL", "1", "2", "3", "4", "5"});
        filterRating.setPreferredSize(new Dimension(60, 28));
        bar.add(UITheme.label("Rating:"));
        bar.add(filterRating);

        filterDateFrom = UITheme.styledField();
        filterDateFrom.setPreferredSize(new Dimension(95, 28));
        filterDateFrom.setToolTipText("yyyy-MM-dd");
        filterDateTo = UITheme.styledField();
        filterDateTo.setPreferredSize(new Dimension(95, 28));
        filterDateTo.setToolTipText("yyyy-MM-dd");
        bar.add(UITheme.label("From:"));
        bar.add(filterDateFrom);
        bar.add(UITheme.label("To:"));
        bar.add(filterDateTo);

        JButton applyBtn = UITheme.primaryButton("Apply");
        applyBtn.setPreferredSize(new Dimension(70, 28));
        applyBtn.addActionListener(e -> loadFeedback());

        JButton clearBtn = UITheme.secondaryButton("Reset");
        clearBtn.setPreferredSize(new Dimension(70, 28));
        clearBtn.addActionListener(e -> {
            filterCategory.setSelectedIndex(0);
            filterStatus.setSelectedIndex(0);
            filterRating.setSelectedIndex(0);
            filterDateFrom.setText("");
            filterDateTo.setText("");
            loadFeedback();
        });

        bar.add(applyBtn);
        bar.add(clearBtn);
        return bar;
    }

    private void openSelectedFeedback() {
        int row = feedbackTable.getSelectedRow();
        if (row < 0) return;
        int id = (int) tableModel.getValueAt(row, 0);
        Feedback f = RepoManager.getInstance().getFeedbackRepository().findById(id);
        if (f != null) {
            FeedbackDetailDialog dlg = new FeedbackDetailDialog(this, f, admin);
            dlg.setVisible(true);
            loadFeedback();
            loadLogs();
            refreshStats();
        }
    }

    // ─── Logs Tab ─────────────────────────────────────────────────────────────

    private JPanel buildLogsTab() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(UITheme.BG_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 8, 8, 8));

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(UITheme.BG_DARK);
        JLabel lbl = UITheme.heading("Admin Activity Logs");
        lbl.setForeground(UITheme.TEXT_PRIMARY);
        JButton refreshBtn = UITheme.secondaryButton("↺ Refresh");
        refreshBtn.addActionListener(e -> loadLogs());
        topRow.add(lbl, BorderLayout.WEST);
        topRow.add(refreshBtn, BorderLayout.EAST);
        panel.add(topRow, BorderLayout.NORTH);

        String[] cols = {"#", "Admin", "Action", "Feedback ID", "Timestamp"};
        logsModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        logsTable = new JTable(logsModel);
        UITheme.styleTable(logsTable);

        logsTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        logsTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        logsTable.getColumnModel().getColumn(2).setPreferredWidth(280);
        logsTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        logsTable.getColumnModel().getColumn(4).setPreferredWidth(130);

        JScrollPane sp = UITheme.styledScrollPane(logsTable);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    // ─── Data Loaders ─────────────────────────────────────────────────────────

    public void loadFeedback() {
        tableModel.setRowCount(0);

        String catRaw = (String) filterCategory.getSelectedItem();
        String statusRaw = (String) filterStatus.getSelectedItem();
        String ratingRaw = (String) filterRating.getSelectedItem();
        String from = filterDateFrom != null ? filterDateFrom.getText().trim() : "";
        String to   = filterDateTo   != null ? filterDateTo.getText().trim()   : "";

        // Map display to DB values
        String cat = null;
        if (catRaw != null && !catRaw.equals("ALL")) {
            switch (catRaw) {
                case "Teacher Rating":  cat = "TEACHER_RATING"; break;
                case "Complaint":       cat = "COMPLAINT";      break;
                case "Suggestion":      cat = "SUGGESTION";     break;
                case "Service Comment": cat = "SERVICE_COMMENT";break;
                default: cat = catRaw;
            }
        }
        String status = (statusRaw != null && !statusRaw.equals("ALL")) ? statusRaw : null;
        Integer rating = (ratingRaw != null && !ratingRaw.equals("ALL")) ? Integer.parseInt(ratingRaw) : null;

        List<Feedback> list = RepoManager.getInstance().getFeedbackRepository()
                .search(cat, status, from.isEmpty() ? null : from, to.isEmpty() ? null : to, rating);

        for (Feedback f : list) {
            tableModel.addRow(new Object[]{
                f.getId(),
                f.getStudentName(),
                f.getCategoryDisplay(),
                f.getSubject(),
                UITheme.starsText(f.getRating()),
                f.getStatus(),
                f.getSubmittedAt() != null ? f.getSubmittedAt().toString().substring(0, 16) : ""
            });
        }
        refreshStats();
    }

    private void refreshStats() {
        var repo = RepoManager.getInstance().getFeedbackRepository();
        int total    = repo.countAll();
        int pending  = repo.countByStatus("PENDING");
        int reviewed = repo.countByStatus("REVIEWED");
        int resolved = repo.countByStatus("RESOLVED");
        totalLbl.setText(String.valueOf(total));
        pendingLbl.setText(String.valueOf(pending));
        reviewedLbl.setText(String.valueOf(reviewed));
        resolvedLbl.setText(String.valueOf(resolved));
    }

    public void loadLogs() {
        if (logsModel == null) return;
        logsModel.setRowCount(0);
        List<ActivityLog> logs = RepoManager.getInstance().getActivityLogRepository().findAll();
        for (ActivityLog log : logs) {
            logsModel.addRow(new Object[]{
                log.getId(),
                log.getAdminName(),
                log.getAction(),
                log.getTargetFeedbackId() != null ? "#" + log.getTargetFeedbackId() : "—",
                log.getLoggedAt() != null ? log.getLoggedAt().toString().substring(0, 16) : ""
            });
        }
    }
}
