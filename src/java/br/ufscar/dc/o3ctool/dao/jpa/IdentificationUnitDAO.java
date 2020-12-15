package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.IdentificationUnit;
import java.util.List;

public interface IdentificationUnitDAO {
    public boolean alreadyExistingIU(Long ontoid, IdentificationUnit iu);
    public void save(IdentificationUnit iu);
    public List<IdentificationUnit> getAllIdentificationUnits(Long projid);
    public void remove(IdentificationUnit iu);
}
