/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.ComfirmedTurn;
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
public class ComfirmedTurnJpaController implements Serializable {

    public ComfirmedTurnJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ComfirmedTurn comfirmedTurn) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(comfirmedTurn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ComfirmedTurn comfirmedTurn) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            comfirmedTurn = em.merge(comfirmedTurn);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = comfirmedTurn.getId();
                if (findComfirmedTurn(id) == null) {
                    throw new NonexistentEntityException("The comfirmedTurn with id " + id + " no longer exists.");
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
            ComfirmedTurn comfirmedTurn;
            try {
                comfirmedTurn = em.getReference(ComfirmedTurn.class, id);
                comfirmedTurn.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comfirmedTurn with id " + id + " no longer exists.", enfe);
            }
            em.remove(comfirmedTurn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ComfirmedTurn> findComfirmedTurnEntities() {
        return findComfirmedTurnEntities(true, -1, -1);
    }

    public List<ComfirmedTurn> findComfirmedTurnEntities(int maxResults, int firstResult) {
        return findComfirmedTurnEntities(false, maxResults, firstResult);
    }

    private List<ComfirmedTurn> findComfirmedTurnEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ComfirmedTurn.class));
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

    public ComfirmedTurn findComfirmedTurn(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ComfirmedTurn.class, id);
        } finally {
            em.close();
        }
    }

    public int getComfirmedTurnCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ComfirmedTurn> rt = cq.from(ComfirmedTurn.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
