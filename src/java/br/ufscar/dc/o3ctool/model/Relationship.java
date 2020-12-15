package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({  
    @NamedQuery(name = Relationship.GET_ALL, query = "select r from Relationship r where r.ontology.id = :ontoid"),
    @NamedQuery(name = Relationship.GET, query = "select r from Relationship r where r.ontology.id = :ontoid and r.source.id = :sourceid and r.target.id = :targetid and r.type = :type"),
    @NamedQuery(name = Relationship.GET_ALL_BY_SOURCE_AND_TYPE, query = "select r from Relationship r where r.ontology.id = :ontoid and r.source.id = :sourceid and r.type = :type"),
    @NamedQuery(name = Relationship.GET_ALL_BY_TARGET, query = "select r from Relationship r where r.ontology.id = :ontoid and r.target.id = :targetid"),
    @NamedQuery(name = Relationship.GET_ALL_BY_TARGET_AND_TYPE, query = "select r from Relationship r where r.ontology.id = :ontoid and r.target.id = :targetid and r.type = :type"),
    @NamedQuery(name = Relationship.GET_ALL_TYPES_BY_SOURCE_AND_TARGET, query = "select r from Relationship r where r.ontology.id = :ontoid and r.source.id = :sourceid and r.target.id = :targetid")
})    
public class Relationship implements Serializable {
    public static enum RelationshipType {
        DEPENDENCY, NEGATIVE_CONTRIBUTION, POSITIVE_CONTRIBUTION, NULL;
    }    
    public static final String GET_ALL = "Relationship.GET_ALL";
    public static final String GET_ALL_BY_TARGET = "Relationship.GET_ALL_BY_TARGET";
    public static final String GET_ALL_BY_SOURCE_AND_TYPE = "Relationship.GET_ALL_BY_SOURCE_AND_TYPE";
    public static final String GET_ALL_BY_TARGET_AND_TYPE = "Relationship.GET_ALL_BY_TARGET_AND_TYPE";
    public static final String GET_ALL_TYPES_BY_SOURCE_AND_TARGET = "Relationship.GET_ALL_TYPES_BY_SOURCE_AND_TARGET";
    public static final String GET = "Relationship.GET";

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    private CCC source;
    @ManyToOne(optional = false)
    private CCC target;
    @ManyToOne(optional = false)
    private Ontology ontology;
    @Transient
    private String name;
    @Enumerated(EnumType.STRING)
    private RelationshipType type;
    
    public Relationship() {
        this.id = null;
        this.ontology = new Ontology();
        this.source = new CCC();
        this.target = new CCC();     
        this.name = "";
        this.type = RelationshipType.DEPENDENCY;
    }
    
    public Relationship(Relationship relat, CCC newSource, CCC newTarget, Ontology onto) {
        this.id = null;
        this.ontology = onto;
        this.source = newSource;
        this.target = newTarget;     
        this.name = relat.getName();
        this.type = relat.getType();
    }

    public boolean isDependency() {
        return (Relationship.RelationshipType.DEPENDENCY == this.type);
    }
    
    public boolean isNegativeContribution() {
        return (Relationship.RelationshipType.NEGATIVE_CONTRIBUTION == this.type);
    }

    public boolean isPositiveContribution() {
        return (Relationship.RelationshipType.POSITIVE_CONTRIBUTION == this.type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CCC getSource() {
        return source;
    }

    public void setSource(CCC source) {
        this.source = source;
    }

    public CCC getTarget() {
        return target;
    }

    public void setTarget(CCC target) {
        this.target = target;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public String getName() {
        return this.getSource().getName() + " >> " + this.getTarget().getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelationshipType getType() {
        return type;
    }

    public void setType(RelationshipType type) {
        this.type = type;
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
        final Relationship other = (Relationship) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    
}
