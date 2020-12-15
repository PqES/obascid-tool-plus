/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Keyword;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class KeywordJPA implements KeywordDAO, Serializable {

    @Override
    public void save(Keyword kw) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (kw.getId() == null) {
                em.persist(kw);
            } else {
                em.merge(kw);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Keyword> getAllKeywords(Long cccid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Keyword> tq = em.createNamedQuery(Keyword.GET_ALL, Keyword.class);
            tq.setParameter("cccid", cccid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Keyword getByName(Long cccid, String name) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Keyword> tq = em.createNamedQuery(Keyword.GET_BY_NAME, Keyword.class);
            tq.setParameter("cccid", cccid);
            tq.setParameter("name", name);
            List<Keyword> kwList = tq.getResultList();
            if (!kwList.isEmpty()) {
                return kwList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean alreadyExistingKeyword(Long cccid, Keyword kw) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByName(cccid, kw.getName()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Keyword kw) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            kw = em.merge(kw);
            em.refresh(kw);
            em.remove(kw);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
