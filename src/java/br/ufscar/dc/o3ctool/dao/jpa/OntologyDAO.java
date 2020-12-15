package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Ontology;
import java.util.List;

public interface OntologyDAO {
    public boolean alreadyExistingOntology(Long resid, Ontology onto);
    public Ontology getByName(Long resid, String name);
    public void save(Ontology onto);
    public List<Ontology> getAllOntologies(Long resid);
    public List<Ontology> searchOntologies(String search, int maxResults);
    public void remove(Ontology onto);
}
