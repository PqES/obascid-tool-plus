/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Ontology;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class OntologyJPA implements OntologyDAO, Serializable {

    @Override
    public void save(Ontology onto) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (onto.getId() == null) {
                em.persist(onto);
            } else {
                em.merge(onto);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ontology> getAllOntologies(Long resid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Ontology> tq = em.createNamedQuery(Ontology.GET_ALL_BY_RESEARCHER, Ontology.class);
            tq.setParameter("resid", resid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Ontology> searchOntologies(String search, int maxResults) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Ontology> tq = em.createNamedQuery(Ontology.SEARCH_ONTOLOGIES, Ontology.class);
            tq.setParameter("license", Ontology.OntologyLicense.PUBLIC);
            tq.setParameter("search", "%" + search + "%");
            tq.setMaxResults(maxResults);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }
    

    @Override
    public Ontology getByName(Long resid, String name) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Ontology> tq = em.createNamedQuery(Ontology.GET_BY_NAME, Ontology.class);
            tq.setParameter("resid", resid);
            tq.setParameter("name", name);
            List<Ontology> ontoList = tq.getResultList();
            if (!ontoList.isEmpty()) {
                return ontoList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean alreadyExistingOntology(Long resid, Ontology onto) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByName(resid, onto.getName()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Ontology onto) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            onto = em.merge(onto);
            em.refresh(onto);
            em.remove(onto);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
