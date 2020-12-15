package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.MainConcern;
import br.ufscar.dc.o3ctool.model.Requirement;
import java.util.List;


public interface MainConcernDAO {

    public boolean alreadyExistingMainConcern(Long iuid, Long reqId, Long concernId);

    public void save(MainConcern mc);

    public List<MainConcern> getAllMainConcerns(Long iuid);

    public MainConcern getByRequirement(Long iuid, Requirement req);

    public void remove(MainConcern mc);
}
