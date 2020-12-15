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
    @NamedQuery(name = MainConcern.GET, query = "select m from MainConcern m where m.iu.id = :iuid and m.requirement.id = :reqid and m.mainConcern.id = :concernid"),
    @NamedQuery(name = MainConcern.GET_BY_REQ, query = "select m from MainConcern m where m.iu.id = :iuid and m.requirement.id = :reqid"),
    @NamedQuery(name = MainConcern.GET_ALL, query = "select m from MainConcern m where m.iu.id = :iuid")
})
public class MainConcern implements Serializable {

    public static final String GET = "MainConcern.GET";
    public static final String GET_ALL = "MainConcern.GET_ALL";
    public static final String GET_BY_REQ = "MainConcern.GET_BY_REQ";

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false)
    private Requirement requirement;
    @ManyToOne(optional = false)
    private CCC mainConcern;
    @ManyToOne(optional = false)
    private IdentificationUnit iu;
    @Transient
    private String name;

    public MainConcern() {
        this.id = null;
        this.iu = new IdentificationUnit();
        this.requirement = new Requirement();
        this.mainConcern = new CCC();
        this.name = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public void setRequirement(Requirement requirement) {
        this.requirement = requirement;
    }

    public CCC getMainConcern() {
        return mainConcern;
    }

    public void setMainConcern(CCC mainConcern) {
        this.mainConcern = mainConcern;
    }

    public String getName() {
        return this.requirement.getName() + " >> " + this.mainConcern.getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public IdentificationUnit getIu() {
        return iu;
    }

    public void setIu(IdentificationUnit iu) {
        this.iu = iu;
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
        final MainConcern other = (MainConcern) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
