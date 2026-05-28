package repository;

import model.Feedback;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackRepository {

    private final Connection conn;

    public FeedbackRepository(Connection conn) {
        this.conn = conn;
    }

    public boolean submit(Feedback f) {
        String sql = "INSERT INTO feedback (student_id, category, subject, message, rating, status) VALUES (?, ?, ?, ?, ?, 'PENDING')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, f.getStudentId());
            ps.setString(2, f.getCategory());
            ps.setString(3, f.getSubject());
            ps.setString(4, f.getMessage());
            ps.setInt(5, f.getRating());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] Submit error: " + e.getMessage());
        }
        return false;
    }

    public List<Feedback> findAll() {
        return query("SELECT f.*, u.full_name as student_name, a.full_name as reviewer_name " +
                "FROM feedback f " +
                "JOIN users u ON f.student_id = u.id " +
                "LEFT JOIN users a ON f.reviewed_by = a.id " +
                "ORDER BY f.submitted_at DESC");
    }

    public List<Feedback> findByStudent(int studentId) {
        String sql = "SELECT f.*, u.full_name as student_name, a.full_name as reviewer_name " +
                "FROM feedback f " +
                "JOIN users u ON f.student_id = u.id " +
                "LEFT JOIN users a ON f.reviewed_by = a.id " +
                "WHERE f.student_id = ? ORDER BY f.submitted_at DESC";
        List<Feedback> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] FindByStudent error: " + e.getMessage());
        }
        return list;
    }

    public List<Feedback> search(String category, String status, String dateFrom, String dateTo, Integer rating) {
        StringBuilder sb = new StringBuilder(
                "SELECT f.*, u.full_name as student_name, a.full_name as reviewer_name " +
                "FROM feedback f " +
                "JOIN users u ON f.student_id = u.id " +
                "LEFT JOIN users a ON f.reviewed_by = a.id WHERE 1=1");

        List<Object> params = new ArrayList<>();

        if (category != null && !category.equals("ALL")) {
            sb.append(" AND f.category = ?");
            params.add(category);
        }
        if (status != null && !status.equals("ALL")) {
            sb.append(" AND f.status = ?");
            params.add(status);
        }
        if (dateFrom != null && !dateFrom.isEmpty()) {
            sb.append(" AND DATE(f.submitted_at) >= ?");
            params.add(dateFrom);
        }
        if (dateTo != null && !dateTo.isEmpty()) {
            sb.append(" AND DATE(f.submitted_at) <= ?");
            params.add(dateTo);
        }
        if (rating != null && rating > 0) {
            sb.append(" AND f.rating = ?");
            params.add(rating);
        }
        sb.append(" ORDER BY f.submitted_at DESC");

        List<Feedback> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sb.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] Search error: " + e.getMessage());
        }
        return list;
    }

    public boolean updateStatusAndResponse(int feedbackId, String status, String response, int adminId) {
        String sql = "UPDATE feedback SET status = ?, admin_response = ?, reviewed_by = ?, reviewed_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, response);
            ps.setInt(3, adminId);
            ps.setInt(4, feedbackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] UpdateStatus error: " + e.getMessage());
        }
        return false;
    }

    public Feedback findById(int id) {
        String sql = "SELECT f.*, u.full_name as student_name, a.full_name as reviewer_name " +
                "FROM feedback f JOIN users u ON f.student_id = u.id " +
                "LEFT JOIN users a ON f.reviewed_by = a.id WHERE f.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] FindById error: " + e.getMessage());
        }
        return null;
    }

    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM feedback WHERE status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] CountByStatus error: " + e.getMessage());
        }
        return 0;
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM feedback";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] CountAll error: " + e.getMessage());
        }
        return 0;
    }

    private List<Feedback> query(String sql) {
        List<Feedback> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[FeedbackRepo] Query error: " + e.getMessage());
        }
        return list;
    }

    private Feedback mapRow(ResultSet rs) throws SQLException {
        Feedback f = new Feedback();
        f.setId(rs.getInt("id"));
        f.setStudentId(rs.getInt("student_id"));
        f.setStudentName(rs.getString("student_name"));
        f.setCategory(rs.getString("category"));
        f.setSubject(rs.getString("subject"));
        f.setMessage(rs.getString("message"));
        f.setRating(rs.getInt("rating"));
        f.setStatus(rs.getString("status"));
        f.setAdminResponse(rs.getString("admin_response"));
        int reviewedBy = rs.getInt("reviewed_by");
        if (!rs.wasNull()) f.setReviewedBy(reviewedBy);
        f.setReviewerName(rs.getString("reviewer_name"));
        f.setSubmittedAt(rs.getTimestamp("submitted_at"));
        f.setReviewedAt(rs.getTimestamp("reviewed_at"));
        return f;
    }
}
