/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.IdentificationUnit;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class IdentificationUnitJPA implements IdentificationUnitDAO, Serializable {

    @Override
    public boolean alreadyExistingIU(Long projid, IdentificationUnit iu) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<IdentificationUnit> tq = em.createNamedQuery(IdentificationUnit.GET_BY_NAME, IdentificationUnit.class);
            tq.setParameter("projid", projid);
            tq.setParameter("name", iu.getName());
            int count = tq.getResultList().size();
            return (count != 0);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(IdentificationUnit iu) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (iu.getId() == null) {
                em.persist(iu);
            } else {
                em.merge(iu);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<IdentificationUnit> getAllIdentificationUnits(Long projid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<IdentificationUnit> tq = em.createNamedQuery(IdentificationUnit.GET_ALL, IdentificationUnit.class);
            tq.setParameter("projid", projid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(IdentificationUnit iu) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            iu = em.merge(iu);
            em.refresh(iu);
            em.remove(iu);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
