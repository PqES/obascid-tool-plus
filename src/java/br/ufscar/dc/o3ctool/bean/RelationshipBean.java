package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.RelationshipDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipJPA;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.Relationship;
import br.ufscar.dc.o3ctool.model.Ontology;
import br.ufscar.dc.o3ctool.model.Relationship.RelationshipType;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

@ManagedBean(name = "relatbean")
@ViewScoped
public class RelationshipBean implements Serializable {

    private Relationship relat;
    private final RelationshipDAO relatDAO;

    public RelationshipBean() {
        relat = Session.getActivSettings().getRelationship();
        relatDAO = new RelationshipJPA();        
    }
    
    public boolean isNegativeContribution(RelationshipType type) {
        return (RelationshipType.NEGATIVE_CONTRIBUTION == type);
    }

    public boolean isPositiveContribution(RelationshipType type) {
        return (RelationshipType.POSITIVE_CONTRIBUTION == type);
    }

    public boolean isNull(RelationshipType type) {
        return (RelationshipType.NULL == type);
    }
    
    public String saveRelatioship() {
        Ontology activeOnto = Session.getActiveOntology();
        if (relat.getId() == null) {
            if (relatDAO.alreadyExistingRelationship(activeOnto.getId(), 
                    relat.getSource().getId(), 
                    relat.getTarget().getId(), 
                    relat.getType())) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_relat"),
                        null);
                return null;
            }
        }
        
        if (relat.getSource().getId().compareTo(relat.getTarget().getId()) == 0) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_same_target_and_source"),
                        null);
                return null;            
        }       
        
        relat.setOntology(activeOnto);
        relatDAO.save(relat);
        relat = Session.getActivSettings().getRelationship();
        return "relat.xhtml?faces-redirect=true";
    }

    public String removeRelationship() {
        relatDAO.remove(relat);
        relat = Session.getActivSettings().getRelationship();
        return "relat.xhtml?faces-redirect=true";
    }

    public List<Relationship> getAllRelationships() {
        return relatDAO.getAllRelationships(Session.getActiveOntology().getId());
    }

    public List<Relationship> getAllDependencies(CCC ccc) {
        return relatDAO.getAllRelationshipsBySourceAndType(Session.getActiveOntology().getId(), ccc.getId(), Relationship.RelationshipType.DEPENDENCY);
    }

    public List<Relationship> getAllNegativeContributions(CCC ccc) {
        return relatDAO.getAllRelationshipsBySourceAndType(Session.getActiveOntology().getId(), ccc.getId(), Relationship.RelationshipType.NEGATIVE_CONTRIBUTION);
    }

    public List<Relationship> getAllPositiveContributions(CCC ccc) {
        return relatDAO.getAllRelationshipsBySourceAndType(Session.getActiveOntology().getId(), ccc.getId(), Relationship.RelationshipType.POSITIVE_CONTRIBUTION);
    }

    public Relationship getRelat() {
        return relat;
    }

    public void setRelat(Relationship relat) {
        this.relat = relat;
    }

}
