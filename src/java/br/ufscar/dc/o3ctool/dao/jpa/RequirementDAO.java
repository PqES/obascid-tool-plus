package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Requirement;
import java.util.List;

public interface RequirementDAO {
    public void save(Requirement req);
    public boolean alreadyExistingRequirement(Long projid, Requirement req);
    public List<Requirement> getAllRequirements(Long projid);
    public Requirement getByName(Long projid, String name);
    public void remove(Requirement req);
    public void removeAll(Long projid);
}
