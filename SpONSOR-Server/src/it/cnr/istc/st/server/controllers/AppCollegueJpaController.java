/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.app.AppCollegue;
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
public class AppCollegueJpaController implements Serializable {

    public AppCollegueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AppCollegue appCollegue) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(appCollegue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AppCollegue appCollegue) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            appCollegue = em.merge(appCollegue);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = appCollegue.getId();
                if (findAppCollegue(id) == null) {
                    throw new NonexistentEntityException("The appCollegue with id " + id + " no longer exists.");
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
            AppCollegue appCollegue;
            try {
                appCollegue = em.getReference(AppCollegue.class, id);
                appCollegue.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The appCollegue with id " + id + " no longer exists.", enfe);
            }
            em.remove(appCollegue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AppCollegue> findAppCollegueEntities() {
        return findAppCollegueEntities(true, -1, -1);
    }

    public List<AppCollegue> findAppCollegueEntities(int maxResults, int firstResult) {
        return findAppCollegueEntities(false, maxResults, firstResult);
    }

    private List<AppCollegue> findAppCollegueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AppCollegue.class));
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

    public AppCollegue findAppCollegue(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AppCollegue.class, id);
        } finally {
            em.close();
        }
    }

    public int getAppCollegueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AppCollegue> rt = cq.from(AppCollegue.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
