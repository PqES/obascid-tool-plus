package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.ProjectDAO;
import br.ufscar.dc.o3ctool.dao.jpa.ProjectJPA;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Researcher;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;

@ManagedBean(name = "projbean")
@SessionScoped
public class ProjectBean implements Serializable {

    public Project proj;
    private final ProjectDAO projDAO;

    private String selected;
    private String result;
    private boolean patternCreate;
    private boolean patternRead;
    private boolean patternUpdate;
    private boolean patternDelete;
    private boolean patternSecurity;

    public ProjectBean() {
        proj = Session.getActivSettings().getProject();
        projDAO = new ProjectJPA();
    }

    public String backToProjects() {
        proj = Session.getActivSettings().getProject();
        return "projects.xhtml?faces-redirect=true";
    }

    public String changeLicense() {
        if (proj.isLicencePrivate()) {
            proj.setLicense(Project.ProjectLicense.PUBLIC);
        } else {
            proj.setLicense(Project.ProjectLicense.PRIVATE);
        }
        return saveProject();
    }

    public String saveProject() {
        Researcher activeResearcher = Session.getActivResearcher();
        if (proj.getId() == null) {
            if (projDAO.alreadyExistingProject(activeResearcher.getId(), proj)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_project"),
                        null);
                return null;
            }
        }
        proj.setResearcher(activeResearcher);
        projDAO.save(proj);
        proj = Session.getActivSettings().getProject();
        return "projects.xhtml?faces-redirect=true";
    }

    public String removeProject() {
        try {
            projDAO.remove(proj);
            proj = Session.getActivSettings().getProject();
            return "projects.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_constraint_exception"),
                    null);
            return null;
        }
    }

    public void listener(AjaxBehaviorEvent event) {
        if (selected != null) {
            if (selected.equals("CREATE")) {
                result = "Permitir a inclusão de informações de <entidade>, contendo os seguintes atributos: <atributos>.";
                this.proj.setDescription(result);
                this.patternCreate = true;
                this.patternRead = false;
                this.patternUpdate = false;
                this.patternDelete = false;
                this.patternSecurity = false;
            } 
            
            if (selected.equals("READ")) {
                result = "Permitir a recuperação de informação de <entidade> pelo(s) atributo(s) <atributos>, <condição>.";
                this.proj.setDescription(result); 
                this.patternRead = true;
                this.patternCreate = false;
                this.patternUpdate = false;
                this.patternDelete = false;
                this.patternSecurity = false;
            } 
            
            if (selected.equals("UPDATE")) {
                result = "Permitir a alteração de <entidade> no(s) seguinte(s) atributo(s): <atributos>.";
                
                this.proj.setDescription(result); 
                this.patternUpdate = true;
                this.patternCreate = false;
                this.patternRead = false;
                this.patternDelete = false;
                this.patternSecurity = false;
            } 
            
            if (selected.equals("DELETE")) {
                result = "Permitir a exclusão de um registro de <entidade><condição>.";
                this.proj.setDescription(result); 
                this.patternDelete = true;
                this.patternCreate = false;
                this.patternRead = false;
                this.patternUpdate = false;
                this.patternSecurity = false;
            }
            
            if (selected.equals("SECURITY")) {
                result = "Permitir acesso <entidade> nas funcionalidades de cadastro apenas quando estiver autenticado no sistema ou quando existir autorização para realizar as tarefas no sistema <condição>.";
                this.proj.setDescription(result); 
                this.patternSecurity = true;
                this.patternDelete = false;
                this.patternCreate = false;
                this.patternRead = false;
                this.patternUpdate = false;
            }
        } else {
            result = "";
            this.proj.setDescription(result);
            this.patternCreate = false;
            this.patternRead = false;
            this.patternUpdate = false;
            this.patternDelete = false;
            this.patternSecurity = false;
        }
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getResult() {
        return result;
    }

    public List<Project> getAllProjects() {
        return projDAO.getAllProjects(Session.getActivResearcher().getId());
    }

    public Project getProj() {
        return proj;
    }

    public void setProj(Project proj) {
        this.proj = proj;
    }
    
    public boolean isPatternCreate() {
        return patternCreate;
    }

    public void setPatternCreate(boolean patternCreate) {
        this.patternCreate = patternCreate;
    }

    public boolean isPatternRead() {
        return patternRead;
    }

    public void setPatternRead(boolean patternRead) {
        this.patternRead = patternRead;
    }

    public boolean isPatternUpdate() {
        return patternUpdate;
    }

    public void setPatternUpdate(boolean patternUpdate) {
        this.patternUpdate = patternUpdate;
    }

    public boolean isPatternDelete() {
        return patternDelete;
    }

    public void setPatternDelete(boolean patternDelete) {
        this.patternDelete = patternDelete;
    }
    
    public boolean isPatternSecurity() {
        return patternSecurity;
    }

    public void setPatternSecurity(boolean patternSecurity) {
        this.patternSecurity = patternSecurity;
    }    
}
