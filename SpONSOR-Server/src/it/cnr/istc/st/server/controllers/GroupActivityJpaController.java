/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.istc.st.server.controllers;

import it.cnr.istc.st.server.controllers.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import it.cnr.istc.st.server.entity.Activity;
import it.cnr.istc.st.server.entity.GroupActivity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Luca Coraci <luca.coraci@istc.cnr.it> ISTC-CNR
 */
public class GroupActivityJpaController implements Serializable {

    public GroupActivityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GroupActivity groupActivity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Activity activity = groupActivity.getActivity();
            if (activity != null) {
                activity = em.getReference(activity.getClass(), activity.getId());
                groupActivity.setActivity(activity);
            }
            em.persist(groupActivity);
            if (activity != null) {
                activity.getGroups().add(groupActivity);
                activity = em.merge(activity);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GroupActivity groupActivity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GroupActivity persistentGroupActivity = em.find(GroupActivity.class, groupActivity.getId());
            Activity activityOld = persistentGroupActivity.getActivity();
            Activity activityNew = groupActivity.getActivity();
            if (activityNew != null) {
                activityNew = em.getReference(activityNew.getClass(), activityNew.getId());
                groupActivity.setActivity(activityNew);
            }
            groupActivity = em.merge(groupActivity);
            if (activityOld != null && !activityOld.equals(activityNew)) {
                activityOld.getGroups().remove(groupActivity);
                activityOld = em.merge(activityOld);
            }
            if (activityNew != null && !activityNew.equals(activityOld)) {
                activityNew.getGroups().add(groupActivity);
                activityNew = em.merge(activityNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = groupActivity.getId();
                if (findGroupActivity(id) == null) {
                    throw new NonexistentEntityException("The groupActivity with id " + id + " no longer exists.");
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
            GroupActivity groupActivity;
            try {
                groupActivity = em.getReference(GroupActivity.class, id);
                groupActivity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The groupActivity with id " + id + " no longer exists.", enfe);
            }
            Activity activity = groupActivity.getActivity();
            if (activity != null) {
                activity.getGroups().remove(groupActivity);
                activity = em.merge(activity);
            }
            em.remove(groupActivity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GroupActivity> findGroupActivityEntities() {
        return findGroupActivityEntities(true, -1, -1);
    }

    public List<GroupActivity> findGroupActivityEntities(int maxResults, int firstResult) {
        return findGroupActivityEntities(false, maxResults, firstResult);
    }

    private List<GroupActivity> findGroupActivityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GroupActivity.class));
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

    public GroupActivity findGroupActivity(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GroupActivity.class, id);
        } finally {
            em.close();
        }
    }

    public int getGroupActivityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GroupActivity> rt = cq.from(GroupActivity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
