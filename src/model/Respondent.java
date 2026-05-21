package model;

import java.sql.Timestamp;

public class Respondent {
    
    private int respondentId;
    private String fullName;
    private int age;
    private String gender;
    private Timestamp submittedAt;

    public int getRespondentId() {
        return respondentId;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setRespondentId(int respondentId) {
        this.respondentId = respondentId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }
}
