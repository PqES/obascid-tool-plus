package br.ufscar.dc.o3ctool.model;

import br.ufscar.dc.o3ctool.model.CCC.CCCType;
import br.ufscar.dc.o3ctool.model.CCC.Priority;
import br.ufscar.dc.o3ctool.model.Ontology.OntologyLicense;
import br.ufscar.dc.o3ctool.model.Project.ProjectLicense;
import br.ufscar.dc.o3ctool.model.Relationship.RelationshipType;
import br.ufscar.dc.o3ctool.model.Requirement.RequirementType;
import br.ufscar.dc.o3ctool.model.Source.SourceType;
import br.ufscar.dc.o3ctool.model.Standard.StandardType;
import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class Settings implements Serializable {
    @Enumerated(EnumType.STRING)
    private OntologyLicense defaultOntologyLicense;
    @Enumerated(EnumType.STRING)
    private ProjectLicense defaultProjectLicense;
    @Enumerated(EnumType.STRING)
    private CCCType defaultCCCType;
    @Enumerated(EnumType.STRING)
    private SourceType defaultSourceType;
    @Enumerated(EnumType.STRING)
    private Priority defaultCCCPriority;
    @Enumerated(EnumType.STRING)
    private RelationshipType defaultRelationshipType;
    @Enumerated(EnumType.STRING)
    private RequirementType defaultRequirementType;
    @Enumerated(EnumType.STRING)
    private StandardType defaultStandardType;
    private double defaultMatchingThreshold;

    public Settings() {
        this.defaultOntologyLicense = OntologyLicense.PRIVATE;
        this.defaultProjectLicense = ProjectLicense.PRIVATE;
        this.defaultCCCType = CCCType.NON_FUNCTIONAL;
        this.defaultSourceType = SourceType.CATALOGUE;
        this.defaultCCCPriority = Priority.NOT_SPECIFIED;
        this.defaultRelationshipType = RelationshipType.DEPENDENCY;
        this.defaultRequirementType = RequirementType.NON_FUNCTIONAL;
        this.defaultMatchingThreshold = 1.0f;
    }

    public CCC getCCC() {
        CCC ccc = new CCC();
        ccc.setType(getDefaultCCCType());
        ccc.setPriority(getDefaultCCCPriority());
        return ccc;
    }

    public Source getSource() {
        Source source = new Source();
        source.setSourceType(getDefaultSourceType());
        return source;
    }
    
    public Ontology getOntology() {
        Ontology onto = new Ontology();
        onto.setLicense(getDefaultOntologyLicense());
        return onto;
    }

    public Project getProject() {
        Project proj = new Project();
        proj.setLicense(getDefaultProjectLicense());
        return proj;
    }
    
    public Relationship getRelationship() {
        Relationship relat = new Relationship();
        relat.setType(getDefaultRelationshipType());
        return relat;
    }

    public Dependency getDependency() {
        return new Dependency();
    }
    
    public MainConcern getMainConcern() {
        return new MainConcern();
    }
    
    public Requirement getRequirement() {
        Requirement req = new Requirement();
        req.setType(getDefaultRequirementType());
        return req;
    }
    
    public Standard getStandard() {
        Standard sta = new Standard();
        sta.setType(getDefaultStandardType());
        return sta;
    }
    
    public IdentificationUnit getIdentificationUnit() {
        IdentificationUnit iu = new IdentificationUnit();
        iu.setMatchingThreshold(getDefaultMatchingThreshold());
        return iu;
    }

    public OntologyLicense getDefaultOntologyLicense() {
        return defaultOntologyLicense;
    }

    public void setDefaultOntologyLicense(OntologyLicense defaultOntologyLicense) {
        this.defaultOntologyLicense = defaultOntologyLicense;
    }

    public ProjectLicense getDefaultProjectLicense() {
        return defaultProjectLicense;
    }

    public void setDefaultProjectLicense(ProjectLicense defaultProjectLicense) {
        this.defaultProjectLicense = defaultProjectLicense;
    }

    public CCCType getDefaultCCCType() {
        return defaultCCCType;
    }

    public void setDefaultCCCType(CCCType defaultCCCType) {
        this.defaultCCCType = defaultCCCType;
    }

    public SourceType getDefaultSourceType() {
        return defaultSourceType;
    }

    public void setDefaultSourceType(SourceType defaultSourceType) {
        this.defaultSourceType = defaultSourceType;
    }


    public Priority getDefaultCCCPriority() {
        return defaultCCCPriority;
    }

    public void setDefaultCCCPriority(Priority defaultCCCPriority) {
        this.defaultCCCPriority = defaultCCCPriority;
    }

    public RelationshipType getDefaultRelationshipType() {
        return defaultRelationshipType;
    }

    public void setDefaultRelationshipType(RelationshipType defaultRelationshipType) {
        this.defaultRelationshipType = defaultRelationshipType;
    }

    public RequirementType getDefaultRequirementType() {
        return defaultRequirementType;
    }
    
    public void setDefaultRequirementType(RequirementType defaultRequirementType) {
        this.defaultRequirementType = defaultRequirementType;
    }
    
    public StandardType getDefaultStandardType() {
        return defaultStandardType;
    }
    
    public void setDefaultStandardType(StandardType defaultStandardType) {
        this.defaultStandardType = defaultStandardType;
    }

    public double getDefaultMatchingThreshold() {
        return defaultMatchingThreshold;
    }

    public void setDefaultMatchingThreshold(double defaultMatchingThreshold) {
        this.defaultMatchingThreshold = defaultMatchingThreshold;
    }
}
