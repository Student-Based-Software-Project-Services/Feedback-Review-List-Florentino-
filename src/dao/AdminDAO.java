package dao;

import model.Admin;

import java.sql.*;
import java.time.LocalDateTime;

public class AdminDAO {

    private final Connection conn;

    public AdminDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password_hash = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // replace with hashed comparison if needed

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // returns true if a matching row exists

        } catch (SQLException e) {
            System.err.println("AdminDAO.login() error: " + e.getMessage());
            return false;
        }
    }

    public boolean register(String username, String password) {
        String sql = "INSERT INTO admins(username, password_hash, created_at) VALUES (?, ?, ?);";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("AdminDAO.register() error: " + e.getMessage());
            return false;
        }

        return true;
    }

    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admins WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Admin admin = new Admin();
                admin.setAdminId(rs.getInt("admin_id"));
                admin.setUsername(rs.getString("username"));
                return admin;
            }

        } catch (SQLException e) {
            System.err.println("AdminDAO.getAdminByUsername() error: " + e.getMessage());
        }

        return null;
    }

    public boolean changePassword(String username, String newPassword) {
        String sql = "UPDATE admins SET password_hash = ? WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("AdminDAO.changePassword() error: " + e.getMessage());
            return false;
        }
    }
}
