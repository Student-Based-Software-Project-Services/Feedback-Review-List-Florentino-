package model;

import java.sql.Timestamp;

public class ActivityLog {

    private int id;
    private int adminId;
    private String adminName;
    private String action;
    private Integer targetFeedbackId;
    private Timestamp loggedAt;

    public ActivityLog() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getAdminName() { return adminName; }
    public void setAdminName(String adminName) { this.adminName = adminName; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Integer getTargetFeedbackId() { return targetFeedbackId; }
    public void setTargetFeedbackId(Integer targetFeedbackId) { this.targetFeedbackId = targetFeedbackId; }

    public Timestamp getLoggedAt() { return loggedAt; }
    public void setLoggedAt(Timestamp loggedAt) { this.loggedAt = loggedAt; }
}
