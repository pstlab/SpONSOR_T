/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import it.cnr.istc.st.server.entity.SkillValue;
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
public class SkillValueJpaController implements Serializable {

    public SkillValueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SkillValue skillValue) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(skillValue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SkillValue skillValue) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            skillValue = em.merge(skillValue);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = skillValue.getId();
                if (findSkillValue(id) == null) {
                    throw new NonexistentEntityException("The skillValue with id " + id + " no longer exists.");
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
            SkillValue skillValue;
            try {
                skillValue = em.getReference(SkillValue.class, id);
                skillValue.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The skillValue with id " + id + " no longer exists.", enfe);
            }
            em.remove(skillValue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SkillValue> findSkillValueEntities() {
        return findSkillValueEntities(true, -1, -1);
    }

    public List<SkillValue> findSkillValueEntities(int maxResults, int firstResult) {
        return findSkillValueEntities(false, maxResults, firstResult);
    }

    private List<SkillValue> findSkillValueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SkillValue.class));
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

    public SkillValue findSkillValue(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SkillValue.class, id);
        } finally {
            em.close();
        }
    }

    public int getSkillValueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SkillValue> rt = cq.from(SkillValue.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
