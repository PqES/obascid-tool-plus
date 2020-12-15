package br.ufscar.dc.o3ctool.bean;

import br.ufscar.dc.o3ctool.dao.jpa.CCCDAO;
import br.ufscar.dc.o3ctool.dao.jpa.CCCJPA;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipDAO;
import br.ufscar.dc.o3ctool.dao.jpa.RelationshipJPA;
import br.ufscar.dc.o3ctool.model.CCC;
import br.ufscar.dc.o3ctool.model.Ontology;
import br.ufscar.dc.o3ctool.model.Relationship.RelationshipType;
import br.ufscar.dc.o3ctool.util.Messages;
import br.ufscar.dc.o3ctool.util.Session;
import br.ufscar.dc.o3ctool.util.i18n;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "cccbean")
@SessionScoped
public class CCCBean implements Serializable {

    private CCC ccc;
    private final CCCDAO cccDAO;
    private final RelationshipDAO relatDAO;
    private final List<List<RelationshipType>> contributionMatrix;
    private final List<List<Boolean>> dependencyMatrix;
    private boolean areThereContributionRelationships;
    private boolean areThereDependencyRelationships;

    public CCCBean() {
        ccc = Session.getActivSettings().getCCC();
        cccDAO = new CCCJPA();
        relatDAO = new RelationshipJPA();
        contributionMatrix = new ArrayList<List<RelationshipType>>();
        dependencyMatrix = new ArrayList<List<Boolean>>();
        areThereDependencyRelationships = false;
        areThereContributionRelationships = false;
    }

    public String backToCCC() {
        ccc = Session.getActivSettings().getCCC();
        return "ccc.xhtml?faces-redirect=true";
    }

    public String saveCCC() {
        Ontology activeOnto = Session.getActiveOntology();
        if (ccc.getId() == null) {
            if (cccDAO.alreadyExistingCCC(activeOnto.getId(), ccc)) {
                Messages.addMessage(
                        FacesMessage.SEVERITY_ERROR,
                        i18n.getProperty("err_de_ccc"),
                        null);
                return null;
            }
        }
        ccc.setOntology(activeOnto);
        cccDAO.save(ccc);
        ccc = Session.getActivSettings().getCCC();
        return "ccc.xhtml?faces-redirect=true";
    }

    public String buildRelationshipMatrix() {
        Ontology activeOnto = Session.getActiveOntology();
        List<CCC> allCCC = getAllCCC();

        contributionMatrix.clear();
        dependencyMatrix.clear();
        areThereDependencyRelationships = false;
        areThereContributionRelationships = false;

        int amountOfCCC = allCCC.size();
        for (int i = 0; i < amountOfCCC; i++) {
            contributionMatrix.add(new ArrayList<RelationshipType>());
            dependencyMatrix.add(new ArrayList<Boolean>());
            Long sourceId = allCCC.get(i).getId();
            for (int j = 0; j < amountOfCCC; j++) {
                if (i == j) {
                    contributionMatrix.get(i).add(RelationshipType.NULL);
                    dependencyMatrix.get(i).add(false);
                    continue;
                }
                Long targetId = allCCC.get(j).getId();
                List<RelationshipType> relatTypes = relatDAO.getAllRelationshipTypesBySourceAndTarget(activeOnto.getId(), sourceId, targetId);
                if (relatTypes.contains(RelationshipType.NEGATIVE_CONTRIBUTION)) {
                    contributionMatrix.get(i).add(RelationshipType.NEGATIVE_CONTRIBUTION);
                    areThereContributionRelationships = true;
                } else if (relatTypes.contains(RelationshipType.POSITIVE_CONTRIBUTION)) {
                    contributionMatrix.get(i).add(RelationshipType.POSITIVE_CONTRIBUTION);
                    areThereContributionRelationships = true;
                } else {
                    contributionMatrix.get(i).add(RelationshipType.NULL);
                }
                if (relatTypes.contains(RelationshipType.DEPENDENCY)) {
                    dependencyMatrix.get(i).add(true);
                    areThereDependencyRelationships = true;
                } else {
                    dependencyMatrix.get(i).add(false);
                }
            }
        }
        return "relationshipsMatrix.xhtml?faces-redirect=true";
    }

    public String removeCCC() {
        try {
            cccDAO.remove(ccc);
            ccc = Session.getActivSettings().getCCC();
            return "ccc.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            Messages.addMessage(
                    FacesMessage.SEVERITY_ERROR,
                    i18n.getProperty("err_constraint_exception"),
                    null);
            return null;
        }
    }

    public List<CCC> getAllCCC() {
        return cccDAO.getAllCCC(Session.getActiveOntology().getId());
    }

    public CCC getCcc() {
        return ccc;
    }

    public void setCcc(CCC ccc) {
        this.ccc = ccc;
    }

    public List<List<RelationshipType>> getContributionMatrix() {
        return contributionMatrix;
    }

    public List<List<Boolean>> getDependencyMatrix() {
        return dependencyMatrix;
    }

    public boolean isAreThereContributionRelationships() {
        return areThereContributionRelationships;
    }

    public void setAreThereContributionRelationships(boolean areThereContributionRelationships) {
        this.areThereContributionRelationships = areThereContributionRelationships;
    }

    public boolean isAreThereDependencyRelationships() {
        return areThereDependencyRelationships;
    }

    public void setAreThereDependencyRelationships(boolean areThereDependencyRelationships) {
        this.areThereDependencyRelationships = areThereDependencyRelationships;
    }

}
