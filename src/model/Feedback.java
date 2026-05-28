package model;

import java.sql.Timestamp;

public class Feedback {

    private int id;
    private int studentId;
    private String studentName;
    private String category;
    private String subject;
    private String message;
    private int rating;
    private String status;
    private String adminResponse;
    private Integer reviewedBy;
    private String reviewerName;
    private Timestamp submittedAt;
    private Timestamp reviewedAt;

    public Feedback() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public Integer getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Integer reviewedBy) { this.reviewedBy = reviewedBy; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public Timestamp getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(Timestamp submittedAt) { this.submittedAt = submittedAt; }

    public Timestamp getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(Timestamp reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getCategoryDisplay() {
        if (category == null) return "";
        switch (category) {
            case "TEACHER_RATING": return "Teacher Rating";
            case "COMPLAINT": return "Complaint";
            case "SUGGESTION": return "Suggestion";
            case "SERVICE_COMMENT": return "Service Comment";
            default: return category;
        }
    }
}
