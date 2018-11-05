/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.TurnCancellation;
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
public class TurnCancellationJpaController implements Serializable {

    public TurnCancellationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TurnCancellation turnCancellation) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(turnCancellation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TurnCancellation turnCancellation) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            turnCancellation = em.merge(turnCancellation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = turnCancellation.getId();
                if (findTurnCancellation(id) == null) {
                    throw new NonexistentEntityException("The turnCancellation with id " + id + " no longer exists.");
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
            TurnCancellation turnCancellation;
            try {
                turnCancellation = em.getReference(TurnCancellation.class, id);
                turnCancellation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turnCancellation with id " + id + " no longer exists.", enfe);
            }
            em.remove(turnCancellation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TurnCancellation> findTurnCancellationEntities() {
        return findTurnCancellationEntities(true, -1, -1);
    }

    public List<TurnCancellation> findTurnCancellationEntities(int maxResults, int firstResult) {
        return findTurnCancellationEntities(false, maxResults, firstResult);
    }

    private List<TurnCancellation> findTurnCancellationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TurnCancellation.class));
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

    public TurnCancellation findTurnCancellation(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TurnCancellation.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnCancellationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TurnCancellation> rt = cq.from(TurnCancellation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
