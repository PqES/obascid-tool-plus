package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Dependency;
import br.ufscar.dc.o3ctool.model.Relationship;
import java.util.List;


public interface DependencyDAO {

    public boolean alreadyExistingDependency(Long projid, Long sourceId, Long targetId);

    public void save(Dependency dep);

    public List<Dependency> getAllDependencies(Long projid);

    public List<Dependency> getBySource(Long projid, Long sourceId);

    public void remove(Dependency dep);
}
