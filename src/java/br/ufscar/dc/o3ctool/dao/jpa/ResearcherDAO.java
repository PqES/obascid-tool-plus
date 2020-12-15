package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Researcher;

public interface ResearcherDAO {
    public boolean alreadyExistingResearcher(Researcher res);
    public Researcher getByEmail(String email);
    public void save(Researcher res);
}
