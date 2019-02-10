package model;

import java.time.LocalDateTime;

public class Project {
    private String projectName;
    private String projectDescription;
    private LocalDateTime projectDateCreate;
    private double projectSumm;
    private double projectCurrentSumm;
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

    public double getProjectSumm() {
        return projectSumm;
    }

    public void setProjectSumm(double projectSumm) {
        this.projectSumm = projectSumm;
    }

    public double getProjectCurrentSumm() {
        return projectCurrentSumm;
    }

    public void setProjectCurrentSumm(double projectCurrentSumm) {
        this.projectCurrentSumm = projectCurrentSumm;
    }

    public String getProjectPhoto() {
        return projectPhoto;
    }

    public void setProjectPhoto(String projectPhoto) {
        this.projectPhoto = projectPhoto;
    }
}
