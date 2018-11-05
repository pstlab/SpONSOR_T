/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.app.AppTurnIndex;
import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
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
public class AppTurnIndexJpaController implements Serializable {

    public AppTurnIndexJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AppTurnIndex appTurnIndex) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(appTurnIndex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AppTurnIndex appTurnIndex) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            appTurnIndex = em.merge(appTurnIndex);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = appTurnIndex.getId();
                if (findAppTurnIndex(id) == null) {
                    throw new NonexistentEntityException("The appTurnIndex with id " + id + " no longer exists.");
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
            AppTurnIndex appTurnIndex;
            try {
                appTurnIndex = em.getReference(AppTurnIndex.class, id);
                appTurnIndex.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appTurnIndex with id " + id + " no longer exists.", enfe);
            }
            em.remove(appTurnIndex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AppTurnIndex> findAppTurnIndexEntities() {
        return findAppTurnIndexEntities(true, -1, -1);
    }

    public List<AppTurnIndex> findAppTurnIndexEntities(int maxResults, int firstResult) {
        return findAppTurnIndexEntities(false, maxResults, firstResult);
    }

    private List<AppTurnIndex> findAppTurnIndexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AppTurnIndex.class));
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

    public AppTurnIndex findAppTurnIndex(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AppTurnIndex.class, id);
        } finally {
            em.close();
        }
    }

    public int getAppTurnIndexCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AppTurnIndex> rt = cq.from(AppTurnIndex.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
