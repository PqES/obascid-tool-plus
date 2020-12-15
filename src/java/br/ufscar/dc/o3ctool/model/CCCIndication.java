package br.ufscar.dc.o3ctool.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CCCIndication implements Serializable {

    private Requirement req;
    private List<CCC> cccIndications;
    private CCC mainConcern;

    public CCCIndication() {
        this.req = new Requirement();
        this.mainConcern = null;                
        this.cccIndications = new ArrayList<CCC>();
    }

    public CCCIndication(Requirement req, List<CCC> cccIndications) {
        this.req = req;
        this.cccIndications = cccIndications;
    }

    public Requirement getReq() {
        return req;
    }

    public void setReq(Requirement req) {
        this.req = req;
    }

    public List<CCC> getCccIndications() {
        return cccIndications;
    }

    public void setCccIndications(List<CCC> cccIndications) {
        this.cccIndications = cccIndications;
    }

    public CCC getMainConcern() {
        return mainConcern;
    }

    public void setMainConcern(CCC mainConcern) {
        this.mainConcern = mainConcern;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.req != null ? this.req.hashCode() : 0);
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
        final CCCIndication other = (CCCIndication) obj;
        if (this.req != other.req && (this.req == null || !this.req.equals(other.req))) {
            return false;
        }
        return true;
    }

}
