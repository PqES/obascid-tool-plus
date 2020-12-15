/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.dao.jpa.JPAUtil;
import br.ufscar.dc.o3ctool.dao.jpa.ResearcherDAO;
import br.ufscar.dc.o3ctool.model.Researcher;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class ResearcherJPA implements ResearcherDAO, Serializable {

    @Override
    public boolean alreadyExistingResearcher(Researcher res) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByEmail(res.getEmail()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public Researcher getByEmail(String email) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Researcher> tq = em.createNamedQuery(Researcher.GET_BY_EMAIL, Researcher.class);
            tq.setParameter("email", email);
            List<Researcher> resList = tq.getResultList();
            if (!resList.isEmpty()) {
                return resList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Researcher res) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (res.getId() == null) {
                em.persist(res);
            } else {
                em.merge(res);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
