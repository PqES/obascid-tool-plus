/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Requirement;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class RequirementJPA implements RequirementDAO, Serializable {

    @Override
    public boolean alreadyExistingRequirement(Long projid, Requirement req) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByName(projid, req.getName()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Requirement req) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (req.getId() == null) {
                em.persist(req);
            } else {
                em.merge(req);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public Requirement getByName(Long projid, String name) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Requirement> tq = em.createNamedQuery(Requirement.GET_BY_NAME, Requirement.class);
            tq.setParameter("projid", projid);
            tq.setParameter("name", name);
            List<Requirement> reqList = tq.getResultList();
            if (!reqList.isEmpty()) {
                return reqList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Requirement> getAllRequirements(Long projid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Requirement> tq = em.createNamedQuery(Requirement.GET_ALL, Requirement.class);
            tq.setParameter("projid", projid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Requirement req) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            req = em.merge(req);
            em.refresh(req);
            em.remove(req);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void removeAll(Long projid) {
        List<Requirement> lsRequirements = getAllRequirements(projid);
        for (Requirement req : lsRequirements) {
            remove(req);
        }
    }
}
