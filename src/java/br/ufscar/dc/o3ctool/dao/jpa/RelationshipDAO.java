package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Relationship;
import br.ufscar.dc.o3ctool.model.Relationship.RelationshipType;
import java.util.List;


public interface RelationshipDAO {

    public boolean alreadyExistingRelationship(Long ontoid, Long sourceId, Long targetId, RelationshipType type);

    public void save(Relationship relat);

    public Relationship get(Long ontoid, Long sourceId, Long targetId, RelationshipType type);

    public List<Relationship> getAllRelationships(Long ontoid);

    public List<Relationship> getAllRelationshipsBySourceAndType(Long ontoid, Long sourceid, RelationshipType type);

    public List<Relationship> getAllRelationshipsByTargetAndType(Long ontoid, Long targetid, RelationshipType type);

    public List<Relationship> getAllRelationshipsByTarget(Long ontoid, Long targetid);

    public List<RelationshipType> getAllRelationshipTypesBySourceAndTarget(Long ontoid, Long sourceid, Long targetid);

    public void remove(Relationship relat);
}
