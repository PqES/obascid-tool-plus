/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Relationship;
import br.ufscar.dc.o3ctool.model.Relationship.RelationshipType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class RelationshipJPA implements RelationshipDAO, Serializable {

    @Override
    public Relationship get(Long ontoid, Long sourceId, Long targetId, RelationshipType type) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Relationship> tq = em.createNamedQuery(Relationship.GET, Relationship.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("sourceid", sourceId);
            tq.setParameter("targetid", targetId);
            tq.setParameter("type", type);
            List<Relationship> relatList = tq.getResultList();
            if (!relatList.isEmpty()) {
                return relatList.get(0);
            }
            return null;
        } finally {
            em.close();
        }

    }

    @Override
    public boolean alreadyExistingRelationship(Long ontoid, Long sourceId, Long targetId, RelationshipType type) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return ((get(ontoid, sourceId, targetId, type) != null) || (get(ontoid, targetId, sourceId, type) != null));
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Relationship relat) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (relat.getId() == null) {
                em.persist(relat);
            } else {
                em.merge(relat);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Relationship> getAllRelationships(Long ontoid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Relationship> tq = em.createNamedQuery(Relationship.GET_ALL, Relationship.class);
            tq.setParameter("ontoid", ontoid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Relationship> getAllRelationshipsBySourceAndType(Long ontoid, Long sourceid, RelationshipType type) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Relationship> tq = em.createNamedQuery(Relationship.GET_ALL_BY_SOURCE_AND_TYPE, Relationship.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("sourceid", sourceid);
            tq.setParameter("type", type);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Relationship> getAllRelationshipsByTarget(Long ontoid, Long targetid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Relationship> tq = em.createNamedQuery(Relationship.GET_ALL_BY_TARGET, Relationship.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("targetid", targetid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    public List<RelationshipType> getAllRelationshipTypesBySourceAndTarget(Long ontoid, Long sourceid, Long targetid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Relationship> tq = em.createNamedQuery(Relationship.GET_ALL_TYPES_BY_SOURCE_AND_TARGET, Relationship.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("sourceid", sourceid);
            tq.setParameter("targetid", targetid);
            List<Relationship> relats = tq.getResultList();
            List<RelationshipType> relatTypes = new ArrayList<RelationshipType>();
            for (Relationship relat : relats) {
                relatTypes.add(relat.getType());
            }
            return relatTypes;
        } finally {
            em.close();
        }

    }

    public List<Relationship> getAllRelationshipsByTargetAndType(Long ontoid, Long targetid, RelationshipType type) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Relationship> tq = em.createNamedQuery(Relationship.GET_ALL_BY_TARGET_AND_TYPE, Relationship.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("targetid", targetid);
            tq.setParameter("type", type);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Relationship relat) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            relat = em.merge(relat);
            em.refresh(relat);
            em.remove(relat);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
