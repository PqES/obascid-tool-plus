package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.RequirementDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RequirementJPA;
import br.ufscar.dc.o3ctool.dao.jpa.StandardDAO;
import br.ufscar.dc.o3ctool.dao.jpa.StandardJPA;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Requirement;
import br.ufscar.dc.o3ctool.model.Standard;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "reqbean")
@ViewScoped
public class RequirementBean implements Serializable {

    private Requirement req;
    private final RequirementDAO reqDAO;
    private final StandardDAO staDAO;
    private Long requirementId;
    private Standard sta;
    
    private Long standardId;
    private String result;
    private boolean patternCreate;
    private boolean patternRead;
    private boolean patternUpdate;
    private boolean patternDelete;
    private boolean patternStorage;
    private boolean patternSecurity;
    private boolean patternPerformance;
    private boolean patternUsability;

    public RequirementBean() {
        req = Session.getActivSettings().getRequirement();
        reqDAO = new RequirementJPA();
        staDAO = new StandardJPA();
        requirementId = null;
        standardId = null;
        patternCreate = false;
        patternRead = false;
        patternUpdate = false;
        patternDelete = false;
        patternStorage = false;
        patternSecurity = false;
        patternPerformance = false;
        patternUsability = false;
    }

    public String saveRequirement() {
        Project activeproject = Session.getActiveProject();
        save();

        if (req.getId() == null) {
            if (reqDAO.alreadyExistingRequirement(activeproject.getId(), req)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_req"),
                        null);
                return null;
            }
        }
        req.setProject(activeproject);
        reqDAO.save(req);
        req = Session.getActivSettings().getRequirement();
        return "req.xhtml?faces-redirect=true";
    }
    
    public void clearFilter() {
        this.req.setName(null);
        this.setStandardId(null);
        this.req.setType(Requirement.RequirementType.NON_FUNCTIONAL);
        this.req.setDescription(null);
    }

    public String removeRequirement() {
        try {
            reqDAO.remove(req);
            req = Session.getActivSettings().getRequirement();
            return "req.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_constraint_exception"),
                    null);
            return null;
        }
    }

    public List<Requirement> getAllRequirements() {
        return reqDAO.getAllRequirements(Session.getActiveProject().getId());
    }

    public Requirement getReq() {
        return req;
    }

    public void setReq(Requirement req) {
        this.req = req;
    }

    public Long getRequirementId() {
        return requirementId;
    }

    public void setRequirementId(Long requirementId) {
        this.requirementId = requirementId;
    }
    
    // Método responsável pela inserção dos padrões de requisitos de Software
    public void listener(AjaxBehaviorEvent event) {
        if (standardId != null) {
            result = staDAO.getByIdStandard(standardId);
            this.req.setDescription(result);
        }
    }
    
    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
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
    
    public boolean isPatternStorage() {
        return patternStorage;
    }

    public void setPatternStorage(boolean patternStorage) {
        this.patternStorage = patternStorage;
    }
   
    public boolean isPatternSecurity() {
        return patternSecurity;
    }

    public void setPatternSecurity(boolean patternSecurity) {
        this.patternSecurity = patternSecurity;
    } 
       
    public boolean isPatternPerformance() {
        return patternPerformance;
    }

    public void setPatternPerformance(boolean patternPerformance) {
        this.patternPerformance = patternPerformance;
    }
    
    public boolean isPatternUsability() {
        return patternUsability;
    }

    public void setPatternUsability(boolean patternUsability) {
        this.patternUsability = patternUsability;
    }
    
     public Standard getSta() {
        return sta;
    }

    public void setSta(Standard sta) {
        this.sta = sta;
    }
    
    public void save() {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Você modificou o Padrão recomendado, isso pode afetar o desempenho."));
    }
//    
//    public void savePattern() {
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage("Você está salvou o requisito com Padrão recomendado."));
//    }
    
//    public void onCancelRelation(CloseEvent event) {
//		Log.info("Close Dialog");
//		editRelation = null;
//		
//	}
}
