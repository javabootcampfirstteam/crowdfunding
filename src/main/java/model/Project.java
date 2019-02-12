package model;

import java.time.LocalDateTime;

public class Project {
	private String projectName;
	private String projectDescription;
	private LocalDateTime projectDateCreate;
	private LocalDateTime projectDateEnd;
	private double projectSum;
	private double projectCurrentSum;
	private String projectPhoto;
	private long projectId;
	private String authorName;

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getAuthorSurname() {
		return authorSurname;
	}

	public void setAuthorSurname(String authorSurname) {
		this.authorSurname = authorSurname;
	}

	private String authorSurname;


	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

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

	public LocalDateTime getProjectDateEnd() {
		return projectDateEnd;
	}

	public void setProjectDateEnd(LocalDateTime projectDateEnd) {
		this.projectDateEnd = projectDateEnd;
	}
}
