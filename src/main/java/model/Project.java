package model;

import java.time.LocalDateTime;

public class Project {
    private String projectName;
    private String projectDescription;
    private LocalDateTime projectDateCreate;
    private double projectSum;
    private double projectCurrentSum;
    private String projectPhoto;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDateTime getProjectDateCreate() {
        return projectDateCreate;
    }

    public void setProjectDateCreate(LocalDateTime projectDateCreate) {
        this.projectDateCreate = projectDateCreate;
    }

    public double getProjectSum() {
        return projectSum;
    }

    public void setProjectSum(double projectSumm) {
        this.projectSum = projectSumm;
    }

    public double getProjectCurrentSum() {
        return projectCurrentSum;
    }

    public void setProjectCurrentSum(double projectCurrentSumm) {
        this.projectCurrentSum = projectCurrentSumm;
    }

    public String getProjectPhoto() {
        return projectPhoto;
    }

    public void setProjectPhoto(String projectPhoto) {
        this.projectPhoto = projectPhoto;
    }
}
