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
    @NamedQuery(name = Source.GET_ALL, query = "select s from Source s where s.ccc.id = :cccid"),
    @NamedQuery(name = Source.GET_BY_NAME, query = "select s from Source s where s.ccc.id = :cccid and s.name = :name")
})
public class Source implements Serializable {
    public static enum SourceType {
        CATALOGUE, BUSINESS_DOC, STAKEHOLDER;
    }
    public static final String GET_ALL = "Source.GET_ALL";
    public static final String GET_BY_NAME = "Source.GET_BY_NAME";

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String name;
    @Lob
    private String description;
    @ManyToOne(optional = false)
    private CCC ccc;
    @Enumerated(EnumType.STRING)
    private SourceType sourceType;

    public Source() {
        this.id = null;
        this.name = "";
        this.description = "";
        this.ccc = new CCC();
        this.sourceType = SourceType.CATALOGUE;
    }
    
    public Source(Source source, CCC ccc) {
        this.id = null;
        this.name = source.getName();
        this.description = source.getDescription();
        this.ccc = ccc;
        this.sourceType = source.getSourceType();
    }
    
    public boolean isCatalogue() {
        return (SourceType.CATALOGUE == this.sourceType);
    }

    public boolean isStakeholder() {
        return (SourceType.STAKEHOLDER == this.sourceType);
    }

    public boolean isBusinessdoc() {
        return (SourceType.BUSINESS_DOC == this.sourceType);
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

    public CCC getCcc() {
        return ccc;
    }

    public void setCcc(CCC ccc) {
        this.ccc = ccc;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.name != null ? this.name.hashCode() : 0);
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
        final Source other = (Source) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
    
    
    
}
