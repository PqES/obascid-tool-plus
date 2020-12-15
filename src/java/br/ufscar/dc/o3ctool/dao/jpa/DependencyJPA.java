/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Dependency;
import br.ufscar.dc.o3ctool.model.Relationship;
import br.ufscar.dc.o3ctool.model.Relationship.RelationshipType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static org.eclipse.persistence.internal.sessions.coordination.corba.sun.CommandDataHelper.type;

/**
 *
 * @author Paulo
 */
public class DependencyJPA implements DependencyDAO, Serializable {

    @Override
    public List<Dependency> getBySource(Long projid, Long sourceId) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Dependency> tq = em.createNamedQuery(Dependency.GET_BY_SOURCE, Dependency.class);
            tq.setParameter("projid", projid);
            tq.setParameter("sourceId", sourceId);
            return tq.getResultList();
        } finally {
            em.close();
        }

    }

    private Dependency get(Long projid, Long sourceId, Long targetId) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Dependency> tq = em.createNamedQuery(Dependency.GET, Dependency.class);
            tq.setParameter("projid", projid);
            tq.setParameter("sourceId", sourceId);
            tq.setParameter("targetId", targetId);
            List<Dependency> depList = tq.getResultList();
            if (!depList.isEmpty()) {
                return depList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean alreadyExistingDependency(Long projid, Long sourceId, Long targetId) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (get(projid, sourceId, targetId) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Dependency dep) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (dep.getId() == null) {
                em.persist(dep);
            } else {
                em.merge(dep);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Dependency> getAllDependencies(Long projid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Dependency> tq = em.createNamedQuery(Dependency.GET_ALL, Dependency.class);
            tq.setParameter("projid", projid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Dependency dep) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            dep = em.merge(dep);
            em.refresh(dep);
            em.remove(dep);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
