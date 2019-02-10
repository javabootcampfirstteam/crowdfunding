package model;

import java.time.LocalDateTime;

public class Project {
    private String projectname;
    private String projectdescription;
    private LocalDateTime projectdatecreate;
    private double projectsumm;
    private double projectcurrentsumm;
    private String projectphoto;

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getProjectdescription() {
        return projectdescription;
    }

    public void setProjectdescription(String projectdescription) {
        this.projectdescription = projectdescription;
    }

    public LocalDateTime getProjectdatecreate() {
        return projectdatecreate;
    }

    public void setProjectdatecreate(LocalDateTime projectdatecreate) {
        this.projectdatecreate = projectdatecreate;
    }

    public double getProjectsumm() {
        return projectsumm;
    }

    public void setProjectsumm(double projectsumm) {
        this.projectsumm = projectsumm;
    }

    public double getProjectcurrentsumm() {
        return projectcurrentsumm;
    }

    public void setProjectcurrentsumm(double projectcurrentsumm) {
        this.projectcurrentsumm = projectcurrentsumm;
    }

    public String getProjectphoto() {
        return projectphoto;
    }

    public void setProjectphoto(String projectphoto) {
        this.projectphoto = projectphoto;
    }
}
