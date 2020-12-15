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
    @NamedQuery(name = Ontology.GET_ALL, query = "select o from Ontology o where o.license = :license"),
    @NamedQuery(name = Ontology.GET_ALL_BY_RESEARCHER, query = "select o from Ontology o where o.researcher.id = :resid"),
    @NamedQuery(name = Ontology.GET_BY_NAME, query = "select o from Ontology o where o.researcher.id = :resid and o.name = :name"),
    @NamedQuery(name = Ontology.SEARCH_ONTOLOGIES, query = "select o from Ontology o where (o.name like :search or o.description like :search) and o.license = :license")
})
public class Ontology implements Serializable {

    public static enum OntologyLicense {
        PUBLIC, PRIVATE;
    }
    public static final String GET_ALL = "Ontology.GET_ALL";
    public static final String GET_ALL_BY_RESEARCHER = "Ontology.GET_ALL_BY_RESEARCHER";
    public static final String SEARCH_ONTOLOGIES = "Ontology.SEARCH_ONTOLOGIES";
    public static final String GET_BY_NAME = "Ontology.GET_BY_NAME";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    private String description;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OntologyLicense license;
    @ManyToOne(optional = false)
    private Researcher researcher;
    private int views;
    private int imports;

    public Ontology() {
        this.id = null;
        this.name = "";
        this.description = "";
        this.license = OntologyLicense.PRIVATE;
        this.researcher = new Researcher();
        this.views = 0;
        this.imports = 0;
    }
    
    public Ontology(Ontology onto, Researcher res) {
        this.id = null;
        this.name = onto.getName();
        this.description = onto.getDescription();
        this.license = OntologyLicense.PRIVATE;
        this.researcher = res;
        this.views = 0;
        this.imports = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isLicencePublic() {
        return (OntologyLicense.PUBLIC == this.license);
    }

    public boolean isLicencePrivate() {
        return (OntologyLicense.PRIVATE == this.license);
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

    public OntologyLicense getLicense() {
        return license;
    }

    public void setLicense(OntologyLicense license) {
        this.license = license;
    }

    public Researcher getResearcher() {
        return researcher;
    }

    public void setResearcher(Researcher researcher) {
        this.researcher = researcher;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getImports() {
        return imports;
    }

    public void setImports(int imports) {
        this.imports = imports;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        final Ontology other = (Ontology) obj;
        return !((this.name == null) ? (other.name != null) : !this.name.equals(other.name));
    }


}
