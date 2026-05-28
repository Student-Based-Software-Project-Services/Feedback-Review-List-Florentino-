package frames;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class UITheme {

    // Colors
    public static final Color BG_DARK       = new Color(15, 23, 42);
    public static final Color BG_CARD       = new Color(30, 41, 59);
    public static final Color BG_SIDEBAR    = new Color(20, 30, 50);
    public static final Color ACCENT_BLUE   = new Color(59, 130, 246);
    public static final Color ACCENT_TEAL   = new Color(20, 184, 166);
    public static final Color ACCENT_GREEN  = new Color(34, 197, 94);
    public static final Color ACCENT_YELLOW = new Color(250, 204, 21);
    public static final Color ACCENT_RED    = new Color(239, 68, 68);
    public static final Color TEXT_PRIMARY  = new Color(241, 245, 249);
    public static final Color TEXT_MUTED    = new Color(148, 163, 184);
    public static final Color BORDER_COLOR  = new Color(51, 65, 85);
    public static final Color ROW_EVEN      = new Color(30, 41, 59);
    public static final Color ROW_ODD       = new Color(22, 33, 52);
    public static final Color ROW_HOVER     = new Color(44, 55, 75);

    // Status colors
    public static Color statusColor(String status) {
        if (status == null) return TEXT_MUTED;
        switch (status.toUpperCase()) {
            case "PENDING":  return ACCENT_YELLOW;
            case "REVIEWED": return ACCENT_BLUE;
            case "RESOLVED": return ACCENT_GREEN;
            default: return TEXT_MUTED;
        }
    }

    // Fonts
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY    = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BOLD    = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_MONO    = new Font("Consolas", Font.PLAIN, 12);

    public static JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_BLUE);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(ACCENT_BLUE);
            }
        });
        return btn;
    }

    public static JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(BG_CARD);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(7, 17, 7, 17)
        ));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(44, 55, 75));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BG_CARD);
            }
        });
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_RED);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    public static JButton successButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(ACCENT_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        return btn;
    }

    public static JTextField styledField() {
        JTextField tf = new JTextField();
        tf.setBackground(BG_DARK);
        tf.setForeground(TEXT_PRIMARY);
        tf.setFont(FONT_BODY);
        tf.setCaretColor(ACCENT_BLUE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return tf;
    }

    public static JPasswordField styledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(BG_DARK);
        pf.setForeground(TEXT_PRIMARY);
        pf.setFont(FONT_BODY);
        pf.setCaretColor(ACCENT_BLUE);
        pf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return pf;
    }

    public static JTextArea styledTextArea() {
        JTextArea ta = new JTextArea();
        ta.setBackground(BG_DARK);
        ta.setForeground(TEXT_PRIMARY);
        ta.setFont(FONT_BODY);
        ta.setCaretColor(ACCENT_BLUE);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        return ta;
    }

    public static JScrollPane styledScrollPane(Component view) {
        JScrollPane sp = new JScrollPane(view);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        sp.setBackground(BG_DARK);
        sp.getViewport().setBackground(BG_DARK);
        sp.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = BORDER_COLOR;
                trackColor = BG_DARK;
            }
        });
        return sp;
    }

    public static JLabel label(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_MUTED);
        lbl.setFont(FONT_SMALL);
        return lbl;
    }

    public static JLabel heading(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setFont(FONT_HEADING);
        return lbl;
    }

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(BG_DARK);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_BODY);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_BLUE : BG_DARK);
                setForeground(TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
        return cb;
    }

    public static void styleTable(JTable table) {
        table.setBackground(ROW_EVEN);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(ACCENT_BLUE);
        table.setSelectionForeground(Color.WHITE);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(BG_SIDEBAR);
        header.setForeground(TEXT_MUTED);
        header.setFont(FONT_BOLD);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COLOR));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? ROW_EVEN : ROW_ODD);
                    setForeground(TEXT_PRIMARY);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return this;
            }
        });
    }

    public static JPanel card(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));
        return panel;
    }

    public static JLabel statusBadge(String status) {
        JLabel lbl = new JLabel("  " + status + "  ");
        lbl.setFont(FONT_SMALL);
        lbl.setForeground(BG_DARK);
        lbl.setBackground(statusColor(status));
        lbl.setOpaque(true);
        lbl.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        return lbl;
    }

    public static String starsText(int rating) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) sb.append(i < rating ? "★" : "☆");
        return sb.toString();
    }

    public static void applyGlobalDefaults() {
        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("OptionPane.background", BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background", ACCENT_BLUE);
        UIManager.put("Button.foreground", Color.WHITE);
    }
}
