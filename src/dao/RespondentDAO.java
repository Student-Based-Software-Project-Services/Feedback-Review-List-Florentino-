package dao;

import model.Respondent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RespondentDAO {

    private final Connection conn;
    
    public RespondentDAO(Connection conn) {
        this.conn = conn;
    }
    
    public int insert(Respondent respondent) {
        String sql = "INSERT INTO respondents (full_name, age, gender) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, respondent.getFullName());
            stmt.setInt(2, respondent.getAge());
            stmt.setString(3, respondent.getGender());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1); // return the new respondent_id
                }
            }

        } catch (SQLException e) {
            System.err.println("RespondentDAO.insert() error: " + e.getMessage());
        }

        return -1; // insert failed
    }

    public List<Respondent> getAll() {
        List<Respondent> list = new ArrayList<>();
        String sql = "SELECT * FROM respondents ORDER BY submitted_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Respondent r = new Respondent();
                r.setRespondentId(rs.getInt("respondent_id"));
                r.setFullName(rs.getString("full_name"));
                r.setAge(rs.getInt("age"));
                r.setGender(rs.getString("gender"));
                r.setSubmittedAt(rs.getTimestamp("submitted_at"));
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("RespondentDAO.getAll() error: " + e.getMessage());
        }

        return list;
    }

    public List<Respondent> searchByName(String name) {
        List<Respondent> list = new ArrayList<>();
        String sql = "SELECT * FROM respondents WHERE full_name LIKE ? ORDER BY submitted_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Respondent r = new Respondent();
                r.setRespondentId(rs.getInt("respondent_id"));
                r.setFullName(rs.getString("full_name"));
                r.setAge(rs.getInt("age"));
                r.setGender(rs.getString("gender"));
                r.setSubmittedAt(rs.getTimestamp("submitted_at"));
                list.add(r);
            }

        } catch (SQLException e) {
            System.err.println("RespondentDAO.searchByName() error: " + e.getMessage());
        }

        return list;
    }
    
    public Respondent findById(int id) {
        String sql = "SELECT * FROM respondents WHERE respondent_id = ?;";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Respondent respondent = new Respondent();
                    respondent.setFullName(rs.getString("full_name"));
                    respondent.setAge(rs.getInt("age"));
                    return respondent;
                }
            }
            
        } catch (SQLException e) {
            System.out.println("RespondentDAO.findById error: " + e.getMessage());
        }
        
        return null;
    }

    public boolean delete(int respondentId) {
        String sql = "DELETE FROM respondents WHERE respondent_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, respondentId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("RespondentDAO.delete() error: " + e.getMessage());
            return false;
        }
    }

    public int getTotalCount() {
        String sql = "SELECT COUNT(*) FROM respondents";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("RespondentDAO.getTotalCount() error: " + e.getMessage());
        }

        return 0;
    }
}
