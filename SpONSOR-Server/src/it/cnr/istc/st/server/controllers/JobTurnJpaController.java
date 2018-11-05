/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.JobTurn;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class JobTurnJpaController implements Serializable {

    public JobTurnJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(JobTurn jobTurn) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(jobTurn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(JobTurn jobTurn) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            jobTurn = em.merge(jobTurn);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = jobTurn.getId();
                if (findJobTurn(id) == null) {
                    throw new NonexistentEntityException("The jobTurn with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            JobTurn jobTurn;
            try {
                jobTurn = em.getReference(JobTurn.class, id);
                jobTurn.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The jobTurn with id " + id + " no longer exists.", enfe);
            }
            em.remove(jobTurn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<JobTurn> findJobTurnEntities() {
        return findJobTurnEntities(true, -1, -1);
    }

    public List<JobTurn> findJobTurnEntities(int maxResults, int firstResult) {
        return findJobTurnEntities(false, maxResults, firstResult);
    }

    private List<JobTurn> findJobTurnEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(JobTurn.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public JobTurn findJobTurn(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(JobTurn.class, id);
        } finally {
            em.close();
        }
    }

    public int getJobTurnCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<JobTurn> rt = cq.from(JobTurn.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
