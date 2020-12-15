/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.MainConcern;
import br.ufscar.dc.o3ctool.model.Requirement;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class MainConcernJPA implements MainConcernDAO, Serializable {

    @Override
    public MainConcern getByRequirement(Long iuid, Requirement req) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<MainConcern> tq = em.createNamedQuery(MainConcern.GET_BY_REQ, MainConcern.class);
            tq.setParameter("iuid", iuid);
            tq.setParameter("reqid", req.getId());
            List<MainConcern> mcList = tq.getResultList();
            if (!mcList.isEmpty()) {
                return mcList.get(0);
            }
            return null;
        } finally {
            em.close();
        }        
    }
    
    private MainConcern get(Long iuid, Long reqId, Long concernId) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<MainConcern> tq = em.createNamedQuery(MainConcern.GET, MainConcern.class);
            tq.setParameter("iuid", iuid);
            tq.setParameter("reqid", reqId);
            tq.setParameter("concernid", concernId);
            List<MainConcern> mcList = tq.getResultList();
            if (!mcList.isEmpty()) {
                return mcList.get(0);
            }
            return null;
        } finally {
            em.close();
        }

    }

    @Override
    public boolean alreadyExistingMainConcern(Long iuid, Long reqId, Long concernId) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (get(iuid, reqId, concernId) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(MainConcern mc) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (mc.getId() == null) {
                em.persist(mc);
            } else {
                em.merge(mc);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<MainConcern> getAllMainConcerns(Long iuid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<MainConcern> tq = em.createNamedQuery(MainConcern.GET_ALL, MainConcern.class);
            tq.setParameter("iuid", iuid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(MainConcern mc) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            mc = em.merge(mc);
            em.refresh(mc);
            em.remove(mc);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
