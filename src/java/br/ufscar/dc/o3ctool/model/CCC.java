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
import javax.persistence.Transient;

@Entity
@NamedQueries({
    @NamedQuery(name = CCC.GET_ALL, query = "select c from CCC c where c.ontology.id = :ontoid"),
    @NamedQuery(name = CCC.GET_BY_NAME, query = "select c from CCC c where c.ontology.id = :ontoid and c.name = :name")
})
public class CCC implements Serializable {

    public static enum Priority {
        LOW, MEDIUM, HIGH, NOT_SPECIFIED;
    }

    public static enum PriorityComparision {
        LOWER, HIGHER, EQUALS, NOT_SPECIFIED;
    }

    public static enum CCCType {
        NON_FUNCTIONAL, FUNCTIONAL;
    }

    public static final String GET_ALL = "CCC.GET_ALL";
    public static final String GET_BY_NAME = "CCC.GET_BY_NAME";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CCCType type;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @ManyToOne(optional = false)
    private Ontology ontology;
    @Transient
    private String fragments;
    
    public CCC() {
        this.id = null;
        this.name = "";
        this.description = "";
        this.type = CCCType.NON_FUNCTIONAL;
        this.priority = Priority.NOT_SPECIFIED;
        this.fragments = "";
        this.ontology = new Ontology();
    }
    
    public CCC(CCC ccc, Ontology onto) {
        this.id = null;
        this.name = ccc.getName();
        this.description = ccc.getDescription();
        this.type = ccc.getType();
        this.priority = ccc.getPriority();
        this.fragments = "";
        this.ontology = onto;
    }

    public String getFragments() {
        return fragments;
    }

    public void setFragments(String fragments) {
        this.fragments = fragments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isNonFunctional() {
        return (CCCType.NON_FUNCTIONAL == this.type);
    }

    public boolean isFunctional() {
        return (CCCType.FUNCTIONAL == this.type);
    }

    public PriorityComparision comparePriority(Priority other) {
        if (priority == Priority.NOT_SPECIFIED || other == Priority.NOT_SPECIFIED)
            return PriorityComparision.NOT_SPECIFIED;
        if (priority == other)
            return PriorityComparision.EQUALS;
        if ((priority == Priority.LOW && other == Priority.MEDIUM) ||
            (priority == Priority.LOW && other == Priority.HIGH) ||
            (priority == Priority.MEDIUM && other == Priority.HIGH)) 
                return PriorityComparision.LOWER;
        return PriorityComparision.HIGHER;
    }


    public boolean isPriorityLow() {
        return (Priority.LOW == this.priority);
    }

    public boolean isPriorityMedium() {
        return (Priority.MEDIUM == this.priority);
    }

    public boolean isNotSpecified() {
        return (Priority.NOT_SPECIFIED == this.priority);
    }

    public boolean isPriorityHigh() {
        return (Priority.HIGH == this.priority);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CCCType getType() {
        return type;
    }

    public void setType(CCCType type) {
        this.type = type;
    }

    public Ontology getOntology() {
        return ontology;
    }

    public void setOntology(Ontology ontology) {
        this.ontology = ontology;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        final CCC other = (CCC) obj;
        return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }
}
