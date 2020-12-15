package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({  
    @NamedQuery(name = Dependency.GET, query = "select d from Dependency d where d.project.id = :projid and d.source.id = :sourceId and d.target.id = :targetId"),
    @NamedQuery(name = Dependency.GET_BY_SOURCE, query = "select d from Dependency d where d.project.id = :projid and d.source.id = :sourceId"),
    @NamedQuery(name = Dependency.GET_ALL, query = "select d from Dependency d where d.project.id = :projid")
})    
public class Dependency implements Serializable {
    public static final String GET = "Dependency.GET";
    public static final String GET_ALL = "Dependency.GET_ALL";
    public static final String GET_BY_SOURCE = "Dependency.GET_BY_SOURCE";

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    private Requirement source;
    @ManyToOne(optional = false)
    private Requirement target;
    @ManyToOne(optional = false)
    private Project project;
    @Transient
    private String name;
    
    public Dependency() {
        this.id = null;
        this.project = new Project();
        this.source = new Requirement();
        this.target = new Requirement();     
        this.name = "";
    }
    
    public Dependency(Dependency dep, Requirement newSource, Requirement newTarget, Project prj) {
        this.id = null;
        this.project = prj;
        this.source = newSource;
        this.target = newTarget;     
        this.name = dep.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requirement getSource() {
        return source;
    }

    public void setSource(Requirement source) {
        this.source = source;
    }

    public Requirement getTarget() {
        return target;
    }

    public void setTarget(Requirement target) {
        this.target = target;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return this.getSource().getName() + " >> " + this.getTarget().getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.id != null ? this.id.hashCode() : 0);
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
        final Dependency other = (Dependency) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
}
