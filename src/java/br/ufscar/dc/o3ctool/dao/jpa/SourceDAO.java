package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Source;
import java.util.List;

public interface SourceDAO {
    public boolean alreadyExistingSource(Long cccid, Source s);
    public void save(Source s);
    public List<Source> getAllSources(Long cccid);
    public Source getByName(Long cccid, String name);
    public void remove(Source s);
}
