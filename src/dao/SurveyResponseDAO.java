package dao;

import model.SurveyResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SurveyResponseDAO {
    
    private final Connection conn;
    
    public SurveyResponseDAO(Connection conn) {
        this.conn = conn;
    }

    public boolean insert(SurveyResponse response) {
        String sql = "INSERT INTO survey_responses "
            + "(respondent_id, frequency, preferred_brands, preferred_type, "
            + "purchase_location, satisfaction_rating, health_aware, comments) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, response.getRespondentId());
            stmt.setString(2, response.getFrequency());
            stmt.setString(3, response.getPreferredBrands());   // e.g. "Coca-Cola, Pepsi"
            stmt.setString(4, response.getPreferredType());
            stmt.setString(5, response.getPurchaseLocation());
            stmt.setInt(6, response.getSatisfactionRating());
            stmt.setString(7, response.getHealthAware());       // "Yes" or "No"
            stmt.setString(8, response.getComments());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SurveyResponseDAO.insert() error: " + e.getMessage());
            return false;
        }
    }

    public List<SurveyResponse> getAll() {
        List<SurveyResponse> list = new ArrayList<>();
        String sql = "SELECT sr.*, r.full_name, r.age, r.gender, r.submitted_at "
            + "FROM survey_responses sr "
            + "JOIN respondents r ON sr.respondent_id = r.respondent_id "
            + "ORDER BY r.submitted_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SurveyResponse sr = mapRow(rs);
                list.add(sr);
            }

        } catch (SQLException e) {
            System.err.println("SurveyResponseDAO.getAll() error: " + e.getMessage());
        }
        
        return list;
    }

    public List<SurveyResponse> searchByName(String name) {
        List<SurveyResponse> list = new ArrayList<>();
        String sql = "SELECT sr.*, r.full_name, r.age, r.gender, r.submitted_at "
            + "FROM survey_responses sr "
            + "JOIN respondents r ON sr.respondent_id = r.respondent_id "
            + "WHERE r.full_name LIKE ? "
            + "ORDER BY r.submitted_at DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("SurveyResponseDAO.searchByName() error: " + e.getMessage());
        }

        return list;
    }

    public double getAverageRating() {
        String sql = "SELECT AVG(satisfaction_rating) FROM survey_responses";

        try (PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return Math.round(rs.getDouble(1) * 10.0) / 10.0;
            }

        } catch (SQLException e) {
            System.err.println("SurveyResponseDAO.getAverageRating() error: " + e.getMessage());
        }

        return 0.0;
    }

    public String getTopBrand() {
        List<SurveyResponse> all = getAll();

        java.util.Map<String, Integer> brandCount = new java.util.HashMap<>();

        for (SurveyResponse sr : all) {
            if (sr.getPreferredBrands() != null) {
                String[] brands = sr.getPreferredBrands().split(",");
                for (String brand : brands) {
                    String trimmed = brand.trim();
                    brandCount.put(trimmed, brandCount.getOrDefault(trimmed, 0) + 1);
                }
            }
        }

        return brandCount.entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(java.util.Map.Entry::getKey)
            .orElse("N/A");
    }

    public boolean delete(int responseId) {
        String sql = "DELETE FROM survey_responses WHERE response_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, responseId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("SurveyResponseDAO.delete() error: " + e.getMessage());
            return false;
        }
    }

    private SurveyResponse mapRow(ResultSet rs) throws SQLException {
        SurveyResponse sr = new SurveyResponse();
        sr.setResponseId(rs.getInt("response_id"));
        sr.setRespondentId(rs.getInt("respondent_id"));
        sr.setFullName(rs.getString("full_name"));
        sr.setAge(rs.getInt("age"));
        sr.setGender(rs.getString("gender"));
        sr.setSubmittedAt(rs.getTimestamp("submitted_at"));
        sr.setFrequency(rs.getString("frequency"));
        sr.setPreferredBrands(rs.getString("preferred_brands"));
        sr.setPreferredType(rs.getString("preferred_type"));
        sr.setPurchaseLocation(rs.getString("purchase_location"));
        sr.setSatisfactionRating(rs.getInt("satisfaction_rating"));
        sr.setHealthAware(rs.getString("health_aware"));
        sr.setComments(rs.getString("comments"));
        return sr;
    }
}
