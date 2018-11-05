/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.ActivityTurnKeyword;
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
public class ActivityTurnKeywordJpaController implements Serializable {

    public ActivityTurnKeywordJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ActivityTurnKeyword activityTurnKeyword) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(activityTurnKeyword);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ActivityTurnKeyword activityTurnKeyword) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            activityTurnKeyword = em.merge(activityTurnKeyword);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = activityTurnKeyword.getId();
                if (findActivityTurnKeyword(id) == null) {
                    throw new NonexistentEntityException("The activityTurnKeyword with id " + id + " no longer exists.");
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
            ActivityTurnKeyword activityTurnKeyword;
            try {
                activityTurnKeyword = em.getReference(ActivityTurnKeyword.class, id);
                activityTurnKeyword.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The activityTurnKeyword with id " + id + " no longer exists.", enfe);
            }
            em.remove(activityTurnKeyword);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ActivityTurnKeyword> findActivityTurnKeywordEntities() {
        return findActivityTurnKeywordEntities(true, -1, -1);
    }

    public List<ActivityTurnKeyword> findActivityTurnKeywordEntities(int maxResults, int firstResult) {
        return findActivityTurnKeywordEntities(false, maxResults, firstResult);
    }

    private List<ActivityTurnKeyword> findActivityTurnKeywordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ActivityTurnKeyword.class));
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

    public ActivityTurnKeyword findActivityTurnKeyword(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ActivityTurnKeyword.class, id);
        } finally {
            em.close();
        }
    }

    public int getActivityTurnKeywordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ActivityTurnKeyword> rt = cq.from(ActivityTurnKeyword.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
