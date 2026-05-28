package repository;

import model.ActivityLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogRepository {

    private final Connection conn;

    public ActivityLogRepository(Connection conn) {
        this.conn = conn;
    }

    public boolean log(int adminId, String action, Integer feedbackId) {
        String sql = "INSERT INTO activity_logs (admin_id, action, target_feedback_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setString(2, action);
            if (feedbackId != null) ps.setInt(3, feedbackId);
            else ps.setNull(3, Types.INTEGER);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ActivityLogRepo] Log error: " + e.getMessage());
        }
        return false;
    }

    public List<ActivityLog> findAll() {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT al.*, u.full_name as admin_name " +
                "FROM activity_logs al JOIN users u ON al.admin_id = u.id " +
                "ORDER BY al.logged_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ActivityLogRepo] FindAll error: " + e.getMessage());
        }
        return list;
    }

    public List<ActivityLog> findByAdmin(int adminId) {
        List<ActivityLog> list = new ArrayList<>();
        String sql = "SELECT al.*, u.full_name as admin_name " +
                "FROM activity_logs al JOIN users u ON al.admin_id = u.id " +
                "WHERE al.admin_id = ? ORDER BY al.logged_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[ActivityLogRepo] FindByAdmin error: " + e.getMessage());
        }
        return list;
    }

    private ActivityLog mapRow(ResultSet rs) throws SQLException {
        ActivityLog log = new ActivityLog();
        log.setId(rs.getInt("id"));
        log.setAdminId(rs.getInt("admin_id"));
        log.setAdminName(rs.getString("admin_name"));
        log.setAction(rs.getString("action"));
        int fid = rs.getInt("target_feedback_id");
        if (!rs.wasNull()) log.setTargetFeedbackId(fid);
        log.setLoggedAt(rs.getTimestamp("logged_at"));
        return log;
    }
}
