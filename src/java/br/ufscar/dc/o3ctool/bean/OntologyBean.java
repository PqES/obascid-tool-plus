package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.CCCDAO;
import br.ufscar.dc.o3ctool.dao.jpa.CCCJPA;
import br.ufscar.dc.o3ctool.dao.jpa.OntologyDAO;
import br.ufscar.dc.o3ctool.dao.jpa.OntologyJPA;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipJPA;
import br.ufscar.dc.o3ctool.model.Ontology;
import br.ufscar.dc.o3ctool.model.Researcher;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "ontobean")
@SessionScoped
public class OntologyBean implements Serializable {

    private Ontology onto;
    private final OntologyDAO ontoDAO;
    private final CCCDAO cccDAO;
    private final RelationshipDAO relatDAO;
    private Long mergeOntology1Id;
    private Long mergeOntology2Id;

    public OntologyBean() {
        onto = Session.getActivSettings().getOntology();
        mergeOntology1Id = null;
        mergeOntology2Id = null;
        ontoDAO = new OntologyJPA();
        cccDAO = new CCCJPA();
        relatDAO = new RelationshipJPA();
    }

    public String backToOntologies() {
        onto = Session.getActivSettings().getOntology();
        return "ontologies.xhtml?faces-redirect=true";
    }

    public String changeLicense() {
        if (onto.isLicencePrivate()) {
            onto.setLicense(Ontology.OntologyLicense.PUBLIC);
        } else {
            onto.setLicense(Ontology.OntologyLicense.PRIVATE);
        }
        return saveOntology();
    }

    public String saveOntology() {
        Researcher activeResearcher = Session.getActivResearcher();
        if (onto.getId() == null) {
            if (ontoDAO.alreadyExistingOntology(activeResearcher.getId(), onto)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_ontology"),
                        null);
                return null;
            }
        }
        onto.setResearcher(activeResearcher);
        ontoDAO.save(onto);
        onto = Session.getActivSettings().getOntology();
        return "ontologies.xhtml?faces-redirect=true";
    }

    public String removeOntology() {
        try {
            ontoDAO.remove(onto);
            onto = Session.getActivSettings().getOntology();
            return "ontologies.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_constraint_exception"),
                    null);
            return null;
        }
    }

    public List<Ontology> getAllOntologiesByResearcher() {
        return ontoDAO.getAllOntologies(Session.getActivResearcher().getId());
    }

    public Ontology getOnto() {
        return onto;
    }

    public void setOnto(Ontology onto) {
        this.onto = onto;
    }

    public Long getMergeOntology1Id() {
        return mergeOntology1Id;
    }

    public void setMergeOntology1Id(Long mergeOntology1Id) {
        this.mergeOntology1Id = mergeOntology1Id;
    }

    public Long getMergeOntology2Id() {
        return mergeOntology2Id;
    }

    public void setMergeOntology2Id(Long mergeOntology2Id) {
        this.mergeOntology2Id = mergeOntology2Id;
    }

}
