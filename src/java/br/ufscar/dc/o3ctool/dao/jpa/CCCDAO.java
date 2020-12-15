package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.CCC;
import java.util.List;

public interface CCCDAO {
    public boolean alreadyExistingCCC(Long ontoid, CCC ccc);
    public void save(CCC ccc);
    public List<CCC> getAllCCC(Long ontoid);
    public CCC getCCCById(Long cccId);
    public CCC getCCCByName(Long ontoid, String cccName);
    public void remove(CCC ccc);
    public void removeAll(Long ontoid);
}
