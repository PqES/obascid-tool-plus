/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufscar.dc.o3ctool.dao.jpa;

import br.ufscar.dc.o3ctool.model.Project;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author Paulo
 */
public class ProjectJPA implements ProjectDAO, Serializable {

    @Override
    public boolean alreadyExistingProject(Long resid, Project proj) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            return (getByName(resid, proj.getName()) != null);
        } finally {
            em.close();
        }
    }

    @Override
    public Project getByName(Long resid, String name) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Project> tq = em.createNamedQuery(Project.GET_BY_NAME, Project.class);
            tq.setParameter("resid", resid);
            tq.setParameter("name", name);
            List<Project> projList = tq.getResultList();
            if (!projList.isEmpty()) {
                return projList.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public void save(Project proj) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            if (proj.getId() == null) {
                em.persist(proj);
            } else {
                em.merge(proj);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Project> getAllProjects(Long resid) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Project> tq = em.createNamedQuery(Project.GET_ALL_BY_RESEARCHER, Project.class);
            tq.setParameter("resid", resid);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Project> searchProjects(String search, int maxResults) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            TypedQuery<Project> tq = em.createNamedQuery(Project.SEARCH_PROJECTS, Project.class);
            tq.setParameter("license", Project.ProjectLicense.PUBLIC);
            tq.setParameter("search", "%" + search + "%");
            tq.setMaxResults(maxResults);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void remove(Project proj) {
        EntityManager em = JPAUtil.obterConexao();
        try {
            em.getTransaction().begin();
            proj = em.merge(proj);
            em.refresh(proj);
            em.remove(proj);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
