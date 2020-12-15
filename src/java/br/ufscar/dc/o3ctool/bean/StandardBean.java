package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.StandardDAO;
import br.ufscar.dc.o3ctool.dao.jpa.StandardJPA;
import br.ufscar.dc.o3ctool.model.Project;
import br.ufscar.dc.o3ctool.model.Standard;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Michael
 */

@ManagedBean(name = "stabean")
@ViewScoped
public class StandardBean implements Serializable {

    private Standard sta;
    private final StandardDAO staDAO;
    private Long standardId;
    
    private String selected;
    private String result;
    private boolean patternCreate;
    private boolean patternRead;
    private boolean patternUpdate;
    private boolean patternDelete;
    private boolean patternStorage;
    private boolean patternSecurity;
    private boolean patternPerformance;
    private boolean patternUsability;

    public StandardBean() {
        sta = Session.getActivSettings().getStandard();
        staDAO = new StandardJPA();
        standardId = null;
        selected = null;
        patternCreate = false;
        patternRead = false;
        patternUpdate = false;
        patternDelete = false;
        patternStorage = false;
        patternSecurity = false;
        patternPerformance = false;
        patternUsability = false;
    }

//    public String saveStandard() {
//        Project activeproject = Session.getActiveProject();
//        if (sta.getId() == null) {
//            if (staDAO.alreadyExistingStandard(activeproject.getId(), sta)) {
//                Messages.addMessage(
//                        FacesMessage.SEVERITY_ERROR,
//                        i18n.getProperty("err_de_sta"),
//                        null);
//                return null;
//            }
//        }
//        sta.setProject(activeproject);
//        staDAO.save(sta);
//        sta = Session.getActivSettings().getStandard();
//        return "req.xhtml?faces-redirect=true";
//    }
    
    public String saveStandard() {
        if (sta.getId() == null) {
            if (staDAO.alreadyExistingStandard(sta)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_sta"),
                        null);
                return null;
            }
        }
        staDAO.save(sta);
        sta = Session.getActivSettings().getStandard();
        return "req.xhtml?faces-redirect=true";
    }
    
    public void clearFilter() {
        this.sta.setName(null);
        this.sta.setDomain(null);
        this.sta.setPurpose(null);
        this.sta.setProblem(null);
        this.sta.setConsequence(null);
        this.sta.setType(Standard.StandardType.NON_FUNCTIONAL);
        this.sta.setTemplate(null);
        this.sta.setRelated_standards(null);
    }
    
    public String removeStandard() {
        try {
            staDAO.remove(sta);
            sta = Session.getActivSettings().getStandard();
            return "req.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_constraint_exception"),
                    null);
            return null;
        }
    }
    
