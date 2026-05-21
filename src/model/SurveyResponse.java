package model;

import java.sql.*;

public class SurveyResponse {
    
    private int responseId;
    private int respondentId;
    private String fullName;
    private int age;
    private String gender;
    private Timestamp submittedAt;
    private String frequency;
    private String preferredBrands;
    private String preferredType;
    private String purchaseLocation;
    private int satisfactionRating;
    private String healthAware;
    private String comments;

    public int getResponseId() {
        return responseId;
    }

    public int getRespondentId() {
        return respondentId;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getPreferredBrands() {
        return preferredBrands;
    }

    public String getPreferredType() {
        return preferredType;
    }

    public String getPurchaseLocation() {
        return purchaseLocation;
    }

    public int getSatisfactionRating() {
        return satisfactionRating;
    }

    public String getHealthAware() {
        return healthAware;
    }

    public String getComments() {
        return comments;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public void setRespondentId(int respondentId) {
        this.respondentId = respondentId;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setPreferredBrands(String preferredBrands) {
        this.preferredBrands = preferredBrands;
    }

    public void setPreferredType(String preferredType) {
        this.preferredType = preferredType;
    }

    public void setPurchaseLocation(String purchaseLocation) {
        this.purchaseLocation = purchaseLocation;
    }

    public void setSatisfactionRating(int satisfactionRating) {
        this.satisfactionRating = satisfactionRating;
    }

    public void setHealthAware(String healthAware) {
        this.healthAware = healthAware;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }
}
