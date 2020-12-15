package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = IdentificationUnit.GET_ALL, query = "select iu from IdentificationUnit iu where iu.project.id = :projid"),
    @NamedQuery(name = IdentificationUnit.GET_BY_NAME, query = "select iu from IdentificationUnit iu where iu.project.id = :projid and iu.name= :name")
})
public class IdentificationUnit implements Serializable {

    public static final String GET_ALL = "IdentificationUnit.GET_ALL";
    public static final String GET_BY_NAME = "IdentificationUnit.GET_BY_NAME";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne(optional = false)
    private Ontology ontology;
    @ManyToOne(optional = false)
    private Project project;
    private boolean checkDependencies;
    private boolean checkContributions;
    private double matchingThreshold;

    public IdentificationUnit() {
        this.id = null;
        this.name = "";
        this.project = new Project();
        this.ontology = new Ontology();
        this.checkContributions = true;
        this.checkDependencies = true;
        this.matchingThreshold = 1.0f;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isCheckDependencies() {
        return checkDependencies;
    }

    public void setCheckDependencies(boolean checkDependencies) {
        this.checkDependencies = checkDependencies;
    }

    public boolean isCheckContributions() {
        return checkContributions;
    }

    public void setCheckContributions(boolean checkContributions) {
        this.checkContributions = checkContributions;
    }

    public double getMatchingThreshold() {
        return matchingThreshold;
    }

    public void setMatchingThreshold(double matchingThreshold) {
        this.matchingThreshold = matchingThreshold;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IdentificationUnit other = (IdentificationUnit) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
