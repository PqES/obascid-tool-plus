package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Michael
 */

@Entity
@NamedQueries({
    @NamedQuery(name = Standard.GET_ALL, query = "select s from Standard s"),
    @NamedQuery(name = Standard.GET_BY_NAME, query = "select s from Standard s where s.name= :name"),
    @NamedQuery(name = Standard.GET_BY_PROBLEM, query = "select s from Standard s where s.problem = :desc")
})

public class Standard implements Serializable {

    public static enum StandardType {

        NON_FUNCTIONAL, FUNCTIONAL;
    }

    public static final String GET_ALL = "Standard.GET_ALL";
    public static final String GET_BY_NAME = "Standard.GET_BY_NAME";
    public static final String GET_BY_PROBLEM = "Standard.GET_BY_PROBLEM";
    public static final String GET_BY_ID_STANDARD = "Standard.GET_BY_ID_STANDARD";

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(nullable = false)
    private String name;    
    @Lob
    
    @Column(nullable = false)
    private String domain;
    
    @Column(nullable = false)
    private String purpose;
    
    @Column(nullable = false)
    private String problem;
    
    @Column(nullable = false)
    private String consequence;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StandardType type;
    
    @Column(nullable = false)
    private String template;
    
    @Column(nullable = false)
    private String related_standards;
    
//    @ManyToOne(optional = true)
//    private Project project;    

    public Standard() {
        this.id = null;
        this.name = "";
        this.domain = "";
        this.purpose = "";
        this.problem = "";
        this.consequence = "";
        this.type = StandardType.NON_FUNCTIONAL;
        this.template = "";
        this.related_standards = "";        
//        this.project = new Project();
    }

    public Standard(Standard sta, Project prj) {
        this.id = null;
        this.name = sta.getName();
        this.domain = sta.getDomain();
        this.purpose = sta.getPurpose();
        this.problem = sta.getProblem();
        this.consequence = sta.getConsequence();
        this.type = sta.getType();
        this.template = sta.getTemplate();
        this.related_standards = sta.getRelated_standards();        
//        this.project = prj;        
    }
    
    public boolean isNonFunctional() {
        return (StandardType.NON_FUNCTIONAL == this.type);
    }

    public boolean isFunctional() {
        return (StandardType.FUNCTIONAL == this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getRelated_standards() {
        return related_standards;
    }

    public void setRelated_standards(String related_standards) {
        this.related_standards = related_standards;
    }   

//    public Project getProject() {
//        return project;
//    }
//
//    public void setProject(Project project) {
//        this.project = project;
//    }

    public StandardType getType() {
        return type;
    }

    public void setType(StandardType type) {
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
        final Standard other = (Standard) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
