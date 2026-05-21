package model;

import java.sql.Timestamp;

public class Admin {

    private int adminId;
    private String username;
    private String password;
    private Timestamp createdAt;

    public int getAdminId() {
        return adminId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
