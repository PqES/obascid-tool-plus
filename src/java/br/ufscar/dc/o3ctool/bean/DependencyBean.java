package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.DependencyDAO;
import br.ufscar.dc.o3ctool.dao.jpa.DependencyJPA;
import br.ufscar.dc.o3ctool.model.Dependency;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Requirement;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "depbean")
@ViewScoped
public class DependencyBean implements Serializable {

    private Dependency dep;
    private final DependencyDAO depDAO;

    public DependencyBean() {
        dep = Session.getActivSettings().getDependency();
        depDAO = new DependencyJPA();        
    }
    
    public String saveDependency() {
        Project activeProj = Session.getActiveProject();
        if (dep.getId() == null) {
            if (depDAO.alreadyExistingDependency(activeProj.getId(), 
                    dep.getSource().getId(), 
                    dep.getTarget().getId())) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_dependency"),
                        null);
                return null;
            }
        }
        
        if (dep.getSource().getId().compareTo(dep.getTarget().getId()) == 0) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_dependency_with_same_target_and_source"),
                        null);
                return null;            
        }       
        
        dep.setProject(activeProj);
        depDAO.save(dep);
        dep = Session.getActivSettings().getDependency();
        return "dependency.xhtml?faces-redirect=true";
    }

    public String removeDependency() {        
        depDAO.remove(dep);
        dep = Session.getActivSettings().getDependency();
        return "dependency.xhtml?faces-redirect=true";
    }

    public List<Dependency> getBySource(Requirement req) {
        return depDAO.getBySource(Session.getActiveProject().getId(), req.getId());
    }
        
    public List<Dependency> getAllDependencies() {
        return depDAO.getAllDependencies(Session.getActiveProject().getId());
    }

    public Dependency getDep() {
        return dep;
    }

    public void setDep(Dependency dep) {
        this.dep = dep;
    }
}
