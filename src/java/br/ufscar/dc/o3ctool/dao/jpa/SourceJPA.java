/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Source;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class SourceJPA implements SourceDAO, Serializable {

    @Override
    public void save(Source s) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (s.getId() == null) {
                em.persist(s);
            } else {
                em.merge(s);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Source> getAllSources(Long cccid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Source> tq = em.createNamedQuery(Source.GET_ALL, Source.class);
            tq.setParameter("cccid", cccid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Source getByName(Long cccid, String name) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Source> tq = em.createNamedQuery(Source.GET_BY_NAME, Source.class);
            tq.setParameter("cccid", cccid);
            tq.setParameter("name", name);
            List<Source> sList = tq.getResultList();
            if (!sList.isEmpty()) {
                return sList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean alreadyExistingSource(Long cccid, Source s) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByName(cccid, s.getName()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Source s) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            s = em.merge(s);
            em.refresh(s);
            em.remove(s);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
