/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.CCC;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class CCCJPA implements CCCDAO, Serializable {

    @Override
    public void save(CCC ccc) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (ccc.getId() == null) {
                em.persist(ccc);
            } else {
                em.merge(ccc);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<CCC> getAllCCC(Long ontoid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<CCC> tq = em.createNamedQuery(CCC.GET_ALL, CCC.class);
            tq.setParameter("ontoid", ontoid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public CCC getCCCById(Long cccId) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            CCC ccc = em.find(CCC.class, cccId);
            em.refresh(ccc);
            return ccc;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean alreadyExistingCCC(Long ontoid, CCC ccc) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<CCC> tq = em.createNamedQuery(CCC.GET_BY_NAME, CCC.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("name", ccc.getName());
            int count = tq.getResultList().size();
            return (count != 0);
        } finally {
            em.close();
        }
    }

    @Override
    public CCC getCCCByName(Long ontoid, String cccName) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<CCC> tq = em.createNamedQuery(CCC.GET_BY_NAME, CCC.class);
            tq.setParameter("ontoid", ontoid);
            tq.setParameter("name", cccName);
            if (tq.getResultList().isEmpty()) {
                return null;
            } else {
                return tq.getResultList().get(0);
            }
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(CCC ccc) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            ccc = em.merge(ccc);
            em.refresh(ccc);
            em.remove(ccc);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public void removeAll(Long ontoid) {
        List<CCC> lsCCC = getAllCCC(ontoid);
        for (CCC ccc : lsCCC) {
            remove(ccc);
        }
    }

}