//    public List<Standard> getAllStandards() {
//        return staDAO.getAllStandards(Session.getActiveProject().getId());
//    }
    
    public List<Standard> getAllStandards() {
        return staDAO.getAllStandards();
    }

    public Standard getSta() {
        return sta;
    }

    public void setSta(Standard sta) {
        this.sta = sta;
    }

    public Long getStandardId() {
        return standardId;
    }

    public void setStandardId(Long standardId) {
        this.standardId = standardId;
    }
    
    // Método responsável pela inserção dos padrões de requisitos de Software
    public void listener(AjaxBehaviorEvent event) {
//        if (selected != null) {
//            if (selected.equals("CREATE")) {
//                result = "Permitir a inclusão de informações de <entidade>, contendo os seguintes atributos: <atributos>.";
//                this.sta.setName(result);
//                this.patternCreate = true;
//                this.patternRead = false;
//                this.patternUpdate = false;
//                this.patternDelete = false;
//                this.patternStorage = false;
//                this.patternSecurity = false;
//                this.patternPerformance = false;
//                this.patternUsability = false;                
//            } 
//            
//            if (selected.equals("READ")) {
//                result = "Permitir a recuperação de informação de <entidade> pelo(s) atributo(s) <atributos>, <condição>.";
//                this.req.setDescription(result); 
//                this.patternRead = true;
//                this.patternCreate = false;
//                this.patternUpdate = false;
//                this.patternDelete = false;
//                this.patternStorage = false;
//                this.patternSecurity = false;
//                this.patternPerformance = false;
//                this.patternUsability = false;
//            } 
//            
//            if (selected.equals("UPDATE")) {
//                result = "Permitir a alteração de <entidade> no(s) seguinte(s) atributo(s): <atributos>.";
//                
//                this.req.setDescription(result); 
//                this.patternUpdate = true;
//                this.patternCreate = false;
//                this.patternRead = false;
//                this.patternDelete = false;
//                this.patternStorage = false;
//                this.patternSecurity = false;
//                this.patternPerformance = false;
//                this.patternUsability = false;
//            } 
//            
//            if (selected.equals("DELETE")) {
//                result = "Permitir a exclusão de um registro de <entidade><condição>.";
//                this.req.setDescription(result); 
//                this.patternDelete = true;
//                this.patternCreate = false;
//                this.patternRead = false;
//                this.patternUpdate = false;
//                this.patternStorage = false;
//                this.patternSecurity = false;
//                this.patternPerformance = false;
//                this.patternUsability = false;
//            }
//            
//            if (selected.equals("STORAGE")) {
//                result = "Permitir que a <entidade> seja armazenada em matrizes, banco de dados, arquivos e <condição> para auxiliar em auditorias.";
//                this.req.setDescription(result); 
//                this.patternStorage = true;
//                this.patternDelete = false;
//                this.patternCreate = false;
//                this.patternRead = false;
//                this.patternUpdate = false;                
//                this.patternSecurity = false;
//                this.patternPerformance = false;
//                this.patternUsability = false;
//            }
//            
//            if (selected.equals("SECURITY")) {
//                result = "Permitir acesso <entidade> nas funcionalidades de cadastro apenas quando estiver autenticado no sistema ou quando existir autorização para realizar as tarefas no sistema <condição>.";
//                this.req.setDescription(result); 
//                this.patternSecurity = true;
//                this.patternDelete = false;
//                this.patternCreate = false;
//                this.patternRead = false;
//                this.patternUpdate = false;
//                this.patternStorage = false;
//                this.patternPerformance = false;
//                this.patternUsability = false;                
//              }
//             
//            if (selected.equals("PERFORMACE")) {
//                result = "Permitir acesso as funcionalidades do sistema de software de muitos <entidade> ao mesmo tempo, e o tempo de resposta não deve exceder <condição>.";
//                this.req.setDescription(result);
//                this.patternPerformance = true;
//                this.patternDelete = false;
//                this.patternCreate = false;
//                this.patternRead = false;
//                this.patternUpdate = false;
//                this.patternStorage = false;
//                this.patternSecurity = false;                
//                this.patternUsability = false;
//            }
//            
//            if (selected.equals("USABILITY")) {
//                result = "Permitir que o <entidade> acesse as interfaces do sistema de software de forma fácil, e que tenha <condição> para auxiliar.";
//                this.req.setDescription(result); 
//                this.patternUsability = true;
//                this.patternDelete = false;
//                this.patternCreate = false;
//                this.patternRead = false;
//                this.patternUpdate = false;
//                this.patternStorage = false;
//                this.patternSecurity = false;
//                this.patternPerformance = false;                
//            }
//              
//        } else {
//            result = "";
//            this.req.setDescription(result);
//            this.patternCreate = false;
//            this.patternRead = false;
//            this.patternUpdate = false;
//            this.patternDelete = false;
//            this.patternStorage = false;
//            this.patternSecurity = false;
//            this.patternPerformance = false;
//            this.patternUsability = false;
//        }
    }
    
    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
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
    
//    public void save() {
//        FacesContext.getCurrentInstance().addMessage(null,
//                new FacesMessage("Você modificou o Padrão recomendado, isso pode afetar o desempenho."));
//    }
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
