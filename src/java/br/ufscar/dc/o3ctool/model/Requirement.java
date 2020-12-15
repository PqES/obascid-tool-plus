package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = Requirement.GET_ALL, query = "select r from Requirement r where r.project.id = :projid"),
    @NamedQuery(name = Requirement.GET_BY_NAME, query = "select r from Requirement r where r.project.id = :projid and r.name= :name"),
    @NamedQuery(name = Requirement.GET_BY_DESCRIPTION, query = "select r from Requirement r where r.project.id = :projid and r.description = :desc")
})
public class Requirement implements Serializable {

    public static enum RequirementType {

        NON_FUNCTIONAL, FUNCTIONAL;
    }

    public static final String GET_ALL = "Requirement.GET_ALL";
    public static final String GET_BY_NAME = "Requirement.GET_BY_NAME";
    public static final String GET_BY_DESCRIPTION = "Requirement.GET_BY_DESCRIPTION";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    @Column(nullable = false)
    private String description;
    @ManyToOne(optional = false)
    private Project project;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequirementType type;
    
//    @ManyToOne(optional = false)
//    private Standard standard;

    public Requirement() {
        this.id = null;
        this.name = "";
        this.description = "";
        this.project = new Project();
//        this.standard = new Standard();
        this.type = RequirementType.NON_FUNCTIONAL;
    }

    public Requirement(Requirement req, Project prj) {
        this.id = null;
        this.name = req.getName();
        this.description = req.getDescription();
        this.project = prj;
        this.type = req.getType();
    }

    public boolean isNonFunctional() {
        return (RequirementType.NON_FUNCTIONAL == this.type);
    }

    public boolean isFunctional() {
        return (RequirementType.FUNCTIONAL == this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    
//    public Standard getStandard() {
//        return standard;
//    }
//
//    public void setStandard(Standard standard) {
//        this.standard = standard;
//    }

    public RequirementType getType() {
        return type;
    }

    public void setType(RequirementType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Requirement other = (Requirement) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
