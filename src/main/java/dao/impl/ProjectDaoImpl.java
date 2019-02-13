package dao.impl;

import dao.abstr.ProjectDao;
import model.Project;
import storage.Storage;

public class ProjectDaoImpl implements ProjectDao{
	public static ProjectDaoImpl instance;
	ProjectDaoImpl getInstance(){
	if (instance == null) {
		instance = new ProjectDaoImpl();
	}
            return instance;
}

	@Override
	public Project getProjectById(long id) {
		return null;
	}

	@Override
	public void addProject(Project project, Integer id) {
		Storage.PROJECTS_TABLE.put(id, project);

	}
}
