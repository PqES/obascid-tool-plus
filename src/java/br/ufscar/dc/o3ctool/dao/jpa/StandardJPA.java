/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Standard;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Michael
 */

public class StandardJPA implements StandardDAO, Serializable {

//    @Override
//    public boolean alreadyExistingStandard(Long projid, Standard sta) {
//        EntityManager em = JPAUtil.obterConexao();
//        try {
//            return (getByName(projid, sta.getName()) != null);
//        } finally {
//            em.close();
//        }
//    }
    @Override
    public boolean alreadyExistingStandard(Standard sta) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByName(sta.getName()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Standard sta) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (sta.getId() == null) {
                em.persist(sta);
            } else {
                em.merge(sta);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

//    @Override
//    public Standard getByName(Long projid, String name) {
//        EntityManager em = JPAUtil.obterConexao();
//        try {
//            TypedQuery<Standard> tq = em.createNamedQuery(Standard.GET_BY_NAME, Standard.class);
//            tq.setParameter("projid", projid);
//            tq.setParameter("name", name);
//            List<Standard> staList = tq.getResultList();
//            if (!staList.isEmpty()) {
//                return staList.get(0);
//            }
//            return null;
//        } finally {
//            em.close();
//        }
//    }
    @Override
    public Standard getByName(String name) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Standard> tq = em.createNamedQuery(Standard.GET_BY_NAME, Standard.class);
            tq.setParameter("name", name);
            List<Standard> staList = tq.getResultList();
            if (!staList.isEmpty()) {
                return staList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }
    
    @Override
    public String getByIdStandard(Long idStandard) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<String> tq = em.createQuery("select s.template from Standard s where s.id= :id", String.class);
            tq.setParameter("id", idStandard);
            String staTexto = tq.getSingleResult();
            if (staTexto != null) {
                return staTexto;
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Standard> getAllStandards() {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Standard> tq = em.createNamedQuery(Standard.GET_ALL, Standard.class);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Standard sta) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            sta = em.merge(sta);
            em.refresh(sta);
            em.remove(sta);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

//    @Override
//    public void removeAll(Long projid) {
//        List<Standard> lsStandards = getAllStandards(projid);
//        for (Standard sta : lsStandards) {
//            remove(sta);
//        }
//    }
    @Override
    public void removeAll() {
        List<Standard> lsStandards = getAllStandards();
        for (Standard sta : lsStandards) {
            remove(sta);
        }
    }
}
