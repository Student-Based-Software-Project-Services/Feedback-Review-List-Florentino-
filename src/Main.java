import database.DBConnection;
import frames.LoginFrame;
import frames.UITheme;
import repository.RepoManager;
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // Apply FlatLaf or fallback to Nimbus
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Flatlaf Dark".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Keep system default
        }

        UITheme.applyGlobalDefaults();

        // Verify DB connection early
        if (DBConnection.getConnection() == null) {
            JOptionPane.showMessageDialog(null,
                "Cannot connect to the database.\n" +
                "Please ensure MySQL is running and the database 'feedback_review_system' exists.\n\n" +
                "Host: localhost:3306\nUser: root / Password: root",
                "Database Connection Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Warm up the singleton
        RepoManager.getInstance();

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
        });

        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(DBConnection::closeConnection));
    }
}
