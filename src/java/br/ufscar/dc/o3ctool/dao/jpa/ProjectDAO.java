package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Project;
import java.util.List;

public interface ProjectDAO {
    public boolean alreadyExistingProject(Long resid, Project proj);
    public Project getByName(Long resid, String name);
    public void save(Project proj);
    public List<Project> getAllProjects(Long resid);
    public List<Project> searchProjects(String search, int maxResults);
    public void remove(Project proj);
}
