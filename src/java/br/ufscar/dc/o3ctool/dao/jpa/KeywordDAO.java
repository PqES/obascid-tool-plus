package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Keyword;
import java.util.List;

public interface KeywordDAO {
    public boolean alreadyExistingKeyword(Long cccid, Keyword kw);
    public void save(Keyword kw);
    public List<Keyword> getAllKeywords(Long cccid);
    public Keyword getByName(Long cccid, String name);
    public void remove(Keyword kw);
}
