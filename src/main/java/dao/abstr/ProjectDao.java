package dao.abstr;

import model.Project;

public interface ProjectDao {

	Project getProjectById(long id);
	void addProject(Project project, Integer id);
}
